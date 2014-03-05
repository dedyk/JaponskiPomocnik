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
import java.util.Arrays;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

import java.util.concurrent.atomic.AtomicInteger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.FieldSelector;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.Similarity;
import org.apache.lucene.store.BufferedIndexInput;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.util.BitVector;
import org.apache.lucene.util.CloseableThreadLocal;
import org.apache.lucene.util.StringHelper;

/**
 * IndexReader implementation over a single segment. 
 * <p>
 * Instances pointing to the same segment (but with different deletes, etc)
 * may share the same core data.
 * @lucene.experimental
 */
public class SegmentReader extends IndexReader implements Cloneable {
  @Deprecated protected boolean readOnly;

  private SegmentInfo si;
  private int readBufferSize;

  CloseableThreadLocal<FieldsReader> fieldsReaderLocal = new FieldsReaderLocal();
  CloseableThreadLocal<TermVectorsReader> termVectorsLocal = new CloseableThreadLocal<TermVectorsReader>();

  BitVector deletedDocs = null;
  AtomicInteger deletedDocsRef = null;
  private boolean deletedDocsDirty = false;
  private boolean normsDirty = false;

  // TODO: we should move this tracking into SegmentInfo;
  // this way SegmentInfo.toString shows pending deletes
  private int pendingDeleteCount;

  private boolean rollbackHasChanges = false;
  private boolean rollbackDeletedDocsDirty = false;
  private boolean rollbackNormsDirty = false;
  private SegmentInfo rollbackSegmentInfo;
  private int rollbackPendingDeleteCount;

  // optionally used for the .nrm file shared by multiple norms
  IndexInput singleNormStream;
  AtomicInteger singleNormRef;

  SegmentCoreReaders core;

  /**
   * Sets the initial value 
   */
  private class FieldsReaderLocal extends CloseableThreadLocal<FieldsReader> {
    @Override
    protected FieldsReader initialValue() {
      return (FieldsReader) core.getFieldsReaderOrig().clone();
    }
  }
  
  Map<String,SegmentNorms> norms = new HashMap<String,SegmentNorms>();
  
  /**
   * @throws CorruptIndexException if the index is corrupt
   * @throws IOException if there is a low-level IO error
   */
  public static SegmentReader get(boolean readOnly, SegmentInfo si, int termInfosIndexDivisor) throws CorruptIndexException, IOException {
    return get(readOnly, si.dir, si, BufferedIndexInput.BUFFER_SIZE, true, termInfosIndexDivisor);
  }

  /**
   * @throws CorruptIndexException if the index is corrupt
   * @throws IOException if there is a low-level IO error
   */
  public static SegmentReader get(boolean readOnly,
                                  Directory dir,
                                  SegmentInfo si,
                                  int readBufferSize,
                                  boolean doOpenStores,
                                  int termInfosIndexDivisor)
    throws CorruptIndexException, IOException {
    SegmentReader instance = readOnly ? new ReadOnlySegmentReader() : new SegmentReader();
    instance.readOnly = readOnly;
    instance.si = si;
    instance.readBufferSize = readBufferSize;

    boolean success = false;

    try {
      instance.core = new SegmentCoreReaders(instance, dir, si, readBufferSize, termInfosIndexDivisor);
      if (doOpenStores) {
        instance.core.openDocStores(si);
      }
      instance.loadDeletedDocs();
      instance.openNorms(instance.core.cfsDir, readBufferSize);
      success = true;
    } finally {

      // With lock-less commits, it's entirely possible (and
      // fine) to hit a FileNotFound exception above.  In
      // this case, we want to explicitly close any subset
      // of things that were opened so that we don't have to
      // wait for a GC to do so.
      if (!success) {
        instance.doClose();
      }
    }
    return instance;
  }

  void openDocStores() throws IOException {
    core.openDocStores(si);
  }

  private boolean checkDeletedCounts() throws IOException {
    final int recomputedCount = deletedDocs.getRecomputedCount();
     
    assert deletedDocs.count() == recomputedCount : "deleted count=" + deletedDocs.count() + " vs recomputed count=" + recomputedCount;

    assert si.getDelCount() == recomputedCount : 
    "delete count mismatch: info=" + si.getDelCount() + " vs BitVector=" + recomputedCount;

    // Verify # deletes does not exceed maxDoc for this
    // segment:
    assert si.getDelCount() <= maxDoc() : 
    "delete count mismatch: " + recomputedCount + ") exceeds max doc (" + maxDoc() + ") for segment " + si.name;

    return true;
  }

  private void loadDeletedDocs() throws IOException {
    // NOTE: the bitvector is stored using the regular directory, not cfs
    if (hasDeletions(si)) {
      deletedDocs = new BitVector(directory(), si.getDelFileName());
      deletedDocsRef = new AtomicInteger(1);
      assert checkDeletedCounts();
      if (deletedDocs.size() != si.docCount) {
        throw new CorruptIndexException("document count mismatch: deleted docs count " + deletedDocs.size() + " vs segment doc count " + si.docCount + " segment=" + si.name);
      }
    } else
      assert si.getDelCount() == 0;
  }
  
  /**
   * Clones the norm bytes.  May be overridden by subclasses.  New and experimental.
   * @param bytes Byte array to clone
   * @return New BitVector
   * @deprecated
   */
  @Deprecated
  protected byte[] cloneNormBytes(byte[] bytes) {
    byte[] cloneBytes = new byte[bytes.length];
    System.arraycopy(bytes, 0, cloneBytes, 0, bytes.length);
    return cloneBytes;
  }
  
  /**
   * Clones the deleteDocs BitVector.  May be overridden by subclasses. New and experimental.
   * @param bv BitVector to clone
   * @return New BitVector
   * @deprecated
   */
  @Deprecated
  protected BitVector cloneDeletedDocs(BitVector bv) {
    ensureOpen();
    return (BitVector)bv.clone();
  }

  @Override
  public final synchronized Object clone() {
    try {
      return clone(readOnly); // Preserve current readOnly
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  /** {@inheritDoc} */
  @Override @Deprecated
  public final synchronized IndexReader clone(boolean openReadOnly) throws CorruptIndexException, IOException {
    return reopenSegment(si, true, openReadOnly);
  }

  @Override
  protected synchronized IndexReader doOpenIfChanged()
    throws CorruptIndexException, IOException {
    return reopenSegment(si, false, readOnly);
  }

  /** {@inheritDoc} */
  @Override @Deprecated
  protected synchronized IndexReader doOpenIfChanged(boolean openReadOnly)
    throws CorruptIndexException, IOException {
    return reopenSegment(si, false, openReadOnly);
  }

  synchronized SegmentReader reopenSegment(SegmentInfo si, boolean doClone, boolean openReadOnly) throws CorruptIndexException, IOException {
    ensureOpen();
    boolean deletionsUpToDate = (this.si.hasDeletions() == si.hasDeletions()) 
                                  && (!si.hasDeletions() || this.si.getDelFileName().equals(si.getDelFileName()));
    boolean normsUpToDate = true;
    
    boolean[] fieldNormsChanged = new boolean[core.fieldInfos.size()];
    final int fieldCount = core.fieldInfos.size();
    for (int i = 0; i < fieldCount; i++) {
      if (!this.si.getNormFileName(i).equals(si.getNormFileName(i))) {
        normsUpToDate = false;
        fieldNormsChanged[i] = true;
      }
    }

    // if we're cloning we need to run through the reopenSegment logic
    // also if both old and new readers aren't readonly, we clone to avoid sharing modifications
    if (normsUpToDate && deletionsUpToDate && !doClone && openReadOnly && readOnly) {
      return null;
    }    

    // When cloning, the incoming SegmentInfos should not
    // have any changes in it:
    assert !doClone || (normsUpToDate && deletionsUpToDate);

    // clone reader
    SegmentReader clone = openReadOnly ? new ReadOnlySegmentReader() : new SegmentReader();

    boolean success = false;
    try {
      core.incRef();
      clone.core = core;
      clone.readOnly = openReadOnly;
      clone.si = si;
      clone.readBufferSize = readBufferSize;
      clone.pendingDeleteCount = pendingDeleteCount;

      if (!openReadOnly && hasChanges) {
        // My pending changes transfer to the new reader
        clone.deletedDocsDirty = deletedDocsDirty;
        clone.normsDirty = normsDirty;
        clone.hasChanges = hasChanges;
        hasChanges = false;
      }
      
      if (doClone) {
        if (deletedDocs != null) {
          deletedDocsRef.incrementAndGet();
          clone.deletedDocs = deletedDocs;
          clone.deletedDocsRef = deletedDocsRef;
        }
      } else {
        if (!deletionsUpToDate) {
          // load deleted docs
          assert clone.deletedDocs == null;
          clone.loadDeletedDocs();
        } else if (deletedDocs != null) {
          deletedDocsRef.incrementAndGet();
          clone.deletedDocs = deletedDocs;
          clone.deletedDocsRef = deletedDocsRef;
        }
      }

      clone.norms = new HashMap<String,SegmentNorms>();

      // Clone norms
      for (int i = 0; i < fieldNormsChanged.length; i++) {

        // Clone unchanged norms to the cloned reader
        if (doClone || !fieldNormsChanged[i]) {
          final String curField = core.fieldInfos.fieldInfo(i).name;
          SegmentNorms norm = this.norms.get(curField);
          if (norm != null)
            clone.norms.put(curField, (SegmentNorms) norm.clone());
        }
      }

      // If we are not cloning, then this will open anew
      // any norms that have changed:
      clone.openNorms(si.getUseCompoundFile() ? core.getCFSReader() : directory(), readBufferSize);

      success = true;
    } finally {
      if (!success) {
        // An exception occurred during reopen, we have to decRef the norms
        // that we incRef'ed already and close singleNormsStream and FieldsReader
        clone.decRef();
      }
    }
    
    return clone;
  }

  /** {@inheritDoc} */
  @Override @Deprecated
  protected void doCommit(Map<String,String> commitUserData) throws IOException {
    if (hasChanges) {
      startCommit();
      boolean success = false;
      try {
        commitChanges(commitUserData);
        success = true;
      } finally {
        if (!success) {
          rollbackCommit();
        }
      }
    }
  }

  private synchronized void commitChanges(Map<String,String> commitUserData) throws IOException {
    if (deletedDocsDirty) {               // re-write deleted
      si.advanceDelGen();

      assert deletedDocs.size() == si.docCount;

      // We can write directly to the actual name (vs to a
      // .tmp & renaming it) because the file is not live
      // until segments file is written:
      final String delFileName = si.getDelFileName();
      boolean success = false;
      try {
        deletedDocs.write(directory(), delFileName);
        success = true;
      } finally {
        if (!success) {
          try {
            directory().deleteFile(delFileName);
          } catch (Throwable t) {
            // suppress this so we keep throwing the
            // original exception
          }
        }
      }

      si.setDelCount(si.getDelCount()+pendingDeleteCount);
      pendingDeleteCount = 0;
      assert deletedDocs.count() == si.getDelCount(): "delete count mismatch during commit: info=" + si.getDelCount() + " vs BitVector=" + deletedDocs.count();
    } else {
      assert pendingDeleteCount == 0;
    }

    if (normsDirty) {               // re-write norms
      si.setNumFields(core.fieldInfos.size());
      for (final SegmentNorms norm : norms.values()) {
        if (norm.dirty) {
          norm.reWrite(si);
        }
      }
    }
    deletedDocsDirty = false;
    normsDirty = false;
    hasChanges = false;
  }

  FieldsReader getFieldsReader() {
    return fieldsReaderLocal.get();
  }

  @Override
  protected void doClose() throws IOException {
    termVectorsLocal.close();
    fieldsReaderLocal.close();
    
    if (deletedDocs != null) {
      deletedDocsRef.decrementAndGet();
      // null so if an app hangs on to us we still free most ram
      deletedDocs = null;
    }

    for (final SegmentNorms norm : norms.values()) {
      norm.decRef();
    }
    if (core != null) {
      core.decRef();
    }
  }

  static boolean hasDeletions(SegmentInfo si) throws IOException {
    // Don't call ensureOpen() here (it could affect performance)
    return si.hasDeletions();
  }

  @Override
  public boolean hasDeletions() {
    // Don't call ensureOpen() here (it could affect performance)
    return deletedDocs != null;
  }

  static boolean usesCompoundFile(SegmentInfo si) throws IOException {
    return si.getUseCompoundFile();
  }

  static boolean hasSeparateNorms(SegmentInfo si) throws IOException {
    return si.hasSeparateNorms();
  }

  /** {@inheritDoc} */
  @Override @Deprecated
  protected void doDelete(int docNum) {
    if (deletedDocs == null) {
      deletedDocs = new BitVector(maxDoc());
      deletedDocsRef = new AtomicInteger(1);
    }
    // there is more than 1 SegmentReader with a reference to this
    // deletedDocs BitVector so decRef the current deletedDocsRef,
    // clone the BitVector, create a new deletedDocsRef
    if (deletedDocsRef.get() > 1) {
      AtomicInteger oldRef = deletedDocsRef;
      deletedDocs = cloneDeletedDocs(deletedDocs);
      deletedDocsRef = new AtomicInteger(1);
      oldRef.decrementAndGet();
    }
    deletedDocsDirty = true;
    if (!deletedDocs.getAndSet(docNum)) {
      pendingDeleteCount++;
    }
  }

  /** {@inheritDoc} */
  @Override @Deprecated
  protected void doUndeleteAll() {
    deletedDocsDirty = false;
    if (deletedDocs != null) {
      assert deletedDocsRef != null;
      deletedDocsRef.decrementAndGet();
      deletedDocs = null;
      deletedDocsRef = null;
      pendingDeleteCount = 0;
      si.clearDelGen();
      si.setDelCount(0);
    } else {
      assert deletedDocsRef == null;
      assert pendingDeleteCount == 0;
    }
  }

  List<String> files() throws IOException {
    return new ArrayList<String>(si.files());
  }

  @Override
  public TermEnum terms() {
    ensureOpen();
    return core.getTermsReader().terms();
  }

  @Override
  public TermEnum terms(Term t) throws IOException {
    ensureOpen();
    return core.getTermsReader().terms(t);
  }

  @Override
  public FieldInfos getFieldInfos() {
    return core.fieldInfos;
  }

  // TODO: remove in 4.0
  FieldInfos fieldInfos() {
    return core.fieldInfos;
  }

  @Override
  public Document document(int n, FieldSelector fieldSelector) throws CorruptIndexException, IOException {
    ensureOpen();
    if (n < 0 || n >= maxDoc()) {       
      throw new IllegalArgumentException("docID must be >= 0 and < maxDoc=" + maxDoc() + " (got docID=" + n + ")");
    }
    return getFieldsReader().doc(n, fieldSelector);
  }

  @Override
  public synchronized boolean isDeleted(int n) {
    return (deletedDocs != null && deletedDocs.get(n));
  }

  @Override
  public TermDocs termDocs(Term term) throws IOException {
    if (term == null) {
      return new AllTermDocs(this);
    } else {
      return super.termDocs(term);
    }
  }

  /** Expert: returns an enumeration of the documents that contain
   *  <code>term</code>, including deleted documents (which
   *  are normally filtered out).
   * 
   * @lucene.experimental
   */
  public TermDocs rawTermDocs(Term term) throws IOException {
    if (term == null) {
      throw new IllegalArgumentException("term must not be null");
    }
    TermDocs td = new SegmentTermDocs(this, true);
    td.seek(term);
    return td;
  }

  @Override
  public TermDocs termDocs() throws IOException {
    ensureOpen();
    return new SegmentTermDocs(this);
  }

  @Override
  public TermPositions termPositions() throws IOException {
    ensureOpen();
    return new SegmentTermPositions(this);
  }

  @Override
  public int docFreq(Term t) throws IOException {
    ensureOpen();
    TermInfo ti = core.getTermsReader().get(t);
    if (ti != null)
      return ti.docFreq;
    else
      return 0;
  }

  @Override
  public int numDocs() {
    // Don't call ensureOpen() here (it could affect performance)
    int n = maxDoc();
    if (deletedDocs != null)
      n -= deletedDocs.count();
    return n;
  }

  @Override
  public int maxDoc() {
    // Don't call ensureOpen() here (it could affect performance)
    return si.docCount;
  }

  @Override
  public boolean hasNorms(String field) {
    ensureOpen();
    return norms.containsKey(field);
  }

  @Override
  public byte[] norms(String field) throws IOException {
    ensureOpen();
    final SegmentNorms norm = norms.get(field);
    if (norm == null) {
      // not indexed, or norms not stored
      return null;  
    }
    return norm.bytes();
  }

  /** {@inheritDoc} */
  @Override @Deprecated
  protected void doSetNorm(int doc, String field, byte value)
          throws IOException {
    SegmentNorms norm = norms.get(field);
    if (norm == null) {
      // field does not store norms
      throw new IllegalStateException("Cannot setNorm for field " + field + ": norms were omitted");
    }

    normsDirty = true;
    norm.copyOnWrite()[doc] = value;                    // set the value
  }

  /** Read norms into a pre-allocated array. */
  @Override
  public synchronized void norms(String field, byte[] bytes, int offset)
    throws IOException {

    ensureOpen();
    SegmentNorms norm = norms.get(field);
    if (norm == null) {
      Arrays.fill(bytes, offset, bytes.length, Similarity.getDefault().encodeNormValue(1.0f));
      return;
    }
  
    norm.bytes(bytes, offset, maxDoc());
  }

  // For testing
  /** @lucene.internal */
  int getPostingsSkipInterval() {
    return core.getTermsReader().getSkipInterval();
  }

  private void openNorms(Directory cfsDir, int readBufferSize) throws IOException {
    boolean normsInitiallyEmpty = norms.isEmpty(); // only used for assert
    long nextNormSeek = SegmentNorms.NORMS_HEADER.length; //skip header (header unused for now)
    int maxDoc = maxDoc();
    for (int i = 0; i < core.fieldInfos.size(); i++) {
      FieldInfo fi = core.fieldInfos.fieldInfo(i);
      if (norms.containsKey(fi.name)) {
        // in case this SegmentReader is being re-opened, we might be able to
        // reuse some norm instances and skip loading them here
        continue;
      }
      if (fi.isIndexed && !fi.omitNorms) {
        Directory d = directory();
        String fileName = si.getNormFileName(fi.number);
        if (!si.hasSeparateNorms(fi.number)) {
          d = cfsDir;
        }
        
        // singleNormFile means multiple norms share this file
        boolean singleNormFile = IndexFileNames.matchesExtension(fileName, IndexFileNames.NORMS_EXTENSION);
        IndexInput normInput = null;
        long normSeek;

        if (singleNormFile) {
          normSeek = nextNormSeek;
          if (singleNormStream == null) {
            singleNormStream = d.openInput(fileName, readBufferSize);
            singleNormRef = new AtomicInteger(1);
          } else {
            singleNormRef.incrementAndGet();
          }
          // All norms in the .nrm file can share a single IndexInput since
          // they are only used in a synchronized context.
          // If this were to change in the future, a clone could be done here.
          normInput = singleNormStream;
        } else {
          normInput = d.openInput(fileName);
          // if the segment was created in 3.2 or after, we wrote the header for sure,
          // and don't need to do the sketchy file size check. otherwise, we check 
          // if the size is exactly equal to maxDoc to detect a headerless file.
          // NOTE: remove this check in Lucene 5.0!
          String version = si.getVersion();
          final boolean isUnversioned = 
            (version == null || StringHelper.getVersionComparator().compare(version, "3.2") < 0)
            && normInput.length() == maxDoc();
          if (isUnversioned) {
            normSeek = 0;
          } else {
            normSeek = SegmentNorms.NORMS_HEADER.length;
          }
        }

        norms.put(fi.name, new SegmentNorms(normInput, fi.number, normSeek, this));
        nextNormSeek += maxDoc; // increment also if some norms are separate
      }
    }
    assert singleNormStream == null || !normsInitiallyEmpty || nextNormSeek == singleNormStream.length();
  }

  boolean termsIndexLoaded() {
    return core.termsIndexIsLoaded();
  }

  // NOTE: only called from IndexWriter when a near
  // real-time reader is opened, or applyDeletes is run,
  // sharing a segment that's still being merged.  This
  // method is not thread safe, and relies on the
  // synchronization in IndexWriter
  void loadTermsIndex(int termsIndexDivisor) throws IOException {
    core.loadTermsIndex(si, termsIndexDivisor);
  }

  // for testing only
  boolean normsClosed() {
    if (singleNormStream != null) {
      return false;
    }
    for (final SegmentNorms norm : norms.values()) {
      if (norm.refCount > 0) {
        return false;
      }
    }
    return true;
  }

  // for testing only
  boolean normsClosed(String field) {
    return norms.get(field).refCount == 0;
  }

  /**
   * Create a clone from the initial TermVectorsReader and store it in the ThreadLocal.
   * @return TermVectorsReader
   */
  TermVectorsReader getTermVectorsReader() {
    TermVectorsReader tvReader = termVectorsLocal.get();
    if (tvReader == null) {
      TermVectorsReader orig = core.getTermVectorsReaderOrig();
      if (orig == null) {
        return null;
      } else {
        try {
          tvReader = (TermVectorsReader) orig.clone();
        } catch (CloneNotSupportedException cnse) {
          return null;
        }
      }
      termVectorsLocal.set(tvReader);
    }
    return tvReader;
  }

  TermVectorsReader getTermVectorsReaderOrig() {
    return core.getTermVectorsReaderOrig();
  }
  
  /** Return a term frequency vector for the specified document and field. The
   *  vector returned contains term numbers and frequencies for all terms in
   *  the specified field of this document, if the field had storeTermVector
   *  flag set.  If the flag was not set, the method returns null.
   * @throws IOException
   */
  @Override
  public TermFreqVector getTermFreqVector(int docNumber, String field) throws IOException {
    // Check if this field is invalid or has no stored term vector
    ensureOpen();
    FieldInfo fi = core.fieldInfos.fieldInfo(field);
    if (fi == null || !fi.storeTermVector) 
      return null;
    
    TermVectorsReader termVectorsReader = getTermVectorsReader();
    if (termVectorsReader == null)
      return null;
    
    return termVectorsReader.get(docNumber, field);
  }


  @Override
  public void getTermFreqVector(int docNumber, String field, TermVectorMapper mapper) throws IOException {
    ensureOpen();
    FieldInfo fi = core.fieldInfos.fieldInfo(field);
    if (fi == null || !fi.storeTermVector)
      return;

    TermVectorsReader termVectorsReader = getTermVectorsReader();
    if (termVectorsReader == null) {
      return;
    }


    termVectorsReader.get(docNumber, field, mapper);
  }


  @Override
  public void getTermFreqVector(int docNumber, TermVectorMapper mapper) throws IOException {
    ensureOpen();

    TermVectorsReader termVectorsReader = getTermVectorsReader();
    if (termVectorsReader == null)
      return;

    termVectorsReader.get(docNumber, mapper);
  }

  /** Return an array of term frequency vectors for the specified document.
   *  The array contains a vector for each vectorized field in the document.
   *  Each vector vector contains term numbers and frequencies for all terms
   *  in a given vectorized field.
   *  If no such fields existed, the method returns null.
   * @throws IOException
   */
  @Override
  public TermFreqVector[] getTermFreqVectors(int docNumber) throws IOException {
    ensureOpen();
    
    TermVectorsReader termVectorsReader = getTermVectorsReader();
    if (termVectorsReader == null)
      return null;
    
    return termVectorsReader.get(docNumber);
  }
  
  /** {@inheritDoc} */
  @Override
  public String toString() {
    final StringBuilder buffer = new StringBuilder();
    if (hasChanges) {
      buffer.append('*');
    }
    buffer.append(si.toString(core.dir, pendingDeleteCount));
    return buffer.toString();
  }

  /**
   * Return the name of the segment this reader is reading.
   */
  public String getSegmentName() {
    return core.segment;
  }
  
  /**
   * Return the SegmentInfo of the segment this reader is reading.
   */
  SegmentInfo getSegmentInfo() {
    return si;
  }

  void setSegmentInfo(SegmentInfo info) {
    si = info;
  }

  void startCommit() {
    rollbackSegmentInfo = (SegmentInfo) si.clone();
    rollbackHasChanges = hasChanges;
    rollbackDeletedDocsDirty = deletedDocsDirty;
    rollbackNormsDirty = normsDirty;
    rollbackPendingDeleteCount = pendingDeleteCount;
    for (SegmentNorms norm : norms.values()) {
      norm.rollbackDirty = norm.dirty;
    }
  }

  void rollbackCommit() {
    si.reset(rollbackSegmentInfo);
    hasChanges = rollbackHasChanges;
    deletedDocsDirty = rollbackDeletedDocsDirty;
    normsDirty = rollbackNormsDirty;
    pendingDeleteCount = rollbackPendingDeleteCount;
    for (SegmentNorms norm : norms.values()) {
      norm.dirty = norm.rollbackDirty;
    }
  }

  /** Returns the directory this index resides in. */
  @Override
  public Directory directory() {
    // Don't ensureOpen here -- in certain cases, when a
    // cloned/reopened reader needs to commit, it may call
    // this method on the closed original reader
    return core.dir;
  }

  // This is necessary so that cloned SegmentReaders (which
  // share the underlying postings data) will map to the
  // same entry in the FieldCache.  See LUCENE-1579.
  @Override
  public final Object getCoreCacheKey() {
    return core.freqStream;
  }

  @Override
  public Object getDeletesCacheKey() {
    return deletedDocs;
  }

  @Override
  public long getUniqueTermCount() {
    return core.getTermsReader().size();
  }

  /**
   * Lotsa tests did hacks like:<br/>
   * SegmentReader reader = (SegmentReader) IndexReader.open(dir);<br/>
   * They broke. This method serves as a hack to keep hacks working
   * We do it with R/W access for the tests (BW compatibility)
   * @deprecated Remove this when tests are fixed!
   */
  @Deprecated
  static SegmentReader getOnlySegmentReader(Directory dir) throws IOException {
    return getOnlySegmentReader(IndexReader.open(dir,false));
  }

  static SegmentReader getOnlySegmentReader(IndexReader reader) {
    if (reader instanceof SegmentReader)
      return (SegmentReader) reader;

    if (reader instanceof DirectoryReader) {
      IndexReader[] subReaders = reader.getSequentialSubReaders();
      if (subReaders.length != 1)
        throw new IllegalArgumentException(reader + " has " + subReaders.length + " segments instead of exactly one");

      return (SegmentReader) subReaders[0];
    }

    throw new IllegalArgumentException(reader + " is not a SegmentReader or a single-segment DirectoryReader");
  }

  @Override
  public int getTermInfosIndexDivisor() {
    return core.termsIndexDivisor;
  }
  
  /**
   * Called when the shared core for this SegmentReader
   * is closed.
   * <p>
   * This listener is called only once all SegmentReaders 
   * sharing the same core are closed.  At this point it 
   * is safe for apps to evict this reader from any caches 
   * keyed on {@link #getCoreCacheKey}.  This is the same 
   * interface that {@link FieldCache} uses, internally, 
   * to evict entries.</p>
   * 
   * @lucene.experimental
   */
  public static interface CoreClosedListener {
    public void onClose(SegmentReader owner);
  }
  
  /** Expert: adds a CoreClosedListener to this reader's shared core */
  public void addCoreClosedListener(CoreClosedListener listener) {
    ensureOpen();
    core.addCoreClosedListener(listener);
  }
  
  /** Expert: removes a CoreClosedListener from this reader's shared core */
  public void removeCoreClosedListener(CoreClosedListener listener) {
    ensureOpen();
    core.removeCoreClosedListener(listener);
  }
}
