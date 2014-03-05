package org.apache.lucene.util.fst;

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

import java.io.*;
import java.util.*;

import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.IntsRef;

/** Static helper methods.
 *
 * @lucene.experimental */
public final class Util {
  private Util() {
  }

  /** Looks up the output for this input, or null if the
   *  input is not accepted. */
  public static<T> T get(FST<T> fst, IntsRef input) throws IOException {

    // TODO: would be nice not to alloc this on every lookup
    final FST.Arc<T> arc = fst.getFirstArc(new FST.Arc<T>());

    final FST.BytesReader fstReader = fst.getBytesReader(0);

    // Accumulate output as we go
    T output = fst.outputs.getNoOutput();
    for(int i=0;i<input.length;i++) {
      if (fst.findTargetArc(input.ints[input.offset + i], arc, arc, fstReader) == null) {
        return null;
      }
      output = fst.outputs.add(output, arc.output);
    }

    if (arc.isFinal()) {
      return fst.outputs.add(output, arc.nextFinalOutput);
    } else {
      return null;
    }
  }

  // TODO: maybe a CharsRef version for BYTE2

  /** Looks up the output for this input, or null if the
   *  input is not accepted */
  public static<T> T get(FST<T> fst, BytesRef input) throws IOException {
    assert fst.inputType == FST.INPUT_TYPE.BYTE1;

    final FST.BytesReader fstReader = fst.getBytesReader(0);

    // TODO: would be nice not to alloc this on every lookup
    final FST.Arc<T> arc = fst.getFirstArc(new FST.Arc<T>());

    // Accumulate output as we go
    T output = fst.outputs.getNoOutput();
    for(int i=0;i<input.length;i++) {
      if (fst.findTargetArc(input.bytes[i+input.offset] & 0xFF, arc, arc, fstReader) == null) {
        return null;
      }
      output = fst.outputs.add(output, arc.output);
    }

    if (arc.isFinal()) {
      return fst.outputs.add(output, arc.nextFinalOutput);
    } else {
      return null;
    }
  }

  /** Reverse lookup (lookup by output instead of by input),
   *  in the special case when your FSTs outputs are
   *  strictly ascending.  This locates the input/output
   *  pair where the output is equal to the target, and will
   *  return null if that output does not exist.
   *
   *  <p>NOTE: this only works with FST<Long>, only
   *  works when the outputs are ascending in order with
   *  the inputs and only works when you shared
   *  the outputs (pass doShare=true to {@link
   *  PositiveIntOutputs#getSingleton}).
   *  For example, simple ordinals (0, 1,
   *  2, ...), or file offets (when appending to a file)
   *  fit this. */
  public static IntsRef getByOutput(FST<Long> fst, long targetOutput) throws IOException {

    final FST.BytesReader in = fst.getBytesReader(0);

    // TODO: would be nice not to alloc this on every lookup
    FST.Arc<Long> arc = fst.getFirstArc(new FST.Arc<Long>());
    
    FST.Arc<Long> scratchArc = new FST.Arc<Long>();

    final IntsRef result = new IntsRef();

    long output = arc.output;
    int upto = 0;

    //System.out.println("reverseLookup output=" + targetOutput);

    while(true) {
      //System.out.println("loop: output=" + output + " upto=" + upto + " arc=" + arc);
      if (arc.isFinal()) {
        final long finalOutput = output + arc.nextFinalOutput;
        //System.out.println("  isFinal finalOutput=" + finalOutput);
        if (finalOutput == targetOutput) {
          result.length = upto;
          //System.out.println("    found!");
          return result;
        } else if (finalOutput > targetOutput) {
          //System.out.println("    not found!");
          return null;
        }
      }

      if (FST.targetHasArcs(arc)) {
        //System.out.println("  targetHasArcs");
        if (result.ints.length == upto) {
          result.grow(1+upto);
        }
        
        fst.readFirstRealTargetArc(arc.target, arc, in);

        if (arc.bytesPerArc != 0) {

          int low = 0;
          int high = arc.numArcs-1;
          int mid = 0;
          //System.out.println("bsearch: numArcs=" + arc.numArcs + " target=" + targetOutput + " output=" + output);
          boolean exact = false;
          while (low <= high) {
            mid = (low + high) >>> 1;
            in.pos = arc.posArcsStart;
            in.skip(arc.bytesPerArc*mid);
            final byte flags = in.readByte();
            fst.readLabel(in);
            final long minArcOutput;
            if ((flags & FST.BIT_ARC_HAS_OUTPUT) != 0) {
              final long arcOutput = fst.outputs.read(in);
              minArcOutput = output + arcOutput;
            } else {
              minArcOutput = output;
            }
            //System.out.println("  cycle mid=" + mid + " label=" + (char) label + " output=" + minArcOutput);
            if (minArcOutput == targetOutput) {
              exact = true;
              break;
            } else if (minArcOutput < targetOutput) {
              low = mid + 1;
            } else {
              high = mid - 1;
            }
          }

          if (high == -1) {
            return null;
          } else if (exact) {
            arc.arcIdx = mid-1;
          } else {
            arc.arcIdx = low-2;
          }

          fst.readNextRealArc(arc, in);
          result.ints[upto++] = arc.label;
          output += arc.output;

        } else {

          FST.Arc<Long> prevArc = null;

          while(true) {
            //System.out.println("    cycle label=" + arc.label + " output=" + arc.output);

            // This is the min output we'd hit if we follow
            // this arc:
            final long minArcOutput = output + arc.output;

            if (minArcOutput == targetOutput) {
              // Recurse on this arc:
              //System.out.println("  match!  break");
              output = minArcOutput;
              result.ints[upto++] = arc.label;
              break;
            } else if (minArcOutput > targetOutput) {
              if (prevArc == null) {
                // Output doesn't exist
                return null;
              } else {
                // Recurse on previous arc:
                arc.copyFrom(prevArc);
                result.ints[upto++] = arc.label;
                output += arc.output;
                //System.out.println("    recurse prev label=" + (char) arc.label + " output=" + output);
                break;
              }
            } else if (arc.isLast()) {
              // Recurse on this arc:
              output = minArcOutput;
              //System.out.println("    recurse last label=" + (char) arc.label + " output=" + output);
              result.ints[upto++] = arc.label;
              break;
            } else {
              // Read next arc in this node:
              prevArc = scratchArc;
              prevArc.copyFrom(arc);
              //System.out.println("      after copy label=" + (char) prevArc.label + " vs " + (char) arc.label);
              fst.readNextRealArc(arc, in);
            }
          }
        }
      } else {
        //System.out.println("  no target arcs; not found!");
        return null;
      }
    }    
  }

  private static class FSTPath<T> implements Comparable<FSTPath<T>> {
    public FST.Arc<T> arc;
    public T cost;
    public final IntsRef input = new IntsRef();
    final Comparator<T> comparator;

    public FSTPath(T cost, FST.Arc<T> arc, Comparator<T> comparator) {
      this.arc = new FST.Arc<T>().copyFrom(arc);
      this.cost = cost;
      this.comparator = comparator;
    }

    @Override
    public String toString() {
      return "input=" + input + " cost=" + cost;
    }

    //@Override
    public int compareTo(FSTPath<T> other) {
      int cmp = comparator.compare(cost, other.cost);
      if (cmp == 0) {
        return input.compareTo(other.input);
      } else {
        return cmp;
      }
    }
  }

  private static class TopNSearcher<T> {

    private final FST<T> fst;
    private final FST.Arc<T> fromNode;
    private final int topN;
    
    final Comparator<T> comparator;

    // Set once the queue has filled:
    FSTPath<T> bottom = null;

    TreeSet<FSTPath<T>> queue = null;

    public TopNSearcher(FST<T> fst, FST.Arc<T> fromNode, int topN, Comparator<T> comparator) {
      this.fst = fst;
      this.topN = topN;
      this.fromNode = fromNode;
      this.comparator = comparator;
    }

    // If back plus this arc is competitive then add to queue:
    private void addIfCompetitive(FSTPath<T> path) {

      assert queue != null;

      T cost = fst.outputs.add(path.cost, path.arc.output);
      //System.out.println("  addIfCompetitive bottom=" + bottom + " queue.size()=" + queue.size());

      if (bottom != null) {
        int comp = comparator.compare(cost, bottom.cost);
        if (comp > 0) {
          // Doesn't compete
          return;
        } else if (comp == 0) {
          // Tie break by alpha sort on the input:
          path.input.grow(path.input.length+1);
          path.input.ints[path.input.length++] = path.arc.label;
          final int cmp = bottom.input.compareTo(path.input);
          path.input.length--;
          assert cmp != 0;
          if (cmp < 0) {
            // Doesn't compete
            return;
          }
        }
        // Competes
      } else {
        // Queue isn't full yet, so any path we hit competes:
      }

      final FSTPath<T> newPath = new FSTPath<T>(cost, path.arc, comparator);

      newPath.input.grow(path.input.length+1);
      System.arraycopy(path.input.ints, 0, newPath.input.ints, 0, path.input.length);
      newPath.input.ints[path.input.length] = path.arc.label;
      newPath.input.length = path.input.length+1;

      //System.out.println("    add path=" + newPath);
      queue.add(newPath);
      if (bottom != null) {
        // only if java6 is available: see replacement below
        // final FSTPath removed = queue.pollLast();
        assert !queue.isEmpty();
        final FSTPath<T> removed = queue.last();
        queue.remove(removed);
        
        assert removed == bottom;
        bottom = queue.last();
        //System.out.println("    now re-set bottom: " + bottom + " queue=" + queue);
      } else if (queue.size() == topN) {
        // Queue just filled up:
        bottom = queue.last();
        //System.out.println("    now set bottom: " + bottom);
      }
    }

    public MinResult<T>[] search() throws IOException {
      //System.out.println("  search topN=" + topN);
      final FST.Arc<T> scratchArc = new FST.Arc<T>();

      final List<MinResult<T>> results = new ArrayList<MinResult<T>>();

      final T NO_OUTPUT = fst.outputs.getNoOutput();

      // TODO: we could enable FST to sorting arcs by weight
      // as it freezes... can easily do this on first pass
      // (w/o requiring rewrite)

      // TODO: maybe we should make an FST.INPUT_TYPE.BYTE0.5!?
      // (nibbles)

      // For each top N path:
      while (results.size() < topN) {
        //System.out.println("\nfind next path");

        FSTPath<T> path;

        if (queue == null) {

          if (results.size() != 0) {
            // Ran out of paths
            break;
          }

          // First pass (top path): start from original fromNode
          if (topN > 1) {
            queue = new TreeSet<FSTPath<T>>();
          }

          T minArcCost = null;
          FST.Arc<T> minArc = null;

          path = new FSTPath<T>(NO_OUTPUT, fromNode, comparator);
          fst.readFirstTargetArc(fromNode, path.arc);

          // Bootstrap: find the min starting arc
          while (true) {
            T arcScore = path.arc.output;
            //System.out.println("  arc=" + (char) path.arc.label + " cost=" + arcScore);
            if (minArcCost == null || comparator.compare(arcScore, minArcCost) < 0) {
              minArcCost = arcScore;
              minArc = scratchArc.copyFrom(path.arc);
              //System.out.println("    **");
            }
            if (queue != null) {
              addIfCompetitive(path);
            }
            if (path.arc.isLast()) {
              break;
            }
            fst.readNextArc(path.arc);
          }

          assert minArc != null;

          if (queue != null) {
            // Remove top path since we are now going to
            // pursue it:
            // only on java6: see replacement 3 lines below
            // path = queue.pollFirst();
            assert !queue.isEmpty();
            path = queue.first();
            queue.remove(path);
            
            //System.out.println("  remove init path=" + path);
            assert path.arc.label == minArc.label;
            if (bottom != null && queue.size() == topN-1) {
              bottom = queue.last();
              //System.out.println("    set init bottom: " + bottom);
            }
          } else {
            path.arc.copyFrom(minArc);
            path.input.grow(1);
            path.input.ints[0] = minArc.label;
            path.input.length = 1;
            path.cost = minArc.output;
          }

        } else {
          // below is only on java5: see replacement code below
          /*
          path = queue.pollFirst();
          if (path == null) {
            // There were less than topN paths available:
            break;
          }
          */
          
          // must do this empty check, first() throws exception.
          if (queue.isEmpty()) {
            // There were less than topN paths available:
            path = null;
            break;
          } else {
            path = queue.first();
            queue.remove(path);
          }
        }

        if (path.arc.label == FST.END_LABEL) {
          //System.out.println("    empty string!  cost=" + path.cost);
          // Empty string!
          path.input.length--;
          results.add(new MinResult<T>(path.input, path.cost, comparator));
          continue;
        }

        if (results.size() == topN-1) {
          // Last path -- don't bother w/ queue anymore:
          queue = null;
        }

        //System.out.println("  path: " + path);
        
        // We take path and find its "0 output completion",
        // ie, just keep traversing the first arc with
        // NO_OUTPUT that we can find, since this must lead
        // to the minimum path that completes from
        // path.arc.

        // For each input letter:
        while (true) {

          //System.out.println("\n    cycle path: " + path);         
          fst.readFirstTargetArc(path.arc, path.arc);

          // For each arc leaving this node:
          boolean foundZero = false;
          while(true) {
            //System.out.println("      arc=" + (char) path.arc.label + " cost=" + path.arc.output);
            // tricky: instead of comparing output == 0, we must
            // express it via the comparator compare(output, 0) == 0
            if (comparator.compare(NO_OUTPUT, path.arc.output) == 0) {
              if (queue == null) {
                foundZero = true;
                break;
              } else if (!foundZero) {
                scratchArc.copyFrom(path.arc);
                foundZero = true;
              } else {
                addIfCompetitive(path);
              }
            } else if (queue != null) {
              addIfCompetitive(path);
            }
            if (path.arc.isLast()) {
              break;
            }
            fst.readNextArc(path.arc);
          }

          assert foundZero;

          if (queue != null) {
            // TODO: maybe we can save this copyFrom if we
            // are more clever above... eg on finding the
            // first NO_OUTPUT arc we'd switch to using
            // scratchArc
            path.arc.copyFrom(scratchArc);
          }

          if (path.arc.label == FST.END_LABEL) {
            // Add final output:
            //System.out.println("    done!: " + path);
            results.add(new MinResult<T>(path.input, fst.outputs.add(path.cost, path.arc.output), comparator));
            break;
          } else {
            path.input.grow(1+path.input.length);
            path.input.ints[path.input.length] = path.arc.label;
            path.input.length++;
            path.cost = fst.outputs.add(path.cost, path.arc.output);
          }
        }
      }
    
      @SuppressWarnings({"rawtypes","unchecked"}) final MinResult<T>[] arr =
        (MinResult<T>[]) new MinResult[results.size()];
      return results.toArray(arr);
    }
  }

  /** Holds a single input (IntsRef) + output, returned by
   *  {@link #shortestPaths}. */
  public final static class MinResult<T> implements Comparable<MinResult<T>> {
    public final IntsRef input;
    public final T output;
    final Comparator<T> comparator;
    public MinResult(IntsRef input, T output, Comparator<T> comparator) {
      this.input = input;
      this.output = output;
      this.comparator = comparator;
    }

    //@Override
    public int compareTo(MinResult<T> other) {
      int cmp = comparator.compare(output, other.output);
      if (cmp == 0) {
        return input.compareTo(other.input);
      } else {
        return cmp;
      }
    }
  }

  /** Starting from node, find the top N min cost 
   * completions to a final node.
   *
   *  <p>NOTE: you must share the outputs when you build the
   *  FST (pass doShare=true to {@link
   *  PositiveIntOutputs#getSingleton}). */

  public static <T> MinResult<T>[] shortestPaths(FST<T> fst, FST.Arc<T> fromNode, Comparator<T> comparator, int topN) throws IOException {
    return new TopNSearcher<T>(fst, fromNode, topN, comparator).search();
  } 

  /**
   * Dumps an {@link FST} to a GraphViz's <code>dot</code> language description
   * for visualization. Example of use:
   * 
   * <pre class="prettyprint">
   * PrintWriter pw = new PrintWriter(&quot;out.dot&quot;);
   * Util.toDot(fst, pw, true, true);
   * pw.close();
   * </pre>
   * 
   * and then, from command line:
   * 
   * <pre>
   * dot -Tpng -o out.png out.dot
   * </pre>
   * 
   * <p>
   * Note: larger FSTs (a few thousand nodes) won't even render, don't bother.
   * 
   * @param sameRank
   *          If <code>true</code>, the resulting <code>dot</code> file will try
   *          to order states in layers of breadth-first traversal. This may
   *          mess up arcs, but makes the output FST's structure a bit clearer.
   * 
   * @param labelStates
   *          If <code>true</code> states will have labels equal to their offsets in their
   *          binary format. Expands the graph considerably. 
   * 
   * @see "http://www.graphviz.org/"
   */
  public static <T> void toDot(FST<T> fst, Writer out, boolean sameRank, boolean labelStates) 
    throws IOException {    
    final String expandedNodeColor = "blue";

    // This is the start arc in the automaton (from the epsilon state to the first state 
    // with outgoing transitions.
    final FST.Arc<T> startArc = fst.getFirstArc(new FST.Arc<T>());

    // A queue of transitions to consider for the next level.
    final List<FST.Arc<T>> thisLevelQueue = new ArrayList<FST.Arc<T>>();

    // A queue of transitions to consider when processing the next level.
    final List<FST.Arc<T>> nextLevelQueue = new ArrayList<FST.Arc<T>>();
    nextLevelQueue.add(startArc);
    //System.out.println("toDot: startArc: " + startArc);
    
    // A list of states on the same level (for ranking).
    final List<Integer> sameLevelStates = new ArrayList<Integer>();

    // A bitset of already seen states (target offset).
    final BitSet seen = new BitSet();
    seen.set(startArc.target);

    // Shape for states.
    final String stateShape = "circle";
    final String finalStateShape = "doublecircle";

    // Emit DOT prologue.
    out.write("digraph FST {\n");
    out.write("  rankdir = LR; splines=true; concentrate=true; ordering=out; ranksep=2.5; \n");

    if (!labelStates) {
      out.write("  node [shape=circle, width=.2, height=.2, style=filled]\n");      
    }

    emitDotState(out, "initial", "point", "white", "");

    final T NO_OUTPUT = fst.outputs.getNoOutput();

    // final FST.Arc<T> scratchArc = new FST.Arc<T>();

    {
      final String stateColor;
      if (fst.isExpandedTarget(startArc)) {
        stateColor = expandedNodeColor;
      } else {
        stateColor = null;
      }

      final boolean isFinal;
      final T finalOutput;
      if (startArc.isFinal()) {
        isFinal = true;
        finalOutput = startArc.nextFinalOutput == NO_OUTPUT ? null : startArc.nextFinalOutput;
      } else {
        isFinal = false;
        finalOutput = null;
      }
      
      emitDotState(out, Integer.toString(startArc.target), isFinal ? finalStateShape : stateShape, stateColor, finalOutput == null ? "" : fst.outputs.outputToString(finalOutput));
    }

    out.write("  initial -> " + startArc.target + "\n");

    int level = 0;

    final FST.BytesReader r = fst.getBytesReader(0);

    while (!nextLevelQueue.isEmpty()) {
      // we could double buffer here, but it doesn't matter probably.
      //System.out.println("next level=" + level);
      thisLevelQueue.addAll(nextLevelQueue);
      nextLevelQueue.clear();

      level++;
      out.write("\n  // Transitions and states at level: " + level + "\n");
      while (!thisLevelQueue.isEmpty()) {
        final FST.Arc<T> arc = thisLevelQueue.remove(thisLevelQueue.size() - 1);
        //System.out.println("  pop: " + arc);
        if (FST.targetHasArcs(arc)) {
          // scan all target arcs
          //System.out.println("  readFirstTarget...");
          final int node = arc.target;

          fst.readFirstRealTargetArc(arc.target, arc, r);

          //System.out.println("    firstTarget: " + arc);

          while (true) {

            //System.out.println("  cycle arc=" + arc);
            // Emit the unseen state and add it to the queue for the next level.
            if (arc.target >= 0 && !seen.get(arc.target)) {

              /*
              boolean isFinal = false;
              T finalOutput = null;
              fst.readFirstTargetArc(arc, scratchArc);
              if (scratchArc.isFinal() && fst.targetHasArcs(scratchArc)) {
                // target is final
                isFinal = true;
                finalOutput = scratchArc.output == NO_OUTPUT ? null : scratchArc.output;
                System.out.println("dot hit final label=" + (char) scratchArc.label);
              }
              */
              final String stateColor;
              if (fst.isExpandedTarget(arc)) {
                stateColor = expandedNodeColor;
              } else {
                stateColor = null;
              }

              final String finalOutput;
              if (arc.nextFinalOutput != null && arc.nextFinalOutput != NO_OUTPUT) {
                finalOutput = fst.outputs.outputToString(arc.nextFinalOutput);
              } else {
                finalOutput = "";
              }

              emitDotState(out, Integer.toString(arc.target), stateShape, stateColor, finalOutput);
              // To see the node address, use this instead:
              //emitDotState(out, Integer.toString(arc.target), stateShape, stateColor, String.valueOf(arc.target));
              seen.set(arc.target);
              nextLevelQueue.add(new FST.Arc<T>().copyFrom(arc));
              sameLevelStates.add(arc.target);
            }

            String outs;
            if (arc.output != NO_OUTPUT) {
              outs = "/" + fst.outputs.outputToString(arc.output);
            } else {
              outs = "";
            }

            if (!FST.targetHasArcs(arc) && arc.isFinal() && arc.nextFinalOutput != NO_OUTPUT) {
              // Tricky special case: sometimes, due to
              // pruning, the builder can [sillily] produce
              // an FST with an arc into the final end state
              // (-1) but also with a next final output; in
              // this case we pull that output up onto this
              // arc
              outs = outs + "/[" + fst.outputs.outputToString(arc.nextFinalOutput) + "]";
            }

            final String arcColor;
            if (arc.flag(FST.BIT_TARGET_NEXT)) {
              arcColor = "red";
            } else {
              arcColor = "black";
            }

            assert arc.label != FST.END_LABEL;
            out.write("  " + node + " -> " + arc.target + " [label=\"" + printableLabel(arc.label) + outs + "\"" + (arc.isFinal() ? " style=\"bold\"" : "" ) + " color=\"" + arcColor + "\"]\n");
                   
            // Break the loop if we're on the last arc of this state.
            if (arc.isLast()) {
              //System.out.println("    break");
              break;
            }
            fst.readNextRealArc(arc, r);
          }
        }
      }

      // Emit state ranking information.
      if (sameRank && sameLevelStates.size() > 1) {
        out.write("  {rank=same; ");
        for (int state : sameLevelStates) {
          out.write(state + "; ");
        }
        out.write(" }\n");
      }
      sameLevelStates.clear();                
    }

    // Emit terminating state (always there anyway).
    out.write("  -1 [style=filled, color=black, shape=doublecircle, label=\"\"]\n\n");
    out.write("  {rank=sink; -1 }\n");
    
    out.write("}\n");
    out.flush();
  }

  /**
   * Emit a single state in the <code>dot</code> language. 
   */
  private static void emitDotState(Writer out, String name, String shape,
      String color, String label) throws IOException {
    out.write("  " + name 
        + " [" 
        + (shape != null ? "shape=" + shape : "") + " "
        + (color != null ? "color=" + color : "") + " "
        + (label != null ? "label=\"" + label + "\"" : "label=\"\"") + " "
        + "]\n");
  }

  /**
   * Ensures an arc's label is indeed printable (dot uses US-ASCII). 
   */
  private static String printableLabel(int label) {
    if (label >= 0x20 && label <= 0x7d) {
      return Character.toString((char) label);
    } else {
      return "0x" + Integer.toHexString(label);
    }
  }

  /** Decodes the Unicode codepoints from the provided
   *  CharSequence and places them in the provided scratch
   *  IntsRef, which must not be null, returning it. */
  public static IntsRef toUTF32(CharSequence s, IntsRef scratch) {
    int charIdx = 0;
    int intIdx = 0;
    final int charLimit = s.length();
    while(charIdx < charLimit) {
      scratch.grow(intIdx+1);
      final int utf32 = Character.codePointAt(s, charIdx);
      scratch.ints[intIdx] = utf32;
      charIdx += Character.charCount(utf32);
      intIdx++;
    }
    scratch.length = intIdx;
    return scratch;
  }

  /** Decodes the Unicode codepoints from the provided
   *  char[] and places them in the provided scratch
   *  IntsRef, which must not be null, returning it. */
  public static IntsRef toUTF32(char[] s, int offset, int length, IntsRef scratch) {
    int charIdx = offset;
    int intIdx = 0;
    final int charLimit = offset + length;
    while(charIdx < charLimit) {
      scratch.grow(intIdx+1);
      final int utf32 = Character.codePointAt(s, charIdx);
      scratch.ints[intIdx] = utf32;
      charIdx += Character.charCount(utf32);
      intIdx++;
    }
    scratch.length = intIdx;
    return scratch;
  }

  /** Just takes unsigned byte values from the BytesRef and
   *  converts into an IntsRef. */
  public static IntsRef toIntsRef(BytesRef input, IntsRef scratch) {
    scratch.grow(input.length);
    for(int i=0;i<input.length;i++) {
      scratch.ints[i] = input.bytes[i+input.offset] & 0xFF;
    }
    scratch.length = input.length;
    return scratch;
  }

  /** Just converts IntsRef to BytesRef; you must ensure the
   *  int values fit into a byte. */
  public static BytesRef toBytesRef(IntsRef input, BytesRef scratch) {
    scratch.grow(input.length);
    for(int i=0;i<input.length;i++) {
      scratch.bytes[i] = (byte) input.ints[i+input.offset];
    }
    scratch.length = input.length;
    return scratch;
  }
}
