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
import org.seasar.extension.jdbc.types.StringClobType;
import org.seasar.extension.jta.TransactionManagerImpl;
import org.seasar.extension.jta.TransactionSynchronizationRegistryImpl;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;
import org.seasar.framework.mock.sql.MockDataSource;
import org.seasar.framework.mock.sql.MockPreparedStatement;

import jakarta.persistence.OptimisticLockException;

/**
 * @author koichik
 */
class AutoBatchUpdateTest {
    private JdbcManagerImpl manager;
    private int addBatchCalled;

    @BeforeEach
    void setUp() {
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
    void tearDown() {
        SqlLogRegistry regisry = SqlLogRegistryLocator.getInstance();
        regisry.clear();
        manager = null;
    }

    /**
     */
    @Test
    void callerClass() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities);
        assertSame(query, query.callerClass(getClass()));
        assertEquals(getClass(), query.callerClass);
    }

    /**
     */
    @Test
    void callerMethodName() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities);
        assertSame(query, query.callerMethodName("hoge"));
        assertEquals("hoge", query.callerMethodName);
    }

    /**
     */
    @Test
    void queryTimeout() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities);
        assertSame(query, query.queryTimeout(100));
        assertEquals(100, query.queryTimeout);
    }

    /**
     */
    @Test
    void includeVersion() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities);
        assertFalse(query.includeVersion);
        assertSame(query, query.includesVersion());
        assertTrue(query.includeVersion);
    }

    /**
     */
    @Test
    void incrementVersions() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities);
        query.incrementVersions();
        for (Eee e : entities) {
            assertEquals(Long.valueOf(1), e.version);
        }
    }

    /**
     */
    @Test
    void incrementVersions_includeVersion() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities);
        query.includesVersion();
        query.incrementVersions();
        for (Eee e : entities) {
            assertEquals(Long.valueOf(0), e.version);
        }
    }

    /**
     */
    @Test
    void includes() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities);
        assertTrue(query.excludesProperties.isEmpty());

        assertSame(query, query.includes("name"));
        assertEquals(1, query.includesProperties.size());
        assertTrue(query.includesProperties.contains("name"));
        assertFalse(query.includesProperties.contains("id"));
        assertFalse(query.includesProperties.contains("version"));

        assertSame(query, query.includes("id", "version"));
        assertEquals(3, query.includesProperties.size());
        assertTrue(query.includesProperties.contains("name"));
        assertTrue(query.includesProperties.contains("id"));
        assertTrue(query.includesProperties.contains("version"));
    }

    /**
     */
    @Test
    void excludes() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities);
        assertTrue(query.excludesProperties.isEmpty());

        assertSame(query, query.excludes("name"));
        assertEquals(1, query.excludesProperties.size());
        assertTrue(query.excludesProperties.contains("name"));
        assertFalse(query.excludesProperties.contains("id"));
        assertFalse(query.excludesProperties.contains("version"));

        assertSame(query, query.excludes("id", "version"));
        assertEquals(3, query.excludesProperties.size());
        assertTrue(query.excludesProperties.contains("name"));
        assertTrue(query.excludesProperties.contains("id"));
        assertTrue(query.excludesProperties.contains("version"));
    }

    /**
     */
    @Test
    void prepareTarget() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities);
        query.prepareTargetProperties();
        assertEquals(4, query.targetProperties.size());
        assertEquals("name", query.targetProperties.get(0).getName());
        assertEquals("longText", query.targetProperties.get(1).getName());
        assertEquals("fffId", query.targetProperties.get(2).getName());
    }

    /**
     */
    @Test
    void prepareTarget_includesVersion() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities);
        query.includesVersion();
        query.prepareTargetProperties();
        assertEquals(5, query.targetProperties.size());
        assertEquals("name", query.targetProperties.get(0).getName());
        assertEquals("longText", query.targetProperties.get(1).getName());
        assertEquals("fffId", query.targetProperties.get(2).getName());
        assertEquals("version", query.targetProperties.get(3).getName());
    }

    /**
     */
    @Test
    void prepareTarget_includes() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities);
        query.includes("name");
        query.prepareTargetProperties();
        assertEquals(1, query.targetProperties.size());
        assertEquals("name", query.targetProperties.get(0).getName());
    }

    /**
     */
    @Test
    void prepareTarget_excludes() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities);
        query.excludes("name");
        query.prepareTargetProperties();
        assertEquals(3, query.targetProperties.size());
        assertEquals("longText", query.targetProperties.get(0).getName());
        assertEquals("fffId", query.targetProperties.get(1).getName());
    }

    /**
     */
    @Test
    void prepareTarget_includesAndExcludes() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities);
        query.includes("name", "fffId").excludes("fffId");
        query.prepareTargetProperties();
        assertEquals(1, query.targetProperties.size());
        assertEquals("name", query.targetProperties.get(0).getName());
    }

    /**
     */
    @Test
    void prepareSetClause() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities);
        query.prepare("execute");
        assertEquals(" set NAME = ?, LONG_TEXT = ?, FFF_ID = ?, UPDATE_AT = ?, VERSION = VERSION + 1",
                query.setClause.toSql());
    }

    /**
     */
    @Test
    void prepareSetClause_includesVersion() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities);
        query.includesVersion();
        query.prepare("execute");
        assertEquals(" set NAME = ?, LONG_TEXT = ?, FFF_ID = ?, VERSION = ?, UPDATE_AT = ?",
                query.setClause.toSql());
    }

    /**
     */
    @Test
    void prepareWhereClause() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities);
        query.prepare("execute");
        assertEquals(" where ID = ? and VERSION = ?", query.whereClause.toSql());
    }

    /**
     */
    @Test
    void prepareWhereClause_inclduesVersion() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities);
        query.includesVersion();
        query.prepare("execute");
        assertEquals(" where ID = ?", query.whereClause.toSql());
    }

    /**
     */
    @Test
    void prepareSql() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities);
        query.prepare("execute");
        assertEquals(
                "update EEE set NAME = ?, LONG_TEXT = ?, FFF_ID = ?, UPDATE_AT = ?, VERSION = VERSION + 1 where ID = ? and VERSION = ?",
                query.executedSql);
    }

    /**
     */
    @Test
    void prepareSql_includesVersion() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities);
        query.includesVersion();
        query.prepare("execute");
        assertEquals(
                "update EEE set NAME = ?, LONG_TEXT = ?, FFF_ID = ?, VERSION = ?, UPDATE_AT = ? where ID = ?",
                query.executedSql);
    }

    /**
     */
    @Test
    void prepareParams() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities);
        query.prepare("execute");

        query.prepareParams(entities.get(0));
        assertEquals(6, query.getParamSize());
        assertEquals("foo", query.getParam(0).value);
        assertNull(query.getParam(1).value);
        assertTrue(query.getParam(1).valueType instanceof StringClobType);
        assertNull(query.getParam(2).value);
        assertEquals(Integer.valueOf(1), query.getParam(4).value);
        assertEquals(Long.valueOf(0L), query.getParam(5).value);
        query.resetParams();

        query.prepareParams(entities.get(1));
        assertEquals(6, query.getParamSize());
        assertEquals("bar", query.getParam(0).value);
        assertNull(query.getParam(1).value);
        assertTrue(query.getParam(1).valueType instanceof StringClobType);
        assertNull(query.getParam(2).value);
        assertEquals(Integer.valueOf(2), query.getParam(4).value);
        assertEquals(Long.valueOf(0L), query.getParam(5).value);
        query.resetParams();

        query.prepareParams(entities.get(2));
        assertEquals(6, query.getParamSize());
        assertEquals("baz", query.getParam(0).value);
        assertNull(query.getParam(1).value);
        assertTrue(query.getParam(1).valueType instanceof StringClobType);
        assertNull(query.getParam(2).value);
        assertEquals(Integer.valueOf(3), query.getParam(4).value);
        assertEquals(Long.valueOf(0L), query.getParam(5).value);
    }

    /**
     */
    @Test
    void prepareParams_includesVersion() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities);
        query.includesVersion();
        query.prepare("execute");

        query.prepareParams(entities.get(0));
        assertEquals(6, query.getParamSize());
        assertEquals("foo", query.getParam(0).value);
        assertNull(query.getParam(1).value);
        assertTrue(query.getParam(1).valueType instanceof StringClobType);
        assertNull(query.getParam(2).value);
        assertEquals(Long.valueOf(0L), query.getParam(3).value);
        assertEquals(Integer.valueOf(1), query.getParam(5).value);
        query.resetParams();

        query.prepareParams(entities.get(1));
        assertEquals(6, query.getParamSize());
        assertEquals("bar", query.getParam(0).value);
        assertNull(query.getParam(1).value);
        assertTrue(query.getParam(1).valueType instanceof StringClobType);
        assertNull(query.getParam(2).value);
        assertEquals(Long.valueOf(0L), query.getParam(3).value);
        assertEquals(Integer.valueOf(2), query.getParam(5).value);
        query.resetParams();

        query.prepareParams(entities.get(2));
        assertEquals(6, query.getParamSize());
        assertEquals("baz", query.getParam(0).value);
        assertNull(query.getParam(1).value);
        assertTrue(query.getParam(1).valueType instanceof StringClobType);
        assertNull(query.getParam(2).value);
        assertEquals(Long.valueOf(0L), query.getParam(3).value);
        assertEquals(Integer.valueOf(3), query.getParam(5).value);
    }

    /**
     */
    @Test
    void execute() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
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
        String actual = DateTimeStr.replace(sqlLog.getCompleteSql());
        assertEquals(
                "update EEE set NAME = 'baz', LONG_TEXT = null, FFF_ID = null, UPDATE_AT = '" + DateTimeStr.REPLACE + "', VERSION = VERSION + 1 where ID = 3 and VERSION = 0",
                actual);

        try {
            query.execute();
            fail();
        } catch (QueryTwiceExecutionRuntimeException expected) {
        }
    }

    /**
     */
    @Test
    void execute_includesVersion() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
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
        query.includesVersion();
        int[] result = query.execute();
        assertEquals(3, addBatchCalled);
        assertEquals(3, result.length);
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        String actual = DateTimeStr.replace(sqlLog.getCompleteSql());
        assertEquals(
                "update EEE set NAME = 'baz', LONG_TEXT = null, FFF_ID = null, VERSION = 0, UPDATE_AT = '" + DateTimeStr.REPLACE + "' where ID = 3",
                actual);
    }

    /**
     */
    @Test
    void optimisticLock() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities) {
            @Override
            protected PreparedStatement getPreparedStatement(
                    JdbcContext jdbcContext) {
                MockPreparedStatement ps = new MockPreparedStatement(null, null) {
                    @Override
                    public int[] executeBatch() throws SQLException {
                        return new int[] { 1, 1, 0 };
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
            assertSame(entities.get(2), expected.getEntity());
        }
    }

    /**
     */
    @Test
    void optimisticLock_includesVersion() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities) {
            @Override
            protected PreparedStatement getPreparedStatement(
                    JdbcContext jdbcContext) {
                MockPreparedStatement ps = new MockPreparedStatement(null, null) {
                    @Override
                    public int[] executeBatch() throws SQLException {
                        return new int[] { 1, 1, 0 };
                    }
                    @Override
                    public void addBatch() throws SQLException {
                        ++addBatchCalled;
                    }
                };
                return ps;
            }

        };
        query.includesVersion();
        int[] result = query.execute();
        assertEquals(3, addBatchCalled);
        assertEquals(3, result.length);
        assertEquals(1, result[0]);
        assertEquals(1, result[1]);
        assertEquals(0, result[2]);
        assertEquals(Long.valueOf(0), entities.get(0).version);
        assertEquals(Long.valueOf(0), entities.get(1).version);
        assertEquals(Long.valueOf(0), entities.get(2).version);
    }

    /**
     */
    @Test
    void optimisticLock_suppressOptimisticLockException() {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = new AutoBatchUpdateImpl<Eee>(manager,
                entities) {
            @Override
            protected PreparedStatement getPreparedStatement(
                    JdbcContext jdbcContext) {
                MockPreparedStatement ps = new MockPreparedStatement(null, null) {
                    @Override
                    public int[] executeBatch() throws SQLException {
                        return new int[] { 1, 1, 0 };
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
        assertEquals(1, result[1]);
        assertEquals(0, result[2]);
    }

}
