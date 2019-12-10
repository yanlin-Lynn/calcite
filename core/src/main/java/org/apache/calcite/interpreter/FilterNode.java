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

import org.apache.calcite.adapter.enumerable.RexToLixTranslator;
import org.apache.calcite.linq4j.function.Function1;
import org.apache.calcite.linq4j.tree.BlockBuilder;
import org.apache.calcite.linq4j.tree.Expression;
import org.apache.calcite.linq4j.tree.Expressions;
import org.apache.calcite.rel.core.Filter;

import com.google.common.collect.ImmutableList;

import org.apache.calcite.rex.RexFieldAccess;
import org.apache.calcite.rex.RexUtil;
import org.apache.calcite.rex.RexVisitorImpl;

import java.lang.reflect.Type;

/**
 * Interpreter node that implements a
 * {@link org.apache.calcite.rel.core.Filter}.
 */
public class FilterNode extends AbstractSingleNode<Filter> {
  private final Scalar condition;
  private final Context context;

  public FilterNode(Compiler compiler, Filter rel) {
    super(compiler, rel);
    this.context = compiler.createContext();
    final RexFieldAccess rexFieldAccess = rel.getCondition().accept(new RexVisitorImpl<RexFieldAccess>(true) {
      @Override public RexFieldAccess visitFieldAccess(RexFieldAccess fieldAccess) {
        return fieldAccess;
      }
    });
    if (rexFieldAccess != null) {
      final Function1<String, RexToLixTranslator.InputGetter> correlates = name -> {
        return new RexToLixTranslator.InputGetter(){
          @Override
          public Expression field(BlockBuilder list, int index, Type storageType) {
            // TODO: add parameter check here
            Object inputValue = context.correlates.get(name);
            final Expression expression = Expressions.constant(inputValue);
            // list.append(expression);
            return expression;
          }
        };
      };
      this.condition =
          compiler.compile(ImmutableList.of(rel.getCondition()),
              rel.getRowType(), correlates);
    } else {
      this.condition =
          compiler.compile(ImmutableList.of(rel.getCondition()),
              rel.getRowType());
    }
  }

  public void run() throws InterruptedException {
    Row row;
    while ((row = source.receive()) != null) {
      context.values = row.getValues();
      Boolean b = (Boolean) condition.execute(context);
      if (b != null && b) {
        sink.send(row);
      }
    }
  }
}

// End FilterNode.java
