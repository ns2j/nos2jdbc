/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.extension.jdbc.service;

import org.seasar.extension.jdbc.dialect.StandardDialect;
import org.seasar.extension.jdbc.manager.JdbcManagerImpl;
import org.seasar.extension.jdbc.manager.JdbcManagerTestImpl;
import org.seasar.extension.jdbc.meta.ColumnMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.EntityMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.PropertyMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.TableMetaFactoryImpl;
import org.seasar.extension.jta.TransactionManagerImpl;
import org.seasar.extension.jta.TransactionSynchronizationRegistryImpl;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;
import org.seasar.framework.mock.sql.MockDataSource;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author higa
 * 
 */
class S2AbstractServiceTest {

    private EmpService empDao;

    
    @BeforeEach
    void setUp() {
        JdbcManagerImpl manager = new JdbcManagerTestImpl();
	empDao = new EmpService();
	empDao.jdbcManager = manager;
    }

    /**
     * @throws Exception
     */
    @Test
    void testSelect() throws Exception {
        assertNotNull(empDao.select());
    }

    /**
     * @throws Exception
     */
    @Test
    void testSqlFilePathPrefix() throws Exception {
        assertEquals("META-INF/sql/org/seasar/extension/jdbc/entity/Emp/",
                empDao.sqlFilePathPrefix);
    }
}