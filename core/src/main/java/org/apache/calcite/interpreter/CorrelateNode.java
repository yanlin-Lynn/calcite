/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.calcite.interpreter;

import com.google.common.collect.ImmutableList;

import org.apache.calcite.rel.core.Correlate;
import org.apache.calcite.rel.core.Join;

/**
 * Interpreter node that implements a
 * {@link org.apache.calcite.rel.core.Correlate}.
 */
public class CorrelateNode implements Node {
  private final Source leftSource;
  private final Source rightSource;
  private final Sink sink;
  private final Correlate correlate;
  // private final Scalar condition;
  private final Context context;

  public CorrelateNode(Compiler compiler, Correlate rel) {
    leftSource = compiler.source(rel, 0);
    rightSource = compiler.source(rel, 1);
    sink = compiler.sink(rel);
    correlate = rel;
    context = compiler.createContext();
  }

  @Override
  public void run() throws InterruptedException {
    Row leftRow = null;
    Row rightRow = null;
    while((leftRow = leftSource.receive()) != null) {
      while ((rightRow = rightSource.receive()) != null) {
        sink.send(rightRow);
      }
    }
  }
}
