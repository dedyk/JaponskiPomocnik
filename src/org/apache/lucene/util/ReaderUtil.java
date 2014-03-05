package org.apache.lucene.util;

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
import java.util.HashSet;
import java.util.List;

import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.FieldInfos;
import org.apache.lucene.index.IndexReader;

/**
 * Common util methods for dealing with {@link IndexReader}s.
 *
 * @lucene.internal
 */
public final class ReaderUtil {

  private ReaderUtil() {} // no instance

  /**
   * Gathers sub-readers from reader into a List.
   * 
   * @param allSubReaders
   * @param reader
   */
  public static void gatherSubReaders(List<IndexReader> allSubReaders, IndexReader reader) {
    IndexReader[] subReaders = reader.getSequentialSubReaders();
    if (subReaders == null) {
      // Add the reader itself, and do not recurse
      allSubReaders.add(reader);
    } else {
      for (int i = 0; i < subReaders.length; i++) {
        gatherSubReaders(allSubReaders, subReaders[i]);
      }
    }
  }

  /** Recursively visits all sub-readers of a reader.  You
   *  should subclass this and override the add method to
   *  gather what you need.
   *
   * @lucene.experimental */
  public static abstract class Gather {
    private final IndexReader topReader;

    public Gather(IndexReader r) {
      topReader = r;
    }

    public int run() throws IOException {
      return run(0, topReader);
    }

    public int run(int docBase) throws IOException {
      return run(docBase, topReader);
    }

    private int run(final int base, final IndexReader reader) throws IOException {
      IndexReader[] subReaders = reader.getSequentialSubReaders();
      if (subReaders == null) {
        // atomic reader
        add(base, reader);
        return base + reader.maxDoc();
      } else {
        // composite reader
        int newBase = base;
        for (int i = 0; i < subReaders.length; i++) {
          newBase = run(newBase, subReaders[i]);
        }
        assert newBase == base + reader.maxDoc();
        return newBase;
      }
    }

    protected abstract void add(int base, IndexReader r) throws IOException;
  }

  /**
   * Returns index of the searcher/reader for document <code>n</code> in the
   * array used to construct this searcher/reader.
   */
  public static int subIndex(int n, int[] docStarts) { // find
    // searcher/reader for doc n:
    int size = docStarts.length;
    int lo = 0; // search starts array
    int hi = size - 1; // for first element less than n, return its index
    while (hi >= lo) {
      int mid = (lo + hi) >>> 1;
      int midValue = docStarts[mid];
      if (n < midValue)
        hi = mid - 1;
      else if (n > midValue)
        lo = mid + 1;
      else { // found a match
        while (mid + 1 < size && docStarts[mid + 1] == midValue) {
          mid++; // scan to last match
        }
        return mid;
      }
    }
    return hi;
  }

  public static Collection<String> getIndexedFields(IndexReader reader) {
    final Collection<String> fields = new HashSet<String>();
    for(FieldInfo fieldInfo : getMergedFieldInfos(reader)) {
      if (fieldInfo.isIndexed) {
        fields.add(fieldInfo.name);
      }
    }
    return fields;
  }

  /** Call this to get the (merged) FieldInfos for a
   *  composite reader */
  public static FieldInfos getMergedFieldInfos(IndexReader reader) {
    final List<IndexReader> subReaders = new ArrayList<IndexReader>();
    ReaderUtil.gatherSubReaders(subReaders, reader);
    final FieldInfos fieldInfos = new FieldInfos();
    for(IndexReader subReader : subReaders) {
      fieldInfos.add(subReader.getFieldInfos());
    }
    return fieldInfos;
  }
}
