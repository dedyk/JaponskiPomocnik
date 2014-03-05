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

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implements a combination of {@link java.util.WeakHashMap} and
 * {@link java.util.IdentityHashMap}.
 * Useful for caches that need to key off of a {@code ==} comparison
 * instead of a {@code .equals}.
 * 
 * <p>This class is not a general-purpose {@link java.util.Map}
 * implementation! It intentionally violates
 * Map's general contract, which mandates the use of the equals method
 * when comparing objects. This class is designed for use only in the
 * rare cases wherein reference-equality semantics are required.
 * 
 * <p>This implementation was forked from <a href="http://cxf.apache.org/">Apache CXF</a>
 * but modified to <b>not</b> implement the {@link java.util.Map} interface and
 * without any set views on it, as those are error-prone and inefficient,
 * if not implemented carefully. The map only contains {@link Iterator} implementations
 * on the values and not-GCed keys. Lucene's implementation also supports {@code null}
 * keys, but those are never weak!
 *
 * @lucene.internal
 */
public final class WeakIdentityMap<K,V> {
  private final ReferenceQueue<Object> queue = new ReferenceQueue<Object>();
  private final Map<IdentityWeakReference, V> backingStore;

  /** Creates a new {@code WeakIdentityMap} based on a non-synchronized {@link HashMap}. */
  public static final <K,V> WeakIdentityMap<K,V> newHashMap() {
    return new WeakIdentityMap<K,V>(new HashMap<IdentityWeakReference,V>());
  }

  /** Creates a new {@code WeakIdentityMap} based on a {@link ConcurrentHashMap}. */
  public static final <K,V> WeakIdentityMap<K,V> newConcurrentHashMap() {
    return new WeakIdentityMap<K,V>(new ConcurrentHashMap<IdentityWeakReference,V>());
  }

  private WeakIdentityMap(Map<IdentityWeakReference, V> backingStore) {
    this.backingStore = backingStore;
  }

  public void clear() {
    backingStore.clear();
    reap();
  }

  public boolean containsKey(Object key) {
    reap();
    return backingStore.containsKey(new IdentityWeakReference(key, null));
  }

  public V get(Object key) {
    reap();
    return backingStore.get(new IdentityWeakReference(key, null));
  }

  public V put(K key, V value) {
    reap();
    return backingStore.put(new IdentityWeakReference(key, queue), value);
  }

  public boolean isEmpty() {
    return size() == 0;
  }

  public V remove(Object key) {
    reap();
    return backingStore.remove(new IdentityWeakReference(key, null));
  }

  public int size() {
    if (backingStore.isEmpty())
      return 0;
    reap();
    return backingStore.size();
  }
  
  /** Returns an iterator over all weak keys of this map.
   * Keys already garbage collected will not be returned.
   * This Iterator does not support removals. */
  public Iterator<K> keyIterator() {
    reap();
    final Iterator<IdentityWeakReference> iterator = backingStore.keySet().iterator();
    return new Iterator<K>() {
      // holds strong reference to next element in backing iterator:
      private Object next = null;
      // the backing iterator was already consumed:
      private boolean nextIsSet = false;
    
      public boolean hasNext() {
        return nextIsSet ? true : setNext();
      }
      
      @SuppressWarnings("unchecked")
      public K next() {
        if (nextIsSet || setNext()) {
          try {
            assert nextIsSet;
            return (K) next;
          } finally {
             // release strong reference and invalidate current value:
            nextIsSet = false;
            next = null;
          }
        }
        throw new NoSuchElementException();
      }
      
      public void remove() {
        throw new UnsupportedOperationException();
      }
      
      private boolean setNext() {
        assert !nextIsSet;
        while (iterator.hasNext()) {
          next = iterator.next().get();
          if (next == null) {
            // already garbage collected!
            continue;
          }
          // unfold "null" special value
          if (next == NULL) {
            next = null;
          }
          return nextIsSet = true;
        }
        return false;
      }
    };
  }
  
  /** Returns an iterator over all values of this map.
   * This iterator may return values whose key is already
   * garbage collected while iterator is consumed. */
  public Iterator<V> valueIterator() {
    reap();
    return backingStore.values().iterator();
  }

  private void reap() {
    Reference<?> zombie;
    while ((zombie = queue.poll()) != null) {
      backingStore.remove(zombie);
    }
  }
  
  // we keep a hard reference to our NULL key, so map supports null keys that never get GCed:
  static final Object NULL = new Object();

  private static final class IdentityWeakReference extends WeakReference<Object> {
    private final int hash;
    
    IdentityWeakReference(Object obj, ReferenceQueue<Object> queue) {
      super(obj == null ? NULL : obj, queue);
      hash = System.identityHashCode(obj);
    }

    public int hashCode() {
      return hash;
    }

    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o instanceof IdentityWeakReference) {
        final IdentityWeakReference ref = (IdentityWeakReference)o;
        if (this.get() == ref.get()) {
          return true;
        }
      }
      return false;
    }
  }
}

