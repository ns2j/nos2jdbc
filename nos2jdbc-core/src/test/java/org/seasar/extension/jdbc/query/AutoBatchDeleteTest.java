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
package org.seasar.extension.jdbc.query;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.SqlLog;
import org.seasar.extension.jdbc.SqlLogRegistry;
import org.seasar.extension.jdbc.SqlLogRegistryLocator;
import org.seasar.extension.jdbc.dialect.StandardDialect;
import org.seasar.extension.jdbc.entity.Eee;
import org.seasar.extension.jdbc.exception.QueryTwiceExecutionRuntimeException;
import org.seasar.extension.jdbc.manager.JdbcManagerImpl;
import org.seasar.extension.jdbc.meta.ColumnMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.EntityMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.PropertyMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.TableMetaFactoryImpl;
import org.seasar.extension.jta.TransactionManagerImpl;
import org.seasar.extension.jta.TransactionSynchronizationRegistryImpl;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;
import org.seasar.framework.mock.sql.MockDataSource;
import org.seasar.framework.mock.sql.MockPreparedStatement;

import jakarta.persistence.OptimisticLockException;

/**
 * @author koichik
 */
class AutoBatchDeleteTest {

    private JdbcManagerImpl manager;

    private int addBatchCalled;

    
    @BeforeEach
    void setUp() throws Exception {
        manager = new JdbcManagerImpl();
        manager.setSyncRegistry(new TransactionSynchronizationRegistryImpl(
                new TransactionManagerImpl()));
        manager.setDataSource(new MockDataSource());
        manager.setDialect(new StandardDialect());

        PersistenceConventionImpl convention = new PersistenceConventionImpl();
        EntityMetaFactoryImpl emFactory = new EntityMetaFactoryImpl();
        emFactory.setPersistenceConvention(convention);
        TableMetaFactoryImpl tableMetaFactory = new TableMetaFactoryImpl();
        tableMetaFactory.setPersistenceConvention(convention);
        emFactory.setTableMetaFactory(tableMetaFactory);

        PropertyMetaFactoryImpl pFactory = new PropertyMetaFactoryImpl();
        pFactory.setPersistenceConvention(convention);
        ColumnMetaFactoryImpl cmFactory = new ColumnMetaFactoryImpl();
        cmFactory.setPersistenceConvention(convention);
        pFactory.setColumnMetaFactory(cmFactory);
        emFactory.setPropertyMetaFactory(pFactory);
        emFactory.initialize();
        manager.setEntityMetaFactory(emFactory);
    }

    
    @AfterEach
    void tearDown() throws Exception {
        SqlLogRegistry regisry = SqlLogRegistryLocator.getInstance();
        regisry.clear();
        manager = null;
    }

    /**
     * 
     */
    @Test
    void testCallerClass() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchDeleteImpl<Eee> query = new AutoBatchDeleteImpl<Eee>(manager,
                entities);
        assertSame(query, query.callerClass(getClass()));
        assertEquals(getClass(), query.callerClass);
    }

    /**
     * 
     */
    @Test
    void testCallerMethodName() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchDeleteImpl<Eee> query = new AutoBatchDeleteImpl<Eee>(manager,
                entities);
        assertSame(query, query.callerMethodName("hoge"));
        assertEquals("hoge", query.callerMethodName);
    }

    /**
     * 
     */
    @Test
    void testQueryTimeout() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchDeleteImpl<Eee> query = new AutoBatchDeleteImpl<Eee>(manager,
                entities);
        assertSame(query, query.queryTimeout(100));
        assertEquals(100, query.queryTimeout);
    }

    /**
     * 
     */
    @Test
    void testIgnoreVersion() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchDeleteImpl<Eee> query = new AutoBatchDeleteImpl<Eee>(manager,
                entities);
        assertFalse(query.ignoreVersion);
        assertSame(query, query.ignoreVersion());
        assertTrue(query.ignoreVersion);
    }

    /**
     * 
     */
    @Test
    void testPrepareWhereClause() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchDeleteImpl<Eee> query = new AutoBatchDeleteImpl<Eee>(manager,
                entities);
        query.prepare("execute");
        assertEquals(" where ID = ? and VERSION = ?", query.whereClause.toSql());
    }

    /**
     * 
     */
    @Test
    void testPrepareWhereClause_ignoreVersion() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchDeleteImpl<Eee> query = new AutoBatchDeleteImpl<Eee>(manager,
                entities);
        query.ignoreVersion();
        query.prepare("execute");
        assertEquals(" where ID = ?", query.whereClause.toSql());
    }

    /**
     * 
     */
    @Test
    void testPrepareSql() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchDeleteImpl<Eee> query = new AutoBatchDeleteImpl<Eee>(manager,
                entities);
        query.prepare("execute");
        assertEquals("delete from EEE where ID = ? and VERSION = ?",
                query.executedSql);
    }

    /**
     * 
     */
    @Test
    void testPrepareSql_ignoreVersion() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchDeleteImpl<Eee> query = new AutoBatchDeleteImpl<Eee>(manager,
                entities);
        query.ignoreVersion();
        query.prepare("execute");
        assertEquals("delete from EEE where ID = ?", query.executedSql);
    }

    /**
     * 
     */
    @Test
    void testPrepareParams() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchDeleteImpl<Eee> query = new AutoBatchDeleteImpl<Eee>(manager,
                entities);
        query.prepare("execute");

        query.prepareParams(entities.get(0));
        assertEquals(2, query.getParamSize());
        assertEquals(Integer.valueOf(1), query.getParam(0).value);
        assertEquals(Long.valueOf(0L), query.getParam(1).value);
        query.resetParams();

        query.prepareParams(entities.get(1));
        assertEquals(2, query.getParamSize());
        assertEquals(Integer.valueOf(2), query.getParam(0).value);
        assertEquals(Long.valueOf(0L), query.getParam(1).value);
        query.resetParams();

        query.prepareParams(entities.get(2));
        assertEquals(2, query.getParamSize());
        assertEquals(Integer.valueOf(3), query.getParam(0).value);
        assertEquals(Long.valueOf(0L), query.getParam(1).value);
    }

    /**
     * 
     */
    @Test
    void testPrepareParams_ignoreVersion() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchDeleteImpl<Eee> query = new AutoBatchDeleteImpl<Eee>(manager,
                entities);
        query.ignoreVersion();
        query.prepare("execute");

        query.prepareParams(entities.get(0));
        assertEquals(1, query.getParamSize());
        assertEquals(Integer.valueOf(1), query.getParam(0).value);
        query.resetParams();

        query.prepareParams(entities.get(1));
        assertEquals(1, query.getParamSize());
        assertEquals(Integer.valueOf(2), query.getParam(0).value);
        query.resetParams();

        query.prepareParams(entities.get(2));
        assertEquals(1, query.getParamSize());
        assertEquals(Integer.valueOf(3), query.getParam(0).value);
    }

    /**
     * @throws Exception
     */
    @Test
    void testExecute() throws Exception {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchDeleteImpl<Eee> query = new AutoBatchDeleteImpl<Eee>(manager,
                entities) {

            
            @Override
            protected PreparedStatement getPreparedStatement(
                    JdbcContext jdbcContext) {
                MockPreparedStatement ps = new MockPreparedStatement(null, null) {

                    
                    @Override
                    public int[] executeBatch() throws SQLException {
                        return new int[] { 1, 1, 1 };
                    }

                    
                    @Override
                    public void addBatch() throws SQLException {
                        ++addBatchCalled;
                    }
                };
                return ps;
            }

        };
        int[] result = query.execute();
        assertEquals(3, addBatchCalled);
        assertEquals(3, result.length);
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals("delete from EEE where ID = 3 and VERSION = 0", sqlLog
                .getCompleteSql());

        try {
            query.execute();
            fail();
        } catch (QueryTwiceExecutionRuntimeException expected) {
        }
    }

    /**
     * @throws Exception
     */
    @Test
    void testExecute_includesVersion() throws Exception {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchDeleteImpl<Eee> query = new AutoBatchDeleteImpl<Eee>(manager,
                entities) {

            
            @Override
            protected PreparedStatement getPreparedStatement(
                    JdbcContext jdbcContext) {
                MockPreparedStatement ps = new MockPreparedStatement(null, null) {

                    
                    @Override
                    public int[] executeBatch() throws SQLException {
                        return new int[] { 1, 1, 1 };
                    }

                    
                    @Override
                    public void addBatch() throws SQLException {
                        ++addBatchCalled;
                    }
                };
                return ps;
            }

        };
        query.ignoreVersion();
        int[] result = query.execute();
        assertEquals(3, addBatchCalled);
        assertEquals(3, result.length);
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals("delete from EEE where ID = 3", sqlLog.getCompleteSql());
    }

    /**
     * @throws Exception
     */
    @Test
    void testOptimisticLock() throws Exception {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchDeleteImpl<Eee> query = new AutoBatchDeleteImpl<Eee>(manager,
                entities) {

            
            @Override
            protected PreparedStatement getPreparedStatement(
                    JdbcContext jdbcContext) {
                MockPreparedStatement ps = new MockPreparedStatement(null, null) {

                    
                    @Override
                    public int[] executeBatch() throws SQLException {
                        return new int[] { 1, 0, 1 };
                    }

                    
                    @Override
                    public void addBatch() throws SQLException {
                        ++addBatchCalled;
                    }
                };
                return ps;
            }

        };
        try {
            query.execute();
            fail();
        } catch (OptimisticLockException expected) {
            expected.printStackTrace();
            assertSame(entities.get(1), expected.getEntity());
        }
    }

    /**
     * @throws Exception
     */
    @Test
    void testOptimisticLock_includesVersion() throws Exception {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchDeleteImpl<Eee> query = new AutoBatchDeleteImpl<Eee>(manager,
                entities) {

            
            @Override
            protected PreparedStatement getPreparedStatement(
                    JdbcContext jdbcContext) {
                MockPreparedStatement ps = new MockPreparedStatement(null, null) {

                    
                    @Override
                    public int[] executeBatch() throws SQLException {
                        return new int[] { 1, 0, 1 };
                    }

                    
                    @Override
                    public void addBatch() throws SQLException {
                        ++addBatchCalled;
                    }
                };
                return ps;
            }

        };
        query.ignoreVersion();
        int[] result = query.execute();
        assertEquals(3, addBatchCalled);
        assertEquals(3, result.length);
        assertEquals(1, result[0]);
        assertEquals(0, result[1]);
        assertEquals(1, result[2]);
    }

    /**
     * @throws Exception
     */
    @Test
    void testOptimisticLock_suppressOptimisticLockException()
            throws Exception {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchDeleteImpl<Eee> query = new AutoBatchDeleteImpl<Eee>(manager,
                entities) {

            
            @Override
            protected PreparedStatement getPreparedStatement(
                    JdbcContext jdbcContext) {
                MockPreparedStatement ps = new MockPreparedStatement(null, null) {

                    
                    @Override
                    public int[] executeBatch() throws SQLException {
                        return new int[] { 1, 0, 1 };
                    }

                    
                    @Override
                    public void addBatch() throws SQLException {
                        ++addBatchCalled;
                    }
                };
                return ps;
            }

        };
        int[] result = query.suppresOptimisticLockException().execute();
        assertEquals(3, result.length);
        assertEquals(1, result[0]);
        assertEquals(0, result[1]);
        assertEquals(1, result[2]);
    }

}
