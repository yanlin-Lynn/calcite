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

import org.apache.calcite.linq4j.AbstractEnumerable;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.linq4j.Enumerator;
import org.apache.calcite.linq4j.Linq4j;
import org.apache.calcite.linq4j.function.Function1;
import org.apache.calcite.linq4j.function.Function2;

import java.util.List;

public class SpecialBindable implements Bindable, Typed {
  @Override
  public Enumerable bind(final org.apache.calcite.DataContext root) {
    final Enumerable _inputEnumerable = Linq4j
        .asEnumerable(new Integer[] {0})
        .hashJoin(
            Linq4j.singletonEnumerable(
                Linq4j.singletonEnumerable(
                    Linq4j.asEnumerable(new Integer[] {1, 2})
                        .select(new Function1() {
                          public Object[] apply(int o) {
                            return new Object[]{o};
                          }
                          public Object apply(Integer o) {
                            return apply(o.intValue());
                          }
                          public Object apply(Object o) {
                            return apply((Integer) o);
                          }
                        })
                        .toList())
                    .concat(Linq4j.singletonEnumerable(
                        Linq4j.asEnumerable(new Integer[] {3, 4})
                            .select(new Function1() {
                              public Object[] apply(int o) {
                                return new Object[]{o};
                              }
                              public Object apply(Integer o) {
                                return apply(o.intValue());
                              }
                              public Object apply(Object o) {
                                return apply((Integer) o);
                              }
                            })
                            .toList()))
                    .select(new Function1() {
                              public Object[] apply(java.util.List o) {
                                return new Object[] {o};
                              }
                              public Object apply(Object o) {
                                return apply(
                                    (java.util.List) o);
                              }
                            })
                    .toList()),
            new Function1() {
              public FlatLists.ComparableEmptyList apply(int v1) {
                return FlatLists.COMPARABLE_EMPTY_LIST;
              }
              public Object apply(Integer v1) {
                return apply(v1.intValue());
              }
              public Object apply(Object v1) {
                return apply((Integer) v1);
              }
            },
            new Function1() {
              public FlatLists.ComparableEmptyList apply(java.util.List v1) {
                return FlatLists.COMPARABLE_EMPTY_LIST;
              }
              public Object apply(Object v1) {
                return apply((java.util.List) v1);
              }
            },
            new Function2() {
              public Object[] apply(Integer left, java.util.List right) {
                return new Object[] {left, right};
              }
              public Object[] apply(Object left, Object right) {
                return apply((Integer) left, (java.util.List) right);
              }
            },
            null, false, false, null); // end for hashJoin call

    return new AbstractEnumerable() {
      public Enumerator enumerator() {
        return new Enumerator(){
          public final Enumerator inputEnumerator = _inputEnumerable.enumerator();
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
            Object current =  ((Object[]) inputEnumerator.current())[1];
            Object result = SqlFunctions.slice((java.util.List) current);
            result = SqlFunctions.slice((List) result);
            result = SqlFunctions.slice((List)result);
            return result;
          }
        };
      }
    };
  }
  public Class getElementType() {
    return java.util.List.class;
  }
}
