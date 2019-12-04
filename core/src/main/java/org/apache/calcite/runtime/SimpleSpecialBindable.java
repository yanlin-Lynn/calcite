/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.calcite.runtime;

import org.apache.calcite.linq4j.Enumerable;

import java.util.List;

public class SimpleSpecialBindable implements Bindable, Typed {

  public org.apache.calcite.linq4j.Enumerable bind(final org.apache.calcite.DataContext root) {
    Enumerable v2 = org.apache.calcite.linq4j.Linq4j.singletonEnumerable(org.apache.calcite.linq4j.Linq4j.asEnumerable(new Integer[] {
        1,
        2}).select(new org.apache.calcite.linq4j.function.Function1() {
                     public Object[] apply(int o) {
                       return new Object[] {
                           o};
                     }
                     public Object apply(Integer o) {
                       return apply(
                           o.intValue());
                     }
                     public Object apply(Object o) {
                       return apply(
                           (Integer) o);
                     }
                   }
    ).toList());
    final org.apache.calcite.linq4j.Enumerable _inputEnumerable = org.apache.calcite.linq4j.Linq4j.asEnumerable(new Integer[] {
        0}).hashJoin(v2, new org.apache.calcite.linq4j.function.Function1() {
          public org.apache.calcite.runtime.FlatLists.ComparableEmptyList apply(int v1) {
            return org.apache.calcite.runtime.FlatLists.COMPARABLE_EMPTY_LIST;
          }
          public Object apply(Integer v1) {
            return apply(
                v1.intValue());
          }
          public Object apply(Object v1) {
            return apply(
                (Integer) v1);
          }
        }
        , new org.apache.calcite.linq4j.function.Function1() {
          public org.apache.calcite.runtime.FlatLists.ComparableEmptyList apply(java.util.List v1) {
            return org.apache.calcite.runtime.FlatLists.COMPARABLE_EMPTY_LIST;
          }
          public Object apply(Object v1) {
            return apply(
                (java.util.List) v1);
          }
        }
        , new org.apache.calcite.linq4j.function.Function2() {
          public Object[] apply(Integer left, java.util.List right) {
            return new Object[] {
                left,
                right};
          }
          public Object[] apply(Object left, Object right) {
            return apply(
                (Integer) left,
                (java.util.List) right);
          }
        }
        , null, false, false, null);
    return new org.apache.calcite.linq4j.AbstractEnumerable(){
      public org.apache.calcite.linq4j.Enumerator enumerator() {
        return new org.apache.calcite.linq4j.Enumerator(){
          public final org.apache.calcite.linq4j.Enumerator inputEnumerator = _inputEnumerable.enumerator();
          public void reset() {
            inputEnumerator.reset();
          }

          public boolean moveNext() {
            return inputEnumerator.moveNext();
          }

          public void close() {
            inputEnumerator.close();
          }

          public Object current() {
            Object[] inputEnumeratorCurrent = (Object[]) inputEnumerator.current();
            List currentList = (List) inputEnumeratorCurrent[1];
            return org.apache.calcite.runtime.SqlFunctions.slice(currentList);
          }

        };
      }

    };
  }


  public Class getElementType() {
    return java.util.List.class;
  }
}
