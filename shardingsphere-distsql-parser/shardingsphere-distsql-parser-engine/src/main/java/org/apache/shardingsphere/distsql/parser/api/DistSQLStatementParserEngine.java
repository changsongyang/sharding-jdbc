/*
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

package org.apache.shardingsphere.distsql.parser.api;

import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.apache.shardingsphere.distsql.parser.core.DistSQLParserFactory;
import org.apache.shardingsphere.distsql.parser.core.DistSQLVisitor;
import org.apache.shardingsphere.distsql.parser.spi.FeatureTypedSQLParserFacade;
import org.apache.shardingsphere.sql.parser.api.parser.SQLParser;
import org.apache.shardingsphere.sql.parser.core.parser.ParseASTNode;
import org.apache.shardingsphere.sql.parser.core.parser.SQLParserFactory;
import org.apache.shardingsphere.sql.parser.exception.SQLParsingException;
import org.apache.shardingsphere.sql.parser.sql.common.statement.SQLStatement;

import java.util.Collection;
import java.util.LinkedList;
import java.util.ServiceLoader;

/**
 * Dist SQL statement parser engine.
 */
public final class DistSQLStatementParserEngine {
    
    private static final Collection<FeatureTypedSQLParserFacade> RULE_PARSER_FACADES = new LinkedList<>();
    
    static {
        for (FeatureTypedSQLParserFacade each : ServiceLoader.load(FeatureTypedSQLParserFacade.class)) {
            RULE_PARSER_FACADES.add(each);
        }
    }
    
    /**
     * Parse SQL.
     *
     * @param sql SQL to be parsed
     * @return AST node
     */
    public SQLStatement parse(final String sql) {
        ParseASTNode parseASTNode;
        try {
            parseASTNode = parseFromStandardParser(sql);
        } catch (final ParseCancellationException ex) {
            parseASTNode = parseFromRuleParsers(sql);
        }
        if (parseASTNode.getRootNode() instanceof ErrorNode) {
            throw new SQLParsingException("Unsupported SQL of `%s`", sql);
        }
        return (SQLStatement) new DistSQLVisitor().visit(parseASTNode.getRootNode());
    }
    
    private ParseASTNode parseFromStandardParser(final String sql) {
        SQLParser sqlParser = DistSQLParserFactory.newInstance(sql);
        try {
            return (ParseASTNode) sqlParser.parse();
        } catch (final ParseCancellationException ex) {
            throw new SQLParsingException("You have an error in your SQL syntax.");
        }
    }
    
    private ParseASTNode parseFromRuleParsers(final String sql) {
        for (FeatureTypedSQLParserFacade each : RULE_PARSER_FACADES) {
            try {
                return (ParseASTNode) SQLParserFactory.newInstance(sql, each.getLexerClass(), each.getParserClass()).parse();
            } catch (final ParseCancellationException ignored) {
            }
        }
        throw new SQLParsingException("You have an error in your SQL syntax.");
    }
}
