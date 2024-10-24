/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may
 *    "Alice" appear in their name, without prior written permission of
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is
 *    contributed by Electronic Arts Inc. and may be used for personal,
 *    non-commercial, and academic use only. Redistributions of any program
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/
package edu.cmu.cs.dennisc.java.util;

import edu.cmu.cs.dennisc.java.lang.ArrayUtilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Dennis Cosgrove
 */
public class Lists {
  private Lists() {
    throw new Error();
  }

  public static <E> LinkedList<E> newLinkedList() {
    return new LinkedList<E>();
  }

  public static <E> LinkedList<E> newLinkedList(Collection<E> other) {
    LinkedList<E> rv = new LinkedList<E>();
    rv.addAll(other);
    return rv;
  }

  public static <E, X extends E> LinkedList<E> newLinkedList(X... array) {
    LinkedList<E> rv = new LinkedList<E>();
    Collections.addAll(rv, array);
    return rv;
  }

  public static <E> ArrayList<E> newArrayListWithInitialCapacity(int initialCapacity) {
    return new ArrayList<E>(initialCapacity);
  }

  public static <E> ArrayList<E> newArrayList() {
    return new ArrayList<E>();
  }

  public static <E, X extends E> ArrayList<E> newArrayList(X... array) {
    ArrayList<E> rv = new ArrayList<E>();
    Collections.addAll(rv, array);
    return rv;
  }

  public static <E> ArrayList<E> newArrayList(Collection<E> other) {
    ArrayList<E> rv = new ArrayList<E>();
    rv.addAll(other);
    return rv;
  }

  public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList() {
    return new CopyOnWriteArrayList<E>();
  }

  public static <E, X extends E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList(X... array) {
    CopyOnWriteArrayList<E> rv = new CopyOnWriteArrayList<E>();
    Collections.addAll(rv, array);
    return rv;
  }

  public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList(Collection<E> other) {
    CopyOnWriteArrayList<E> rv = new CopyOnWriteArrayList<E>();
    rv.addAll(other);
    return rv;
  }

  public static List<Byte> newList(byte[] array) {
    if (array != null) {
      ArrayList<Byte> rv = newArrayListWithInitialCapacity(array.length);
      for (byte item : array) {
        rv.add(item);
      }
      return rv;
    } else {
      return Collections.emptyList();
    }
  }

  public static List<Short> newList(short[] array) {
    if (array != null) {
      ArrayList<Short> rv = newArrayListWithInitialCapacity(array.length);
      for (short item : array) {
        rv.add(item);
      }
      return rv;
    } else {
      return Collections.emptyList();
    }
  }

  public static List<Integer> newList(int[] array) {
    if (array != null) {
      ArrayList<Integer> rv = newArrayListWithInitialCapacity(array.length);
      for (int item : array) {
        rv.add(item);
      }
      return rv;
    } else {
      return Collections.emptyList();
    }
  }

  public static List<Long> newList(long[] array) {
    if (array != null) {
      ArrayList<Long> rv = newArrayListWithInitialCapacity(array.length);
      for (long item : array) {
        rv.add(item);
      }
      return rv;
    } else {
      return Collections.emptyList();
    }
  }

  public static List<Float> newList(float[] array) {
    if (array != null) {
      ArrayList<Float> rv = newArrayListWithInitialCapacity(array.length);
      for (float item : array) {
        rv.add(item);
      }
      return rv;
    } else {
      return Collections.emptyList();
    }
  }

  public static List<Character> newList(char[] array) {
    if (array != null) {
      ArrayList<Character> rv = newArrayListWithInitialCapacity(array.length);
      for (char item : array) {
        rv.add(item);
      }
      return rv;
    } else {
      return Collections.emptyList();
    }
  }

  public static List<Double> newList(double[] array) {
    if (array != null) {
      ArrayList<Double> rv = newArrayListWithInitialCapacity(array.length);
      for (double item : array) {
        rv.add(item);
      }
      return rv;
    } else {
      return Collections.emptyList();
    }
  }

  public static <E, X extends E> List<List<E>> newArrayListOfSingleArrayList(X... list0Array) {
    ArrayList<List<E>> rv = new ArrayList<List<E>>();
    ArrayList<E> list0 = new ArrayList<E>();
    ArrayUtilities.set(list0, list0Array);
    rv.add(list0);
    return rv;
  }
}
