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

package org.apache.shardingsphere.sql.parser.sql.dialect.statement.sqlserver.dml;

import lombok.Setter;
import org.apache.shardingsphere.sql.parser.sql.common.segment.generic.WithSegment;
import org.apache.shardingsphere.sql.parser.sql.common.statement.dml.UpdateStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.segment.sqlserver.hint.OptionHintSegment;
import org.apache.shardingsphere.sql.parser.sql.dialect.segment.sqlserver.hint.WithTableHintSegment;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.sqlserver.SQLServerStatement;

import java.util.Optional;

/**
 * SQLServer update statement.
 */
@Setter
public final class SQLServerUpdateStatement extends UpdateStatement implements SQLServerStatement {
    
    private WithSegment withSegment;
    
    private WithTableHintSegment withTableHintSegment;
    
    private OptionHintSegment optionHintSegment;
    
    /**
     * Get with segment.
     *
     * @return with segment.
     */
    public Optional<WithSegment> getWithSegment() {
        return Optional.ofNullable(withSegment);
    }
    
    /**
     * Get with table hint segment.
     *
     * @return with table hint segment.
     */
    public Optional<WithTableHintSegment> getWithTableHintSegment() {
        return Optional.ofNullable(withTableHintSegment);
    }
    
    /**
     * Get option hint segment.
     *
     * @return option hint segment.
     */
    public Optional<OptionHintSegment> getOptionHintSegment() {
        return Optional.ofNullable(optionHintSegment);
    }
}
