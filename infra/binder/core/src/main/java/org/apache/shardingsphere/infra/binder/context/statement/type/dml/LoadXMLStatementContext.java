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

package org.apache.shardingsphere.infra.binder.context.statement.type.dml;

import lombok.Getter;
import org.apache.shardingsphere.infra.binder.context.segment.table.TablesContext;
import org.apache.shardingsphere.infra.binder.context.statement.CommonSQLStatementContext;
import org.apache.shardingsphere.infra.binder.context.type.TableAvailable;
import org.apache.shardingsphere.infra.database.core.type.DatabaseType;
import org.apache.shardingsphere.sql.parser.statement.core.statement.dml.LoadXMLStatement;

/**
 * Load xml statement context.
 */
@Getter
public final class LoadXMLStatementContext extends CommonSQLStatementContext implements TableAvailable {
    
    private final TablesContext tablesContext;
    
    public LoadXMLStatementContext(final DatabaseType databaseType, final LoadXMLStatement sqlStatement) {
        super(databaseType, sqlStatement);
        tablesContext = new TablesContext(sqlStatement.getTable());
    }
    
    @Override
    public LoadXMLStatement getSqlStatement() {
        return (LoadXMLStatement) super.getSqlStatement();
    }
}
