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

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.lucene.document.AbstractField;  // for javadocs
import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.util.CommandLineUtil;
import org.apache.lucene.util.StringHelper;

/**
 * Basic tool and API to check the health of an index and
 * write a new segments file that removes reference to
 * problematic segments.
 * 
 * <p>As this tool checks every byte in the index, on a large
 * index it can take quite a long time to run.
 *
 * @lucene.experimental Please make a complete backup of your
 * index before using this to fix your index!
 */
public class CheckIndex {

  private PrintStream infoStream;
  private Directory dir;

  /**
   * Returned from {@link #checkIndex()} detailing the health and status of the index.
   *
   * @lucene.experimental
   **/

  public static class Status {

    /** True if no problems were found with the index. */
    public boolean clean;

    /** True if we were unable to locate and load the segments_N file. */
    public boolean missingSegments;

    /** True if we were unable to open the segments_N file. */
    public boolean cantOpenSegments;

    /** True if we were unable to read the version number from segments_N file. */
    public boolean missingSegmentVersion;

    /** Name of latest segments_N file in the index. */
    public String segmentsFileName;

    /** Number of segments in the index. */
    public int numSegments;

    /** String description of the version of the index. */
    public String segmentFormat;

    /** Empty unless you passed specific segments list to check as optional 3rd argument.
     *  @see CheckIndex#checkIndex(List) */
    public List<String> segmentsChecked = new ArrayList<String>();
  
    /** True if the index was created with a newer version of Lucene than the CheckIndex tool. */
    public boolean toolOutOfDate;

    /** List of {@link SegmentInfoStatus} instances, detailing status of each segment. */
    public List<SegmentInfoStatus> segmentInfos = new ArrayList<SegmentInfoStatus>();
  
    /** Directory index is in. */
    public Directory dir;

    /** 
     * SegmentInfos instance containing only segments that
     * had no problems (this is used with the {@link CheckIndex#fixIndex} 
     * method to repair the index. 
     */
    SegmentInfos newSegments;

    /** How many documents will be lost to bad segments. */
    public int totLoseDocCount;

    /** How many bad segments were found. */
    public int numBadSegments;

    /** True if we checked only specific segments ({@link
     * #checkIndex(List)}) was called with non-null
     * argument). */
    public boolean partial;

    /** The greatest segment name. */
    public int maxSegmentName;

    /** Whether the SegmentInfos.counter is greater than any of the segments' names. */
    public boolean validCounter; 

    /** Holds the userData of the last commit in the index */
    public Map<String, String> userData;

    /** Holds the status of each segment in the index.
     *  See {@link #segmentInfos}.
     *
     * <p><b>WARNING</b>: this API is new and experimental and is
     * subject to suddenly change in the next release.
     */
    public static class SegmentInfoStatus {
      /** Name of the segment. */
      public String name;

      /** Document count (does not take deletions into account). */
      public int docCount;

      /** True if segment is compound file format. */
      public boolean compound;

      /** Number of files referenced by this segment. */
      public int numFiles;

      /** Net size (MB) of the files referenced by this
       *  segment. */
      public double sizeMB;

      /** Doc store offset, if this segment shares the doc
       *  store files (stored fields and term vectors) with
       *  other segments.  This is -1 if it does not share. */
      public int docStoreOffset = -1;
    
      /** String of the shared doc store segment, or null if
       *  this segment does not share the doc store files. */
      public String docStoreSegment;

      /** True if the shared doc store files are compound file
       *  format. */
      public boolean docStoreCompoundFile;

      /** True if this segment has pending deletions. */
      public boolean hasDeletions;

      /** Name of the current deletions file name. */
      public String deletionsFileName;
    
      /** Number of deleted documents. */
      public int numDeleted;

      /** True if we were able to open a SegmentReader on this
       *  segment. */
      public boolean openReaderPassed;

      /** Number of fields in this segment. */
      int numFields;

      /** True if at least one of the fields in this segment
       *  has position data
       *  @see AbstractField#setIndexOptions(org.apache.lucene.index.FieldInfo.IndexOptions) */
      public boolean hasProx;

      /** Map that includes certain
       *  debugging details that IndexWriter records into
       *  each segment it creates */
      public Map<String,String> diagnostics;

      /** Status for testing of field norms (null if field norms could not be tested). */
      public FieldNormStatus fieldNormStatus;

      /** Status for testing of indexed terms (null if indexed terms could not be tested). */
      public TermIndexStatus termIndexStatus;

      /** Status for testing of stored fields (null if stored fields could not be tested). */
      public StoredFieldStatus storedFieldStatus;

      /** Status for testing of term vectors (null if term vectors could not be tested). */
      public TermVectorStatus termVectorStatus;
    }

    /**
     * Status from testing field norms.
     */
    public static final class FieldNormStatus {
      /** Number of fields successfully tested */
      public long totFields = 0L;

      /** Exception thrown during term index test (null on success) */
      public Throwable error = null;
    }

    /**
     * Status from testing term index.
     */
    public static final class TermIndexStatus {
      /** Total term count */
      public long termCount = 0L;

      /** Total frequency across all terms. */
      public long totFreq = 0L;
      
      /** Total number of positions. */
      public long totPos = 0L;

      /** Exception thrown during term index test (null on success) */
      public Throwable error = null;
    }

    /**
     * Status from testing stored fields.
     */
    public static final class StoredFieldStatus {
      
      /** Number of documents tested. */
      public int docCount = 0;
      
      /** Total number of stored fields tested. */
      public long totFields = 0;
      
      /** Exception thrown during stored fields test (null on success) */
      public Throwable error = null;
    }

    /**
     * Status from testing stored fields.
     */
    public static final class TermVectorStatus {
      
      /** Number of documents tested. */
      public int docCount = 0;
      
      /** Total number of term vectors tested. */
      public long totVectors = 0;
      
      /** Exception thrown during term vector test (null on success) */
      public Throwable error = null;
    }
  }

  /** Create a new CheckIndex on the directory. */
  public CheckIndex(Directory dir) {
    this.dir = dir;
    infoStream = null;
  }

  /** Set infoStream where messages should go.  If null, no
   *  messages are printed */
  public void setInfoStream(PrintStream out) {
    infoStream = out;
  }

  private void msg(String msg) {
    if (infoStream != null)
      infoStream.println(msg);
  }

  private static class MySegmentTermDocs extends SegmentTermDocs {

    int delCount;

    MySegmentTermDocs(SegmentReader p) {    
      super(p);
    }

    @Override
    public void seek(Term term) throws IOException {
      super.seek(term);
      delCount = 0;
    }

    @Override
    protected void skippingDoc() throws IOException {
      delCount++;
    }
  }

  /** Returns a {@link Status} instance detailing
   *  the state of the index.
   *
   *  <p>As this method checks every byte in the index, on a large
   *  index it can take quite a long time to run.
   *
   *  <p><b>WARNING</b>: make sure
   *  you only call this when the index is not opened by any
   *  writer. */
  public Status checkIndex() throws IOException {
    return checkIndex(null);
  }

  /** Returns a {@link Status} instance detailing
   *  the state of the index.
   * 
   *  @param onlySegments list of specific segment names to check
   *
   *  <p>As this method checks every byte in the specified
   *  segments, on a large index it can take quite a long
   *  time to run.
   *
   *  <p><b>WARNING</b>: make sure
   *  you only call this when the index is not opened by any
   *  writer. */
  public Status checkIndex(List<String> onlySegments) throws IOException {
    NumberFormat nf = NumberFormat.getInstance();
    SegmentInfos sis = new SegmentInfos();
    Status result = new Status();
    result.dir = dir;
    try {
      sis.read(dir);
    } catch (Throwable t) {
      msg("ERROR: could not read any segments file in directory");
      result.missingSegments = true;
      if (infoStream != null)
        t.printStackTrace(infoStream);
      return result;
    }

    // find the oldest and newest segment versions
    String oldest = Integer.toString(Integer.MAX_VALUE), newest = Integer.toString(Integer.MIN_VALUE);
    String oldSegs = null;
    boolean foundNonNullVersion = false;
    Comparator<String> versionComparator = StringHelper.getVersionComparator();
    for (SegmentInfo si : sis) {
      String version = si.getVersion();
      if (version == null) {
        // pre-3.1 segment
        oldSegs = "pre-3.1";
      } else if (version.equals("2.x")) {
        // an old segment that was 'touched' by 3.1+ code
        oldSegs = "2.x";
      } else {
        foundNonNullVersion = true;
        if (versionComparator.compare(version, oldest) < 0) {
          oldest = version;
        }
        if (versionComparator.compare(version, newest) > 0) {
          newest = version;
        }
      }
    }
    
    final int numSegments = sis.size();
    final String segmentsFileName = sis.getSegmentsFileName();
    IndexInput input = null;
    try {
      input = dir.openInput(segmentsFileName);
    } catch (Throwable t) {
      msg("ERROR: could not open segments file in directory");
      if (infoStream != null)
        t.printStackTrace(infoStream);
      result.cantOpenSegments = true;
      return result;
    }
    int format = 0;
    try {
      format = input.readInt();
    } catch (Throwable t) {
      msg("ERROR: could not read segment file version in directory");
      if (infoStream != null)
        t.printStackTrace(infoStream);
      result.missingSegmentVersion = true;
      return result;
    } finally {
      if (input != null)
        input.close();
    }

    String sFormat = "";
    boolean skip = false;

    if (format == SegmentInfos.FORMAT)
      sFormat = "FORMAT [Lucene Pre-2.1]";
    if (format == SegmentInfos.FORMAT_LOCKLESS)
      sFormat = "FORMAT_LOCKLESS [Lucene 2.1]";
    else if (format == SegmentInfos.FORMAT_SINGLE_NORM_FILE)
      sFormat = "FORMAT_SINGLE_NORM_FILE [Lucene 2.2]";
    else if (format == SegmentInfos.FORMAT_SHARED_DOC_STORE)
      sFormat = "FORMAT_SHARED_DOC_STORE [Lucene 2.3]";
    else {
      if (format == SegmentInfos.FORMAT_CHECKSUM)
        sFormat = "FORMAT_CHECKSUM [Lucene 2.4]";
      else if (format == SegmentInfos.FORMAT_DEL_COUNT)
        sFormat = "FORMAT_DEL_COUNT [Lucene 2.4]";
      else if (format == SegmentInfos.FORMAT_HAS_PROX)
        sFormat = "FORMAT_HAS_PROX [Lucene 2.4]";
      else if (format == SegmentInfos.FORMAT_USER_DATA)
        sFormat = "FORMAT_USER_DATA [Lucene 2.9]";
      else if (format == SegmentInfos.FORMAT_DIAGNOSTICS)
        sFormat = "FORMAT_DIAGNOSTICS [Lucene 2.9]";
      else if (format == SegmentInfos.FORMAT_HAS_VECTORS)
        sFormat = "FORMAT_HAS_VECTORS [Lucene 3.1]";
      else if (format == SegmentInfos.FORMAT_3_1)
        sFormat = "FORMAT_3_1 [Lucene 3.1+]";
      else if (format == SegmentInfos.CURRENT_FORMAT)
        throw new RuntimeException("BUG: You should update this tool!");
      else if (format < SegmentInfos.CURRENT_FORMAT) {
        sFormat = "int=" + format + " [newer version of Lucene than this tool]";
        skip = true;
      } else {
        sFormat = format + " [Lucene 1.3 or prior]";
      }
    }

    result.segmentsFileName = segmentsFileName;
    result.numSegments = numSegments;
    result.segmentFormat = sFormat;
    result.userData = sis.getUserData();
    String userDataString;
    if (sis.getUserData().size() > 0) {
      userDataString = " userData=" + sis.getUserData();
    } else {
      userDataString = "";
    }

    String versionString = null;
    if (oldSegs != null) {
      if (foundNonNullVersion) {
        versionString = "versions=[" + oldSegs + " .. " + newest + "]";
      } else {
        versionString = "version=" + oldSegs;
      }
    } else {
      versionString = oldest.equals(newest) ? ( "version=" + oldest ) : ("versions=[" + oldest + " .. " + newest + "]");
    }
    
    msg("Segments file=" + segmentsFileName + " numSegments=" + numSegments
        + " " + versionString + " format=" + sFormat + userDataString);

    if (onlySegments != null) {
      result.partial = true;
      if (infoStream != null)
        infoStream.print("\nChecking only these segments:");
      for (String s : onlySegments) {
        if (infoStream != null)
          infoStream.print(" " + s);
      }
      result.segmentsChecked.addAll(onlySegments);
      msg(":");
    }

    if (skip) {
      msg("\nERROR: this index appears to be created by a newer version of Lucene than this tool was compiled on; please re-compile this tool on the matching version of Lucene; exiting");
      result.toolOutOfDate = true;
      return result;
    }


    result.newSegments = (SegmentInfos) sis.clone();
    result.newSegments.clear();
    result.maxSegmentName = -1;

    for(int i=0;i<numSegments;i++) {
      final SegmentInfo info = sis.info(i);
      int segmentName = Integer.parseInt(info.name.substring(1), Character.MAX_RADIX);
      if (segmentName > result.maxSegmentName) {
        result.maxSegmentName = segmentName;
      }
      if (onlySegments != null && !onlySegments.contains(info.name))
        continue;
      Status.SegmentInfoStatus segInfoStat = new Status.SegmentInfoStatus();
      result.segmentInfos.add(segInfoStat);
      msg("  " + (1+i) + " of " + numSegments + ": name=" + info.name + " docCount=" + info.docCount);
      segInfoStat.name = info.name;
      segInfoStat.docCount = info.docCount;

      int toLoseDocCount = info.docCount;

      SegmentReader reader = null;

      try {
        msg("    compound=" + info.getUseCompoundFile());
        segInfoStat.compound = info.getUseCompoundFile();
        msg("    hasProx=" + info.getHasProx());
        segInfoStat.hasProx = info.getHasProx();
        msg("    numFiles=" + info.files().size());
        segInfoStat.numFiles = info.files().size();
        segInfoStat.sizeMB = info.sizeInBytes(true)/(1024.*1024.);
        msg("    size (MB)=" + nf.format(segInfoStat.sizeMB));
        Map<String,String> diagnostics = info.getDiagnostics();
        segInfoStat.diagnostics = diagnostics;
        if (diagnostics.size() > 0) {
          msg("    diagnostics = " + diagnostics);
        }

        final int docStoreOffset = info.getDocStoreOffset();
        if (docStoreOffset != -1) {
          msg("    docStoreOffset=" + docStoreOffset);
          segInfoStat.docStoreOffset = docStoreOffset;
          msg("    docStoreSegment=" + info.getDocStoreSegment());
          segInfoStat.docStoreSegment = info.getDocStoreSegment();
          msg("    docStoreIsCompoundFile=" + info.getDocStoreIsCompoundFile());
          segInfoStat.docStoreCompoundFile = info.getDocStoreIsCompoundFile();
        }
        final String delFileName = info.getDelFileName();
        if (delFileName == null){
          msg("    no deletions");
          segInfoStat.hasDeletions = false;
        }
        else{
          msg("    has deletions [delFileName=" + delFileName + "]");
          segInfoStat.hasDeletions = true;
          segInfoStat.deletionsFileName = delFileName;
        }
        if (infoStream != null)
          infoStream.print("    test: open reader.........");
        reader = SegmentReader.get(true, info, IndexReader.DEFAULT_TERMS_INDEX_DIVISOR);

        segInfoStat.openReaderPassed = true;

        final int numDocs = reader.numDocs();
        toLoseDocCount = numDocs;
        if (reader.hasDeletions()) {
          if (reader.deletedDocs.count() != info.getDelCount()) {
            throw new RuntimeException("delete count mismatch: info=" + info.getDelCount() + " vs deletedDocs.count()=" + reader.deletedDocs.count());
          }
          if (reader.deletedDocs.count() > reader.maxDoc()) {
            throw new RuntimeException("too many deleted docs: maxDoc()=" + reader.maxDoc() + " vs deletedDocs.count()=" + reader.deletedDocs.count());
          }
          if (info.docCount - numDocs != info.getDelCount()){
            throw new RuntimeException("delete count mismatch: info=" + info.getDelCount() + " vs reader=" + (info.docCount - numDocs));
          }

          int numLive = 0;    
          for (int j = 0; j < reader.maxDoc(); j++) {
            if (!reader.isDeleted(j)) {
              numLive++;
            }
          }
          if (numLive != numDocs) {
            throw new RuntimeException("liveDocs count mismatch: info=" + numDocs + ", vs bits=" + numLive);
          }
          segInfoStat.numDeleted = info.docCount - numDocs;
          msg("OK [" + (segInfoStat.numDeleted) + " deleted docs]");
        } else {
          if (info.getDelCount() != 0) {
            throw new RuntimeException("delete count mismatch: info=" + info.getDelCount() + " vs reader=" + (info.docCount - numDocs));
          }

          for (int j = 0; j < reader.maxDoc(); j++) {
            if (reader.isDeleted(j)) {
              throw new RuntimeException("liveDocs mismatch: info says no deletions but doc " + j + " is deleted.");
            }
          }
          msg("OK");
        }
        if (reader.maxDoc() != info.docCount)
          throw new RuntimeException("SegmentReader.maxDoc() " + reader.maxDoc() + " != SegmentInfos.docCount " + info.docCount);

        // Test getFieldInfos()
        if (infoStream != null) {
          infoStream.print("    test: fields..............");
        }         
        FieldInfos fieldInfos = reader.getFieldInfos();
        msg("OK [" + fieldInfos.size() + " fields]");
        segInfoStat.numFields = fieldInfos.size();
        
        // Test Field Norms
        segInfoStat.fieldNormStatus = testFieldNorms(fieldInfos, reader);

        // Test the Term Index
        segInfoStat.termIndexStatus = testTermIndex(info, fieldInfos, reader);

        // Test Stored Fields
        segInfoStat.storedFieldStatus = testStoredFields(info, reader, nf);

        // Test Term Vectors
        segInfoStat.termVectorStatus = testTermVectors(info, reader, nf);

        // Rethrow the first exception we encountered
        //  This will cause stats for failed segments to be incremented properly
        if (segInfoStat.fieldNormStatus.error != null) {
          throw new RuntimeException("Field Norm test failed");
        } else if (segInfoStat.termIndexStatus.error != null) {
          throw new RuntimeException("Term Index test failed");
        } else if (segInfoStat.storedFieldStatus.error != null) {
          throw new RuntimeException("Stored Field test failed");
        } else if (segInfoStat.termVectorStatus.error != null) {
          throw new RuntimeException("Term Vector test failed");
        }

        msg("");

      } catch (Throwable t) {
        msg("FAILED");
        String comment;
        comment = "fixIndex() would remove reference to this segment";
        msg("    WARNING: " + comment + "; full exception:");
        if (infoStream != null)
          t.printStackTrace(infoStream);
        msg("");
        result.totLoseDocCount += toLoseDocCount;
        result.numBadSegments++;
        continue;
      } finally {
        if (reader != null)
          reader.close();
      }

      // Keeper
      result.newSegments.add((SegmentInfo) info.clone());
    }

    if (0 == result.numBadSegments) {
      result.clean = true;
    } else
      msg("WARNING: " + result.numBadSegments + " broken segments (containing " + result.totLoseDocCount + " documents) detected");

    if ( ! (result.validCounter = (result.maxSegmentName < sis.counter))) {
      result.clean = false;
      result.newSegments.counter = result.maxSegmentName + 1; 
      msg("ERROR: Next segment name counter " + sis.counter + " is not greater than max segment name " + result.maxSegmentName);
    }
    
    if (result.clean) {
      msg("No problems were detected with this index.\n");
    }

    return result;
  }

  /**
   * Test field norms.
   */
  private Status.FieldNormStatus testFieldNorms(FieldInfos fieldInfos, SegmentReader reader) {
    final Status.FieldNormStatus status = new Status.FieldNormStatus();

    try {
      // Test Field Norms
      if (infoStream != null) {
        infoStream.print("    test: field norms.........");
      }
      final byte[] b = new byte[reader.maxDoc()];
      for (FieldInfo fieldInfo : fieldInfos) {
        if (reader.hasNorms(fieldInfo.name)) {
          reader.norms(fieldInfo.name, b, 0);
          ++status.totFields;
        }
      }

      msg("OK [" + status.totFields + " fields]");
    } catch (Throwable e) {
      msg("ERROR [" + String.valueOf(e.getMessage()) + "]");
      status.error = e;
      if (infoStream != null) {
        e.printStackTrace(infoStream);
      }
    }

    return status;
  }

  /**
   * Test the term index.
   */
  private Status.TermIndexStatus testTermIndex(SegmentInfo info, FieldInfos fieldInfos, SegmentReader reader) {
    final Status.TermIndexStatus status = new Status.TermIndexStatus();

    final IndexSearcher is = new IndexSearcher(reader);

    try {
      if (infoStream != null) {
        infoStream.print("    test: terms, freq, prox...");
      }

      final TermEnum termEnum = reader.terms();
      final TermPositions termPositions = reader.termPositions();

      // Used only to count up # deleted docs for this term
      final MySegmentTermDocs myTermDocs = new MySegmentTermDocs(reader);

      final int maxDoc = reader.maxDoc();
      Term lastTerm = null;
      String lastField = null;
      while (termEnum.next()) {
        status.termCount++;
        final Term term = termEnum.term();

        if (lastTerm != null && term.compareTo(lastTerm) <= 0) {
          throw new RuntimeException("terms out of order: lastTerm=" + lastTerm + " term=" + term);
        }
        lastTerm = term;
        
        if (term.field != lastField) {
          // field change: verify its in fieldinfos, and that its indexed.
          FieldInfo fi = fieldInfos.fieldInfo(term.field);
          if (fi == null) {
            throw new RuntimeException("terms inconsistent with fieldInfos, no fieldInfos for: " + term.field);
          }
          if (!fi.isIndexed) {
            throw new RuntimeException("terms inconsistent with fieldInfos, isIndexed == false for: " + term.field);
          }
          lastField = term.field;
        }
        final int docFreq = termEnum.docFreq();
        if (docFreq <= 0) {
          throw new RuntimeException("docfreq: " + docFreq + " is out of bounds");
        }
        termPositions.seek(term);
        int lastDoc = -1;
        int freq0 = 0;
        status.totFreq += docFreq;
        while (termPositions.next()) {
          freq0++;
          final int doc = termPositions.doc();
          final int freq = termPositions.freq();
          if (doc <= lastDoc)
            throw new RuntimeException("term " + term + ": doc " + doc + " <= lastDoc " + lastDoc);
          if (doc >= maxDoc)
            throw new RuntimeException("term " + term + ": doc " + doc + " >= maxDoc " + maxDoc);

          lastDoc = doc;
          if (freq <= 0)
            throw new RuntimeException("term " + term + ": doc " + doc + ": freq " + freq + " is out of bounds");
            
          int lastPos = -1;
          status.totPos += freq;
          for(int j=0;j<freq;j++) {
            final int pos = termPositions.nextPosition();
            // NOTE: -1 is allowed because of ancient bug
            // (LUCENE-1542) whereby IndexWriter could
            // write pos=-1 when first token's posInc is 0
            // (separately: analyzers should not give
            // posInc=0 to first token):
            if (pos < -1) {
              throw new RuntimeException("term " + term + ": doc " + doc + ": pos " + pos + " is out of bounds");
            }
            if (pos < lastPos) {
              throw new RuntimeException("term " + term + ": doc " + doc + ": pos " + pos + " < lastPos " + lastPos);
            }
            lastPos = pos;
          }
        }

        // Test skipping
        for(int idx=0;idx<7;idx++) {
          final int skipDocID = (int) (((idx+1)*(long) maxDoc)/8);
          termPositions.seek(term);
          if (!termPositions.skipTo(skipDocID)) {
            break;
          } else {

            final int docID = termPositions.doc();
            if (docID < skipDocID) {
              throw new RuntimeException("term " + term + ": skipTo(docID=" + skipDocID + ") returned docID=" + docID);
            }
            final int freq = termPositions.freq();
            if (freq <= 0) {
              throw new RuntimeException("termFreq " + freq + " is out of bounds");
            }
            int lastPosition = -1;
            for(int posUpto=0;posUpto<freq;posUpto++) {
              final int pos = termPositions.nextPosition();
              // NOTE: -1 is allowed because of ancient bug
              // (LUCENE-1542) whereby IndexWriter could
              // write pos=-1 when first token's posInc is 0
              // (separately: analyzers should not give
              // posInc=0 to first token):
              if (pos < -1) {
                throw new RuntimeException("position " + pos + " is out of bounds");
              }
              // TODO: we should assert when all pos == 0 that positions are actually omitted
              if (pos < lastPosition) {
                throw new RuntimeException("position " + pos + " is < lastPosition " + lastPosition);
              }
              lastPosition = pos;
            } 

            if (!termPositions.next()) {
              break;
            }
            final int nextDocID = termPositions.doc();
            if (nextDocID <= docID) {
              throw new RuntimeException("term " + term + ": skipTo(docID=" + skipDocID + "), then .next() returned docID=" + nextDocID + " vs prev docID=" + docID);
            }
          }
        }

        // Now count how many deleted docs occurred in
        // this term:
        final int delCount;
        if (reader.hasDeletions()) {
          myTermDocs.seek(term);
          while(myTermDocs.next()) { }
          delCount = myTermDocs.delCount;
        } else {
          delCount = 0; 
        }

        if (freq0 + delCount != docFreq) {
          throw new RuntimeException("term " + term + " docFreq=" + 
                                     docFreq + " != num docs seen " + freq0 + " + num docs deleted " + delCount);
        }
      }

      // Test search on last term:
      if (lastTerm != null) {
        is.search(new TermQuery(lastTerm), 1);
      }

      try {
        long uniqueTermCountAllFields = reader.getUniqueTermCount();
        if (status.termCount != uniqueTermCountAllFields) {
          throw new RuntimeException("termCount mismatch " + uniqueTermCountAllFields + " vs " + (status.termCount));
        }
      } catch (UnsupportedOperationException ex) {
        // not supported
      }
      
      msg("OK [" + status.termCount + " terms; " + status.totFreq + " terms/docs pairs; " + status.totPos + " tokens]");

    } catch (Throwable e) {
      msg("ERROR [" + String.valueOf(e.getMessage()) + "]");
      status.error = e;
      if (infoStream != null) {
        e.printStackTrace(infoStream);
      }
    }

    return status;
  }
  
  /**
   * Test stored fields for a segment.
   */
  private Status.StoredFieldStatus testStoredFields(SegmentInfo info, SegmentReader reader, NumberFormat format) {
    final Status.StoredFieldStatus status = new Status.StoredFieldStatus();

    try {
      if (infoStream != null) {
        infoStream.print("    test: stored fields.......");
      }

      // Scan stored fields for all documents
      for (int j = 0; j < info.docCount; ++j) {
        Document doc = reader.document(j);
        if (!reader.isDeleted(j)) {
          status.docCount++;
          status.totFields += doc.getFields().size();
        }
      }      

      // Validate docCount
      if (status.docCount != reader.numDocs()) {
        throw new RuntimeException("docCount=" + status.docCount + " but saw " + status.docCount + " undeleted docs");
      }

      msg("OK [" + status.totFields + " total field count; avg " + 
          format.format((((float) status.totFields)/status.docCount)) + " fields per doc]");      
    } catch (Throwable e) {
      msg("ERROR [" + String.valueOf(e.getMessage()) + "]");
      status.error = e;
      if (infoStream != null) {
        e.printStackTrace(infoStream);
      }
    }

    return status;
  }

  /**
   * Test term vectors for a segment.
   */
  private Status.TermVectorStatus testTermVectors(SegmentInfo info, SegmentReader reader, NumberFormat format) {
    final Status.TermVectorStatus status = new Status.TermVectorStatus();
    
    try {
      if (infoStream != null) {
        infoStream.print("    test: term vectors........");
      }

      for (int j = 0; j < info.docCount; ++j) {
        if (!reader.isDeleted(j)) {
          status.docCount++;
          TermFreqVector[] tfv = reader.getTermFreqVectors(j);
          if (tfv != null) {
            status.totVectors += tfv.length;
          }
        }
      }
      
      msg("OK [" + status.totVectors + " total vector count; avg " + 
          format.format((((float) status.totVectors) / status.docCount)) + " term/freq vector fields per doc]");
    } catch (Throwable e) {
      msg("ERROR [" + String.valueOf(e.getMessage()) + "]");
      status.error = e;
      if (infoStream != null) {
        e.printStackTrace(infoStream);
      }
    }
    
    return status;
  }

  /** Repairs the index using previously returned result
   *  from {@link #checkIndex}.  Note that this does not
   *  remove any of the unreferenced files after it's done;
   *  you must separately open an {@link IndexWriter}, which
   *  deletes unreferenced files when it's created.
   *
   * <p><b>WARNING</b>: this writes a
   *  new segments file into the index, effectively removing
   *  all documents in broken segments from the index.
   *  BE CAREFUL.
   *
   * <p><b>WARNING</b>: Make sure you only call this when the
   *  index is not opened  by any writer. */
  public void fixIndex(Status result) throws IOException {
    if (result.partial)
      throw new IllegalArgumentException("can only fix an index that was fully checked (this status checked a subset of segments)");
    result.newSegments.changed();
    result.newSegments.commit(result.dir);
  }

  private static boolean assertsOn;

  private static boolean testAsserts() {
    assertsOn = true;
    return true;
  }

  private static boolean assertsOn() {
    assert testAsserts();
    return assertsOn;
  }

  /** Command-line interface to check and fix an index.

    <p>
    Run it like this:
    <pre>
    java -ea:org.apache.lucene... org.apache.lucene.index.CheckIndex pathToIndex [-fix] [-segment X] [-segment Y]
    </pre>
    <ul>
    <li><code>-fix</code>: actually write a new segments_N file, removing any problematic segments

    <li><code>-segment X</code>: only check the specified
    segment(s).  This can be specified multiple times,
    to check more than one segment, eg <code>-segment _2
    -segment _a</code>.  You can't use this with the -fix
    option.
    </ul>

    <p><b>WARNING</b>: <code>-fix</code> should only be used on an emergency basis as it will cause
                       documents (perhaps many) to be permanently removed from the index.  Always make
                       a backup copy of your index before running this!  Do not run this tool on an index
                       that is actively being written to.  You have been warned!

    <p>                Run without -fix, this tool will open the index, report version information
                       and report any exceptions it hits and what action it would take if -fix were
                       specified.  With -fix, this tool will remove any segments that have issues and
                       write a new segments_N file.  This means all documents contained in the affected
                       segments will be removed.

    <p>
                       This tool exits with exit code 1 if the index cannot be opened or has any
                       corruption, else 0.
   */
  public static void main(String[] args) throws IOException, InterruptedException {

    boolean doFix = false;
    List<String> onlySegments = new ArrayList<String>();
    String indexPath = null;
    String dirImpl = null;
    int i = 0;
    while(i < args.length) {
      String arg = args[i];
      if ("-fix".equals(arg)) {
        doFix = true;
        i++;
      } else if (args[i].equals("-segment")) {
        if (i == args.length-1) {
          System.out.println("ERROR: missing name for -segment option");
          System.exit(1);
        }
        i++;
        onlySegments.add(args[i]);
      } else if ("-dir-impl".equals(arg)) {
        if (i == args.length - 1) {
          System.out.println("ERROR: missing value for -dir-impl option");
          System.exit(1);
        }
        i++;
        dirImpl = args[i];
      } else {
        if (indexPath != null) {
          System.out.println("ERROR: unexpected extra argument '" + args[i] + "'");
          System.exit(1);
        }
        indexPath = args[i];
      }
      i++;
    }

    if (indexPath == null) {
      System.out.println("\nERROR: index path not specified");
      System.out.println("\nUsage: java org.apache.lucene.index.CheckIndex pathToIndex [-fix] [-segment X] [-segment Y] [-dir-impl X]\n" +
                         "\n" +
                         "  -fix: actually write a new segments_N file, removing any problematic segments\n" +
                         "  -segment X: only check the specified segments.  This can be specified multiple\n" + 
                         "              times, to check more than one segment, eg '-segment _2 -segment _a'.\n" +
                         "              You can't use this with the -fix option\n" +
                         "  -dir-impl X: use a specific " + FSDirectory.class.getSimpleName() + " implementation. " +
                         		"If no package is specified the " + FSDirectory.class.getPackage().getName() + " package will be used.\n" +
                         "**WARNING**: -fix should only be used on an emergency basis as it will cause\n" +
                         "documents (perhaps many) to be permanently removed from the index.  Always make\n" +
                         "a backup copy of your index before running this!  Do not run this tool on an index\n" +
                         "that is actively being written to.  You have been warned!\n" +
                         "\n" +
                         "Run without -fix, this tool will open the index, report version information\n" +
                         "and report any exceptions it hits and what action it would take if -fix were\n" +
                         "specified.  With -fix, this tool will remove any segments that have issues and\n" + 
                         "write a new segments_N file.  This means all documents contained in the affected\n" +
                         "segments will be removed.\n" +
                         "\n" +
                         "This tool exits with exit code 1 if the index cannot be opened or has any\n" +
                         "corruption, else 0.\n");
      System.exit(1);
    }

    if (!assertsOn())
      System.out.println("\nNOTE: testing will be more thorough if you run java with '-ea:org.apache.lucene...', so assertions are enabled");

    if (onlySegments.size() == 0)
      onlySegments = null;
    else if (doFix) {
      System.out.println("ERROR: cannot specify both -fix and -segment");
      System.exit(1);
    }

    System.out.println("\nOpening index @ " + indexPath + "\n");
    Directory dir = null;
    try {
      if (dirImpl == null) {
        dir = FSDirectory.open(new File(indexPath));
      } else {
        dir = CommandLineUtil.newFSDirectory(dirImpl, new File(indexPath));
      }
    } catch (Throwable t) {
      System.out.println("ERROR: could not open directory \"" + indexPath + "\"; exiting");
      t.printStackTrace(System.out);
      System.exit(1);
    }

    CheckIndex checker = new CheckIndex(dir);
    checker.setInfoStream(System.out);

    Status result = checker.checkIndex(onlySegments);
    if (result.missingSegments) {
      System.exit(1);
    }

    if (!result.clean) {
      if (!doFix) {
        System.out.println("WARNING: would write new segments file, and " + result.totLoseDocCount + " documents would be lost, if -fix were specified\n");
      } else {
        System.out.println("WARNING: " + result.totLoseDocCount + " documents will be lost\n");
        System.out.println("NOTE: will write new segments file in 5 seconds; this will remove " + result.totLoseDocCount + " docs from the index. THIS IS YOUR LAST CHANCE TO CTRL+C!");
        for(int s=0;s<5;s++) {
          Thread.sleep(1000);
          System.out.println("  " + (5-s) + "...");
        }
        System.out.println("Writing...");
        checker.fixIndex(result);
        System.out.println("OK");
        System.out.println("Wrote new segments file \"" + result.newSegments.getSegmentsFileName() + "\"");
      }
    }
    System.out.println("");

    final int exitCode;
    if (result.clean == true)
      exitCode = 0;
    else
      exitCode = 1;
    System.exit(exitCode);
  }
}
