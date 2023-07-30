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

package org.apache.shardingsphere.infra.database.core.type;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.shardingsphere.infra.database.core.metadata.database.DialectDatabaseMetaData;
import org.apache.shardingsphere.infra.database.core.spi.DatabaseTypedSPILoader;
import org.apache.shardingsphere.infra.util.exception.ShardingSpherePreconditions;
import org.apache.shardingsphere.infra.util.spi.ShardingSphereServiceLoader;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Database type factory.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DatabaseTypeFactory {
    
    /**
     * Get database type.
     *
     * @param url database URL
     * @return database type
     */
    public static DatabaseType get(final String url) {
        Collection<DatabaseType> databaseTypes = ShardingSphereServiceLoader.getServiceInstances(DatabaseType.class).stream().filter(each -> matchURLs(url, each)).collect(Collectors.toList());
        ShardingSpherePreconditions.checkState(!databaseTypes.isEmpty(), () -> new UnsupportedStorageTypeException(url));
        for (DatabaseType each : databaseTypes) {
            if (each.getTrunkDatabaseType().isPresent()) {
                return each;
            }
        }
        return databaseTypes.iterator().next();
    }
    
    private static boolean matchURLs(final String url, final DatabaseType databaseType) {
        return databaseType.getJdbcUrlPrefixes().stream().anyMatch(url::startsWith);
    }
    
    /**
     * Get all branch database types.
     * 
     * @param databaseType database type
     * @return all branch database types
     */
    public static Collection<DatabaseType> getAllBranchDatabaseTypes(final DatabaseType databaseType) {
        return ShardingSphereServiceLoader.getServiceInstances(DatabaseType.class)
                .stream().filter(each -> each.getTrunkDatabaseType().map(optional -> optional == databaseType).orElse(false)).collect(Collectors.toList());
    }
    
    /**
     * Get default schema name.
     *
     * @param databaseType database type
     * @param databaseName database name
     * @return default schema name
     */
    public static String getDefaultSchemaName(final DatabaseType databaseType, final String databaseName) {
        return DatabaseTypedSPILoader.getService(DialectDatabaseMetaData.class, databaseType).getDefaultSchema().orElseGet(() -> null == databaseName ? null : databaseName.toLowerCase());
    }
}
