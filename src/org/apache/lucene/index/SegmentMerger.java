package org.apache.lucene.index;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.FieldInfo.IndexOptions;
import org.apache.lucene.index.MergePolicy.MergeAbortedException;
import org.apache.lucene.index.PayloadProcessorProvider.PayloadProcessor;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.util.IOUtils;
import org.apache.lucene.util.ReaderUtil;

/**
 * The SegmentMerger class combines two or more Segments, represented by an IndexReader ({@link #add},
 * into a single Segment.  After adding the appropriate readers, call the merge method to combine the 
 * segments.
 * 
 * @see #merge
 * @see #add
 */
final class SegmentMerger {
  private Directory directory;
  private String segment;
  private int termIndexInterval = IndexWriterConfig.DEFAULT_TERM_INDEX_INTERVAL;

  private List<IndexReader> readers = new ArrayList<IndexReader>();
  private final FieldInfos fieldInfos;
  
  private int mergedDocs;

  private final CheckAbort checkAbort;

  /** Maximum number of contiguous documents to bulk-copy
      when merging stored fields */
  private final static int MAX_RAW_MERGE_DOCS = 4192;

  private SegmentWriteState segmentWriteState;

  private final PayloadProcessorProvider payloadProcessorProvider;
  
  SegmentMerger(Directory dir, int termIndexInterval, String name, MergePolicy.OneMerge merge, PayloadProcessorProvider payloadProcessorProvider, FieldInfos fieldInfos) {
    this.payloadProcessorProvider = payloadProcessorProvider;
    directory = dir;
    this.fieldInfos = fieldInfos;
    segment = name;
    if (merge != null) {
      checkAbort = new CheckAbort(merge, directory);
    } else {
      checkAbort = new CheckAbort(null, null) {
        @Override
        public void work(double units) throws MergeAbortedException {
          // do nothing
        }
      };
    }
    this.termIndexInterval = termIndexInterval;
  }

  public FieldInfos fieldInfos() {
    return fieldInfos;
  }

  /**
   * Add an IndexReader to the collection of readers that are to be merged
   * @param reader
   */
  final void add(IndexReader reader) {
    ReaderUtil.gatherSubReaders(readers, reader);
  }

  /**
   * Merges the readers specified by the {@link #add} method into the directory passed to the constructor
   * @return The number of documents that were merged
   * @throws CorruptIndexException if the index is corrupt
   * @throws IOException if there is a low-level IO error
   */
  final int merge() throws CorruptIndexException, IOException {
    // NOTE: it's important to add calls to
    // checkAbort.work(...) if you make any changes to this
    // method that will spend alot of time.  The frequency
    // of this check impacts how long
    // IndexWriter.close(false) takes to actually stop the
    // threads.

    mergedDocs = mergeFields();
    mergeTerms();
    mergeNorms();

    if (fieldInfos.hasVectors())
      mergeVectors();

    return mergedDocs;
  }

  /**
   * NOTE: this method creates a compound file for all files returned by
   * info.files(). While, generally, this may include separate norms and
   * deletion files, this SegmentInfo must not reference such files when this
   * method is called, because they are not allowed within a compound file.
   */
  final Collection<String> createCompoundFile(String fileName, final SegmentInfo info)
          throws IOException {
    // Now merge all added files
    Collection<String> files = info.files();
    CompoundFileWriter cfsWriter = new CompoundFileWriter(directory, fileName, checkAbort);
    for (String file : files) {
      assert !IndexFileNames.matchesExtension(file, IndexFileNames.DELETES_EXTENSION) 
                : ".del file is not allowed in .cfs: " + file;
      assert !IndexFileNames.isSeparateNormsFile(file)
                : "separate norms file (.s[0-9]+) is not allowed in .cfs: " + file;
      cfsWriter.addFile(file);
    }
    
    // Perform the merge
    cfsWriter.close();
   
    return files;
  }

  private SegmentReader[] matchingSegmentReaders;
  private int[] rawDocLengths;
  private int[] rawDocLengths2;
  private int matchedCount;

  public int getMatchedSubReaderCount() {
    return matchedCount;
  }

  private void setMatchingSegmentReaders() {
    // If the i'th reader is a SegmentReader and has
    // identical fieldName -> number mapping, then this
    // array will be non-null at position i:
    int numReaders = readers.size();
    matchingSegmentReaders = new SegmentReader[numReaders];

    // If this reader is a SegmentReader, and all of its
    // field name -> number mappings match the "merged"
    // FieldInfos, then we can do a bulk copy of the
    // stored fields:
    for (int i = 0; i < numReaders; i++) {
      IndexReader reader = readers.get(i);
      // TODO: we may be able to broaden this to
      // non-SegmentReaders, since FieldInfos is now
      // required?  But... this'd also require exposing
      // bulk-copy (TVs and stored fields) API in foreign
      // readers..
      if (reader instanceof SegmentReader) {
        SegmentReader segmentReader = (SegmentReader) reader;
        boolean same = true;
        FieldInfos segmentFieldInfos = segmentReader.getFieldInfos();
        int numFieldInfos = segmentFieldInfos.size();
        for (int j = 0; j < numFieldInfos; j++) {
          if (!fieldInfos.fieldName(j).equals(segmentFieldInfos.fieldName(j))) {
            same = false;
            break;
          }
        }
        if (same) {
          matchingSegmentReaders[i] = segmentReader;
          matchedCount++;
        }
      }
    }

    // Used for bulk-reading raw bytes for stored fields
    rawDocLengths = new int[MAX_RAW_MERGE_DOCS];
    rawDocLengths2 = new int[MAX_RAW_MERGE_DOCS];
  }

  /**
   * 
   * @return The number of documents in all of the readers
   * @throws CorruptIndexException if the index is corrupt
   * @throws IOException if there is a low-level IO error
   */
  private int mergeFields() throws CorruptIndexException, IOException {

    for (IndexReader reader : readers) {
      fieldInfos.add(reader.getFieldInfos());
    }
    fieldInfos.write(directory, segment + ".fnm");

    int docCount = 0;

    setMatchingSegmentReaders();

    final FieldsWriter fieldsWriter = new FieldsWriter(directory, segment, fieldInfos);

    try {
      int idx = 0;
      for (IndexReader reader : readers) {
        final SegmentReader matchingSegmentReader = matchingSegmentReaders[idx++];
        FieldsReader matchingFieldsReader = null;
        if (matchingSegmentReader != null) {
          final FieldsReader fieldsReader = matchingSegmentReader.getFieldsReader();
          if (fieldsReader != null && fieldsReader.canReadRawDocs()) {
            matchingFieldsReader = fieldsReader;
          }
        }
        if (reader.hasDeletions()) {
          docCount += copyFieldsWithDeletions(fieldsWriter,
                                              reader, matchingFieldsReader);
        } else {
          docCount += copyFieldsNoDeletions(fieldsWriter,
                                            reader, matchingFieldsReader);
        }
      }
      fieldsWriter.finish(docCount);
    } finally {
      fieldsWriter.close();
    }

    segmentWriteState = new SegmentWriteState(null, directory, segment, fieldInfos, docCount, termIndexInterval, null);
    return docCount;
  }

  private int copyFieldsWithDeletions(final FieldsWriter fieldsWriter, final IndexReader reader,
                                      final FieldsReader matchingFieldsReader)
    throws IOException, MergeAbortedException, CorruptIndexException {
    int docCount = 0;
    final int maxDoc = reader.maxDoc();
    if (matchingFieldsReader != null) {
      // We can bulk-copy because the fieldInfos are "congruent"
      for (int j = 0; j < maxDoc;) {
        if (reader.isDeleted(j)) {
          // skip deleted docs
          ++j;
          continue;
        }
        // We can optimize this case (doing a bulk byte copy) since the field 
        // numbers are identical
        int start = j, numDocs = 0;
        do {
          j++;
          numDocs++;
          if (j >= maxDoc) break;
          if (reader.isDeleted(j)) {
            j++;
            break;
          }
        } while(numDocs < MAX_RAW_MERGE_DOCS);
        
        IndexInput stream = matchingFieldsReader.rawDocs(rawDocLengths, start, numDocs);
        fieldsWriter.addRawDocuments(stream, rawDocLengths, numDocs);
        docCount += numDocs;
        checkAbort.work(300 * numDocs);
      }
    } else {
      for (int j = 0; j < maxDoc; j++) {
        if (reader.isDeleted(j)) {
          // skip deleted docs
          continue;
        }
        // NOTE: it's very important to first assign to doc then pass it to
        // termVectorsWriter.addAllDocVectors; see LUCENE-1282
        Document doc = reader.document(j);
        fieldsWriter.addDocument(doc);
        docCount++;
        checkAbort.work(300);
      }
    }
    return docCount;
  }

  private int copyFieldsNoDeletions(final FieldsWriter fieldsWriter, final IndexReader reader,
                                    final FieldsReader matchingFieldsReader)
    throws IOException, MergeAbortedException, CorruptIndexException {
    final int maxDoc = reader.maxDoc();
    int docCount = 0;
    if (matchingFieldsReader != null) {
      // We can bulk-copy because the fieldInfos are "congruent"
      while (docCount < maxDoc) {
        int len = Math.min(MAX_RAW_MERGE_DOCS, maxDoc - docCount);
        IndexInput stream = matchingFieldsReader.rawDocs(rawDocLengths, docCount, len);
        fieldsWriter.addRawDocuments(stream, rawDocLengths, len);
        docCount += len;
        checkAbort.work(300 * len);
      }
    } else {
      for (; docCount < maxDoc; docCount++) {
        // NOTE: it's very important to first assign to doc then pass it to
        // termVectorsWriter.addAllDocVectors; see LUCENE-1282
        Document doc = reader.document(docCount);
        fieldsWriter.addDocument(doc);
        checkAbort.work(300);
      }
    }
    return docCount;
  }

  /**
   * Merge the TermVectors from each of the segments into the new one.
   * @throws IOException
   */
  private final void mergeVectors() throws IOException {
    TermVectorsWriter termVectorsWriter = 
      new TermVectorsWriter(directory, segment, fieldInfos);

    try {
      int idx = 0;
      for (final IndexReader reader : readers) {
        final SegmentReader matchingSegmentReader = matchingSegmentReaders[idx++];
        TermVectorsReader matchingVectorsReader = null;
        if (matchingSegmentReader != null) {
          TermVectorsReader vectorsReader = matchingSegmentReader.getTermVectorsReader();

          // If the TV* files are an older format then they cannot read raw docs:
          if (vectorsReader != null && vectorsReader.canReadRawDocs()) {
            matchingVectorsReader = vectorsReader;
          }
        }
        if (reader.hasDeletions()) {
          copyVectorsWithDeletions(termVectorsWriter, matchingVectorsReader, reader);
        } else {
          copyVectorsNoDeletions(termVectorsWriter, matchingVectorsReader, reader);
          
        }
      }
      termVectorsWriter.finish(mergedDocs);
    } finally {
      termVectorsWriter.close();
    }
  }

  private void copyVectorsWithDeletions(final TermVectorsWriter termVectorsWriter,
                                        final TermVectorsReader matchingVectorsReader,
                                        final IndexReader reader)
    throws IOException, MergeAbortedException {
    final int maxDoc = reader.maxDoc();
    if (matchingVectorsReader != null) {
      // We can bulk-copy because the fieldInfos are "congruent"
      for (int docNum = 0; docNum < maxDoc;) {
        if (reader.isDeleted(docNum)) {
          // skip deleted docs
          ++docNum;
          continue;
        }
        // We can optimize this case (doing a bulk byte copy) since the field 
        // numbers are identical
        int start = docNum, numDocs = 0;
        do {
          docNum++;
          numDocs++;
          if (docNum >= maxDoc) break;
          if (reader.isDeleted(docNum)) {
            docNum++;
            break;
          }
        } while(numDocs < MAX_RAW_MERGE_DOCS);
        
        matchingVectorsReader.rawDocs(rawDocLengths, rawDocLengths2, start, numDocs);
        termVectorsWriter.addRawDocuments(matchingVectorsReader, rawDocLengths, rawDocLengths2, numDocs);
        checkAbort.work(300 * numDocs);
      }
    } else {
      for (int docNum = 0; docNum < maxDoc; docNum++) {
        if (reader.isDeleted(docNum)) {
          // skip deleted docs
          continue;
        }
        
        // NOTE: it's very important to first assign to vectors then pass it to
        // termVectorsWriter.addAllDocVectors; see LUCENE-1282
        TermFreqVector[] vectors = reader.getTermFreqVectors(docNum);
        termVectorsWriter.addAllDocVectors(vectors);
        checkAbort.work(300);
      }
    }
  }
  
  private void copyVectorsNoDeletions(final TermVectorsWriter termVectorsWriter,
                                      final TermVectorsReader matchingVectorsReader,
                                      final IndexReader reader)
      throws IOException, MergeAbortedException {
    final int maxDoc = reader.maxDoc();
    if (matchingVectorsReader != null) {
      // We can bulk-copy because the fieldInfos are "congruent"
      int docCount = 0;
      while (docCount < maxDoc) {
        int len = Math.min(MAX_RAW_MERGE_DOCS, maxDoc - docCount);
        matchingVectorsReader.rawDocs(rawDocLengths, rawDocLengths2, docCount, len);
        termVectorsWriter.addRawDocuments(matchingVectorsReader, rawDocLengths, rawDocLengths2, len);
        docCount += len;
        checkAbort.work(300 * len);
      }
    } else {
      for (int docNum = 0; docNum < maxDoc; docNum++) {
        // NOTE: it's very important to first assign to vectors then pass it to
        // termVectorsWriter.addAllDocVectors; see LUCENE-1282
        TermFreqVector[] vectors = reader.getTermFreqVectors(docNum);
        termVectorsWriter.addAllDocVectors(vectors);
        checkAbort.work(300);
      }
    }
  }

  private SegmentMergeQueue queue = null;

  private final void mergeTerms() throws CorruptIndexException, IOException {

    final FormatPostingsFieldsConsumer fieldsConsumer = new FormatPostingsFieldsWriter(segmentWriteState, fieldInfos);

    try {
      queue = new SegmentMergeQueue(readers.size());

      mergeTermInfos(fieldsConsumer);

    } finally {
      try {
        fieldsConsumer.finish();
      } finally {
        if (queue != null) {
          queue.close();
        }
      }
    }
  }

  IndexOptions indexOptions;

  private final void mergeTermInfos(final FormatPostingsFieldsConsumer consumer) throws CorruptIndexException, IOException {
    int base = 0;
    final int readerCount = readers.size();
    for (int i = 0; i < readerCount; i++) {
      IndexReader reader = readers.get(i);
      TermEnum termEnum = reader.terms();
      SegmentMergeInfo smi = new SegmentMergeInfo(base, termEnum, reader);
      if (payloadProcessorProvider != null) {
        smi.readerPayloadProcessor = payloadProcessorProvider.getReaderProcessor(reader);
      }
      int[] docMap  = smi.getDocMap();
      if (docMap != null) {
        if (docMaps == null) {
          docMaps = new int[readerCount][];
        }
        docMaps[i] = docMap;
      }
      
      base += reader.numDocs();

      assert reader.numDocs() == reader.maxDoc() - smi.delCount;

      if (smi.next())
        queue.add(smi);				  // initialize queue
      else
        smi.close();
    }

    SegmentMergeInfo[] match = new SegmentMergeInfo[readers.size()];

    String currentField = null;
    FormatPostingsTermsConsumer termsConsumer = null;

    while (queue.size() > 0) {
      int matchSize = 0;			  // pop matching terms
      match[matchSize++] = queue.pop();
      Term term = match[0].term;
      SegmentMergeInfo top = queue.top();

      while (top != null && term.compareTo(top.term) == 0) {
        match[matchSize++] =  queue.pop();
        top =  queue.top();
      }

      if (currentField != term.field) {
        currentField = term.field;
        if (termsConsumer != null)
          termsConsumer.finish();
        final FieldInfo fieldInfo = fieldInfos.fieldInfo(currentField);
        termsConsumer = consumer.addField(fieldInfo);
        indexOptions = fieldInfo.indexOptions;
      }

      int df = appendPostings(termsConsumer, match, matchSize);		  // add new TermInfo
      checkAbort.work(df/3.0);

      while (matchSize > 0) {
        SegmentMergeInfo smi = match[--matchSize];
        if (smi.next())
          queue.add(smi);			  // restore queue
        else
          smi.close();				  // done with a segment
      }
    }
  }

  private byte[] payloadBuffer;
  private int[][] docMaps;

  /** Process postings from multiple segments all positioned on the
   *  same term. Writes out merged entries into freqOutput and
   *  the proxOutput streams.
   *
   * @param smis array of segments
   * @param n number of cells in the array actually occupied
   * @return number of documents across all segments where this term was found
   * @throws CorruptIndexException if the index is corrupt
   * @throws IOException if there is a low-level IO error
   */
  private final int appendPostings(final FormatPostingsTermsConsumer termsConsumer, SegmentMergeInfo[] smis, int n)
        throws CorruptIndexException, IOException {

    final FormatPostingsDocsConsumer docConsumer = termsConsumer.addTerm(smis[0].term.text);
    int df = 0;
    for (int i = 0; i < n; i++) {
      SegmentMergeInfo smi = smis[i];
      TermPositions postings = smi.getPositions();
      assert postings != null;
      int base = smi.base;
      int[] docMap = smi.getDocMap();
      postings.seek(smi.termEnum);

      PayloadProcessor payloadProcessor = null;
      if (smi.readerPayloadProcessor != null) {
        payloadProcessor = smi.readerPayloadProcessor.getProcessor(smi.term);
      }

      while (postings.next()) {
        df++;
        int doc = postings.doc();
        if (docMap != null)
          doc = docMap[doc];                      // map around deletions
        doc += base;                              // convert to merged space

        final int freq = postings.freq();
        final FormatPostingsPositionsConsumer posConsumer = docConsumer.addDoc(doc, freq);

        if (indexOptions == IndexOptions.DOCS_AND_FREQS_AND_POSITIONS) {
          for (int j = 0; j < freq; j++) {
            final int position = postings.nextPosition();
            int payloadLength = postings.getPayloadLength();
            if (payloadLength > 0) {
              if (payloadBuffer == null || payloadBuffer.length < payloadLength)
                payloadBuffer = new byte[payloadLength];
              postings.getPayload(payloadBuffer, 0);
              if (payloadProcessor != null) {
                payloadBuffer = payloadProcessor.processPayload(payloadBuffer, 0, payloadLength);
                payloadLength = payloadProcessor.payloadLength();
              }
            }
            posConsumer.addPosition(position, payloadBuffer, 0, payloadLength);
          }
          posConsumer.finish();
        }
      }
    }
    docConsumer.finish();

    return df;
  }

  public boolean getAnyNonBulkMerges() {
    assert matchedCount <= readers.size();
    return matchedCount != readers.size();
  }

  private void mergeNorms() throws IOException {
    // get needed buffer size by finding the largest segment
    int bufferSize = 0;
    for (IndexReader reader : readers) {
      bufferSize = Math.max(bufferSize, reader.maxDoc());
    }
    
    byte[] normBuffer = null;
    IndexOutput output = null;
    boolean success = false;
    try {
      int numFieldInfos = fieldInfos.size();
      for (int i = 0; i < numFieldInfos; i++) {
        FieldInfo fi = fieldInfos.fieldInfo(i);
        if (fi.isIndexed && !fi.omitNorms) {
          if (output == null) { 
            output = directory.createOutput(IndexFileNames.segmentFileName(segment, IndexFileNames.NORMS_EXTENSION));
            output.writeBytes(SegmentNorms.NORMS_HEADER, SegmentNorms.NORMS_HEADER.length);
          }
          if (normBuffer == null) {
            normBuffer = new byte[bufferSize];
          }
          for (IndexReader reader : readers) {
            final int maxDoc = reader.maxDoc();
            reader.norms(fi.name, normBuffer, 0);
            if (!reader.hasDeletions()) {
              //optimized case for segments without deleted docs
              output.writeBytes(normBuffer, maxDoc);
            } else {
              // this segment has deleted docs, so we have to
              // check for every doc if it is deleted or not
              for (int k = 0; k < maxDoc; k++) {
                if (!reader.isDeleted(k)) {
                  output.writeByte(normBuffer[k]);
                }
              }
            }
            checkAbort.work(maxDoc);
          }
        }
      }
      success = true;
    } finally {
      if (success) {
        IOUtils.close(output);
      } else {
        IOUtils.closeWhileHandlingException(output);
      }
    }
  }

  static class CheckAbort {
    private double workCount;
    private MergePolicy.OneMerge merge;
    private Directory dir;
    public CheckAbort(MergePolicy.OneMerge merge, Directory dir) {
      this.merge = merge;
      this.dir = dir;
    }

    /**
     * Records the fact that roughly units amount of work
     * have been done since this method was last called.
     * When adding time-consuming code into SegmentMerger,
     * you should test different values for units to ensure
     * that the time in between calls to merge.checkAborted
     * is up to ~ 1 second.
     */
    public void work(double units) throws MergePolicy.MergeAbortedException {
      workCount += units;
      if (workCount >= 10000.0) {
        merge.checkAborted(dir);
        workCount = 0;
      }
    }
  }
  
}
