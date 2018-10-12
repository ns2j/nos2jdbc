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

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.ResultSetHandler;
import org.seasar.extension.jdbc.SqlLog;
import org.seasar.extension.jdbc.SqlLogRegistry;
import org.seasar.extension.jdbc.SqlLogRegistryLocator;
import org.seasar.extension.jdbc.dialect.PostgreDialect;
import org.seasar.extension.jdbc.dialect.StandardDialect;
import org.seasar.extension.jdbc.dto.AaaDto;
import org.seasar.extension.jdbc.entity.Aaa;
import org.seasar.extension.jdbc.exception.QueryTwiceExecutionRuntimeException;
import org.seasar.extension.jdbc.exception.SNonUniqueResultException;
import org.seasar.extension.jdbc.manager.JdbcManagerImpl;
import org.seasar.extension.jdbc.types.ValueTypes;
import org.seasar.extension.jta.TransactionManagerImpl;
import org.seasar.extension.jta.TransactionSynchronizationRegistryImpl;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;
import org.seasar.framework.exception.SQLRuntimeException;
import org.seasar.framework.mock.sql.MockColumnMetaData;
import org.seasar.framework.mock.sql.MockDataSource;
import org.seasar.framework.mock.sql.MockResultSet;
import org.seasar.framework.mock.sql.MockResultSetMetaData;
import org.seasar.framework.util.ArrayMap;

import static org.seasar.extension.jdbc.parameter.Parameter.*;

/**
 * @author higa
 * 
 */
class SqlSelectImplTest {

    private JdbcManagerImpl manager;

    
    @BeforeEach
    void setUp() throws Exception {
        manager = new JdbcManagerImpl();
        manager.setSyncRegistry(new TransactionSynchronizationRegistryImpl(
                new TransactionManagerImpl()));
        manager.setDataSource(new MockDataSource());
        manager.setDialect(new StandardDialect());
        manager.setPersistenceConvention(new PersistenceConventionImpl());
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
        SqlSelectImpl<Aaa> query = new SqlSelectImpl<Aaa>(manager, Aaa.class,
                "select * from aaa");
        assertSame(query, query.callerClass(getClass()));
        assertEquals(getClass(), query.callerClass);
    }

    /**
     * 
     */
    @Test
    void testCallerMethodName() {
        SqlSelectImpl<Aaa> query = new SqlSelectImpl<Aaa>(manager, Aaa.class,
                "select * from aaa");
        assertSame(query, query.callerMethodName("hoge"));
        assertEquals("hoge", query.callerMethodName);
    }

    /**
     * 
     */
    @Test
    void testMaxRows() {
        SqlSelectImpl<Aaa> query = new SqlSelectImpl<Aaa>(manager, Aaa.class,
                "select * from aaa");
        assertSame(query, query.maxRows(100));
        assertEquals(100, query.maxRows);
    }

    /**
     * 
     */
    @Test
    void testFetchSize() {
        SqlSelectImpl<Aaa> query = new SqlSelectImpl<Aaa>(manager, Aaa.class,
                "select * from aaa");
        assertSame(query, query.fetchSize(100));
        assertEquals(100, query.fetchSize);
    }

    /**
     * 
     */
    @Test
    void testQueryTimeout() {
        SqlSelectImpl<Aaa> query = new SqlSelectImpl<Aaa>(manager, Aaa.class,
                "select * from aaa");
        assertSame(query, query.queryTimeout(100));
        assertEquals(100, query.queryTimeout);
    }

    /**
     * 
     */
    @Test
    void testLimit() {
        SqlSelectImpl<Aaa> query = new SqlSelectImpl<Aaa>(manager, Aaa.class,
                "select * from aaa");
        assertSame(query, query.limit(100));
        assertEquals(100, query.limit);
    }

    /**
     * 
     */
    @Test
    void testOffset() {
        SqlSelectImpl<Aaa> query = new SqlSelectImpl<Aaa>(manager, Aaa.class,
                "select * from aaa");
        assertSame(query, query.offset(100));
        assertEquals(100, query.offset);
    }

    /**
     * 
     */
    @Test
    void testPrepare_executedSql() {
        manager.setDialect(new PostgreDialect());
        SqlSelectImpl<Aaa> query = new SqlSelectImpl<Aaa>(manager, Aaa.class,
                "select * from aaa");
        query.limit(10);
        query.prepare("getResultList");
        assertEquals("select * from aaa limit 10", query.executedSql);
    }

    /**
     * 
     */
    @Test
    void testConstructor_nullPointer() {
        try {
            new SqlSelectImpl<AaaDto>(manager, AaaDto.class,
                    "select foo2, aaa_bbb from hoge where aaa = ?",
                    (Object[]) null);
            fail();
        } catch (NullPointerException e) {
            System.out.println(e);
        }
    }

    /**
     * 
     */
    @Test
    void testPrepare_nullBindVariable2() {
        try {
            new SqlSelectImpl<AaaDto>(manager, AaaDto.class,
                    "select foo2, aaa_bbb from hoge where aaa = ?",
                    (Object) null);
            fail();
        } catch (NullPointerException e) {
            System.out.println(e);
        }
    }

    /**
     * 
     */
    @Test
    void testGetResultList() {
        SqlSelectImpl<AaaDto> query = new SqlSelectImpl<AaaDto>(manager,
                AaaDto.class, "select foo2, aaa_bbb from hoge") {

            
            protected Object processResultSet(final JdbcContext jdbcContext,
                    final ResultSetHandler handler) {
                try {
                    MockResultSetMetaData rsMeta = new MockResultSetMetaData();
                    MockColumnMetaData columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("FOO2");
                    rsMeta.addColumnMetaData(columnMeta);
                    columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("AAA_BBB");
                    rsMeta.addColumnMetaData(columnMeta);
                    MockResultSet rs = new MockResultSet(rsMeta);
                    ArrayMap data = new ArrayMap();
                    data.put("FOO2", "111");
                    data.put("AAA_BBB", "222");
                    rs.addRowData(data);
                    return handler.handle(rs);
                } catch (SQLException e) {
                    throw new SQLRuntimeException(e);
                }
            }

        };
        List<AaaDto> ret = query.getResultList();
        assertEquals(1, ret.size());
        AaaDto dto = ret.get(0);
        assertEquals("111", dto.foo);
        assertEquals("222", dto.aaaBbb);
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals("select foo2, aaa_bbb from hoge", sqlLog.getCompleteSql());

        try {
            query.getResultList();
            fail();
        } catch (QueryTwiceExecutionRuntimeException expected) {
        }
    }

    /**
     * 
     */
    @Test
    void testGetResultList_prepare() {
        SqlSelectImpl<AaaDto> query = new SqlSelectImpl<AaaDto>(manager,
                AaaDto.class, "select foo2, aaa_bbb from hoge") {

            
            protected Object processResultSet(final JdbcContext jdbcContext,
                    final ResultSetHandler handler) {
                try {
                    MockResultSetMetaData rsMeta = new MockResultSetMetaData();
                    MockColumnMetaData columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("FOO2");
                    rsMeta.addColumnMetaData(columnMeta);
                    columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("AAA_BBB");
                    rsMeta.addColumnMetaData(columnMeta);
                    MockResultSet rs = new MockResultSet(rsMeta);
                    ArrayMap data = new ArrayMap();
                    data.put("FOO2", "111");
                    data.put("AAA_BBB", "222");
                    rs.addRowData(data);
                    return handler.handle(rs);
                } catch (SQLException e) {
                    throw new SQLRuntimeException(e);
                }
            }

        };
        query.getResultList();
        assertEquals("getResultList", query.getCallerMethodName());
    }

    /**
     * 
     */
    @Test
    void testGetResultList_paging() {
        manager.setDialect(new PostgreDialect());
        SqlSelectImpl<AaaDto> query = new SqlSelectImpl<AaaDto>(manager,
                AaaDto.class, "select foo2, aaa_bbb from hoge") {

            
            protected Object processResultSet(final JdbcContext jdbcContext,
                    final ResultSetHandler handler) {
                try {
                    MockResultSetMetaData rsMeta = new MockResultSetMetaData();
                    MockColumnMetaData columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("FOO2");
                    rsMeta.addColumnMetaData(columnMeta);
                    columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("AAA_BBB");
                    rsMeta.addColumnMetaData(columnMeta);
                    MockResultSet rs = new MockResultSet(rsMeta);
                    ArrayMap data = new ArrayMap();
                    data.put("FOO2", "111");
                    data.put("AAA_BBB", "222");
                    rs.addRowData(data);
                    return handler.handle(rs);
                } catch (SQLException e) {
                    throw new SQLRuntimeException(e);
                }
            }

        };
        List<AaaDto> ret = query.limit(10).offset(5).getResultList();
        assertEquals(1, ret.size());
        AaaDto dto = ret.get(0);
        assertEquals("111", dto.foo);
        assertEquals("222", dto.aaaBbb);
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals("select foo2, aaa_bbb from hoge limit 10 offset 5", sqlLog
                .getCompleteSql());
    }

    /**
     * 
     */
    @Test
    void testGetResultList_parameters() {
        SqlSelectImpl<AaaDto> query = new SqlSelectImpl<AaaDto>(manager,
                AaaDto.class,
                "select foo2, aaa_bbb from hoge where aaa = ? and bbb = ?",
                "111", "222") {

            
            protected Object processResultSet(final JdbcContext jdbcContext,
                    final ResultSetHandler handler) {
                try {
                    MockResultSetMetaData rsMeta = new MockResultSetMetaData();
                    MockColumnMetaData columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("FOO2");
                    rsMeta.addColumnMetaData(columnMeta);
                    columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("AAA_BBB");
                    rsMeta.addColumnMetaData(columnMeta);
                    MockResultSet rs = new MockResultSet(rsMeta);
                    ArrayMap data = new ArrayMap();
                    data.put("FOO2", "111");
                    data.put("AAA_BBB", "222");
                    rs.addRowData(data);
                    return handler.handle(rs);
                } catch (SQLException e) {
                    throw new SQLRuntimeException(e);
                }
            }
        };
        List<AaaDto> ret = query.getResultList();
        assertEquals(1, ret.size());
        AaaDto dto = ret.get(0);
        assertEquals("111", dto.foo);
        assertEquals("222", dto.aaaBbb);
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals(
                "select foo2, aaa_bbb from hoge where aaa = '111' and bbb = '222'",
                sqlLog.getCompleteSql());
    }

    /**
     * 
     */
    @Test
    void testGetResultList_parameters_paging() {
        manager.setDialect(new PostgreDialect());
        SqlSelectImpl<AaaDto> query = new SqlSelectImpl<AaaDto>(manager,
                AaaDto.class,
                "select foo2, aaa_bbb from hoge where aaa = ? and bbb = ?",
                "111", "222") {

            
            protected Object processResultSet(final JdbcContext jdbcContext,
                    final ResultSetHandler handler) {
                try {
                    MockResultSetMetaData rsMeta = new MockResultSetMetaData();
                    MockColumnMetaData columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("FOO2");
                    rsMeta.addColumnMetaData(columnMeta);
                    columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("AAA_BBB");
                    rsMeta.addColumnMetaData(columnMeta);
                    MockResultSet rs = new MockResultSet(rsMeta);
                    ArrayMap data = new ArrayMap();
                    data.put("FOO2", "111");
                    data.put("AAA_BBB", "222");
                    rs.addRowData(data);
                    return handler.handle(rs);
                } catch (SQLException e) {
                    throw new SQLRuntimeException(e);
                }
            }
        };
        List<AaaDto> ret = query.limit(10).offset(5).getResultList();
        assertEquals(1, ret.size());
        AaaDto dto = ret.get(0);
        assertEquals("111", dto.foo);
        assertEquals("222", dto.aaaBbb);
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals(
                "select foo2, aaa_bbb from hoge where aaa = '111' and bbb = '222' limit 10 offset 5",
                sqlLog.getCompleteSql());
    }

    /**
     * 
     */
    @Test
    void testGetSingleResult() {
        SqlSelectImpl<AaaDto> query = new SqlSelectImpl<AaaDto>(manager,
                AaaDto.class, "select foo2, aaa_bbb from hoge") {

            
            protected Object processResultSet(final JdbcContext jdbcContext,
                    final ResultSetHandler handler) {
                try {
                    MockResultSetMetaData rsMeta = new MockResultSetMetaData();
                    MockColumnMetaData columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("FOO2");
                    rsMeta.addColumnMetaData(columnMeta);
                    columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("AAA_BBB");
                    rsMeta.addColumnMetaData(columnMeta);
                    MockResultSet rs = new MockResultSet(rsMeta);
                    ArrayMap data = new ArrayMap();
                    data.put("FOO2", "111");
                    data.put("AAA_BBB", "222");
                    rs.addRowData(data);
                    return handler.handle(rs);
                } catch (SQLException e) {
                    throw new SQLRuntimeException(e);
                }
            }

        };
        AaaDto dto = query.getSingleResult();
        assertNotNull(dto);
        assertEquals("111", dto.foo);
        assertEquals("222", dto.aaaBbb);
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals("select foo2, aaa_bbb from hoge", sqlLog.getCompleteSql());

        try {
            query.getSingleResult();
            fail();
        } catch (QueryTwiceExecutionRuntimeException expected) {
        }
    }

    /**
     * 
     */
    @Test
    void testGetSingleResult_prepare() {
        SqlSelectImpl<AaaDto> query = new SqlSelectImpl<AaaDto>(manager,
                AaaDto.class, "select foo2, aaa_bbb from hoge") {

            
            protected Object processResultSet(final JdbcContext jdbcContext,
                    final ResultSetHandler handler) {
                try {
                    MockResultSetMetaData rsMeta = new MockResultSetMetaData();
                    MockColumnMetaData columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("FOO2");
                    rsMeta.addColumnMetaData(columnMeta);
                    columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("AAA_BBB");
                    rsMeta.addColumnMetaData(columnMeta);
                    MockResultSet rs = new MockResultSet(rsMeta);
                    ArrayMap data = new ArrayMap();
                    data.put("FOO2", "111");
                    data.put("AAA_BBB", "222");
                    rs.addRowData(data);
                    return handler.handle(rs);
                } catch (SQLException e) {
                    throw new SQLRuntimeException(e);
                }
            }

        };
        query.getSingleResult();
        assertEquals("getSingleResult", query.getCallerMethodName());
    }

    /**
     * 
     */
    @Test
    void testGetSingleResult_paging() {
        manager.setDialect(new PostgreDialect());
        SqlSelectImpl<AaaDto> query = new SqlSelectImpl<AaaDto>(manager,
                AaaDto.class, "select foo2, aaa_bbb from hoge") {

            
            protected Object processResultSet(final JdbcContext jdbcContext,
                    final ResultSetHandler handler) {
                try {
                    MockResultSetMetaData rsMeta = new MockResultSetMetaData();
                    MockColumnMetaData columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("FOO2");
                    rsMeta.addColumnMetaData(columnMeta);
                    columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("AAA_BBB");
                    rsMeta.addColumnMetaData(columnMeta);
                    MockResultSet rs = new MockResultSet(rsMeta);
                    ArrayMap data = new ArrayMap();
                    data.put("FOO2", "111");
                    data.put("AAA_BBB", "222");
                    rs.addRowData(data);
                    return handler.handle(rs);
                } catch (SQLException e) {
                    throw new SQLRuntimeException(e);
                }
            }

        };
        AaaDto dto = query.limit(10).offset(5).getSingleResult();
        assertNotNull(dto);
        assertEquals("111", dto.foo);
        assertEquals("222", dto.aaaBbb);
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals("select foo2, aaa_bbb from hoge limit 10 offset 5", sqlLog
                .getCompleteSql());
    }

    /**
     * 
     */
    @Test
    void testGetSingleResult_simpleType() {
        SqlSelectImpl<Integer> query = new SqlSelectImpl<Integer>(manager,
                Integer.class, "select count(*) as cnt from aaa") {

            
            protected Object processResultSet(final JdbcContext jdbcContext,
                    final ResultSetHandler handler) {
                try {
                    MockResultSetMetaData rsMeta = new MockResultSetMetaData();
                    MockColumnMetaData columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("CNT");
                    rsMeta.addColumnMetaData(columnMeta);
                    MockResultSet rs = new MockResultSet(rsMeta);
                    ArrayMap data = new ArrayMap();
                    data.put("CNT", 5);
                    rs.addRowData(data);
                    return handler.handle(rs);
                } catch (SQLException e) {
                    throw new SQLRuntimeException(e);
                }
            }

        };
        Integer count = query.getSingleResult();
        assertEquals(5, count.intValue());
    }

    /**
     * 
     */
    @Test
    void testGetSingleResult_nodata() {
        SqlSelectImpl<AaaDto> query = new SqlSelectImpl<AaaDto>(manager,
                AaaDto.class, "select foo2, aaa_bbb from hoge") {

            
            protected Object processResultSet(final JdbcContext jdbcContext,
                    final ResultSetHandler handler) {
                try {
                    MockResultSetMetaData rsMeta = new MockResultSetMetaData();
                    MockColumnMetaData columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("FOO2");
                    rsMeta.addColumnMetaData(columnMeta);
                    columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("AAA_BBB");
                    rsMeta.addColumnMetaData(columnMeta);
                    return handler.handle(new MockResultSet(rsMeta));
                } catch (SQLException e) {
                    throw new SQLRuntimeException(e);
                }
            }

        };
        AaaDto dto = query.getSingleResult();
        assertNull(dto);
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals("select foo2, aaa_bbb from hoge", sqlLog.getCompleteSql());
    }

    /**
     * 
     */
    @Test
    void testGetSingleResult_nonunique() {
        SqlSelectImpl<AaaDto> query = new SqlSelectImpl<AaaDto>(manager,
                AaaDto.class, "select foo2, aaa_bbb from hoge where aaa = ?") {

            
            protected Object processResultSet(final JdbcContext jdbcContext,
                    final ResultSetHandler handler) {
                try {
                    MockResultSetMetaData rsMeta = new MockResultSetMetaData();
                    MockColumnMetaData columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("FOO2");
                    rsMeta.addColumnMetaData(columnMeta);
                    columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("AAA_BBB");
                    rsMeta.addColumnMetaData(columnMeta);
                    MockResultSet rs = new MockResultSet(rsMeta);
                    ArrayMap data = new ArrayMap();
                    data.put("FOO2", "111");
                    data.put("AAA_BBB", "222");
                    rs.addRowData(data);
                    data = new ArrayMap();
                    data.put("FOO2", "111x");
                    data.put("AAA_BBB", "222x");
                    rs.addRowData(data);
                    return handler.handle(rs);
                } catch (SQLException e) {
                    throw new SQLRuntimeException(e);
                }
            }

        };
        try {
            query.getSingleResult();
            fail();
        } catch (SNonUniqueResultException e) {
            System.out.println(e);
        }
    }

    /**
     * 
     */
    @Test
    void testGetSingleResult_parameters() {
        SqlSelectImpl<AaaDto> query = new SqlSelectImpl<AaaDto>(manager,
                AaaDto.class,
                "select foo2, aaa_bbb from hoge where aaa = ? and bbb = ?",
                "111", "222") {

            
            protected Object processResultSet(final JdbcContext jdbcContext,
                    final ResultSetHandler handler) {
                try {
                    MockResultSetMetaData rsMeta = new MockResultSetMetaData();
                    MockColumnMetaData columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("FOO2");
                    rsMeta.addColumnMetaData(columnMeta);
                    columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("AAA_BBB");
                    rsMeta.addColumnMetaData(columnMeta);
                    MockResultSet rs = new MockResultSet(rsMeta);
                    ArrayMap data = new ArrayMap();
                    data.put("FOO2", "111");
                    data.put("AAA_BBB", "222");
                    rs.addRowData(data);
                    return handler.handle(rs);
                } catch (SQLException e) {
                    throw new SQLRuntimeException(e);
                }
            }
        };
        AaaDto dto = query.getSingleResult();
        assertNotNull(dto);
        assertEquals("111", dto.foo);
        assertEquals("222", dto.aaaBbb);
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals(
                "select foo2, aaa_bbb from hoge where aaa = '111' and bbb = '222'",
                sqlLog.getCompleteSql());
    }

    /**
     * 
     */
    @Test
    void testGetSingleResult_parameters_paging() {
        manager.setDialect(new PostgreDialect());
        SqlSelectImpl<AaaDto> query = new SqlSelectImpl<AaaDto>(manager,
                AaaDto.class,
                "select foo2, aaa_bbb from hoge where aaa = ? and bbb = ?",
                "111", "222") {

            
            protected Object processResultSet(final JdbcContext jdbcContext,
                    final ResultSetHandler handler) {
                try {
                    MockResultSetMetaData rsMeta = new MockResultSetMetaData();
                    MockColumnMetaData columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("FOO2");
                    rsMeta.addColumnMetaData(columnMeta);
                    columnMeta = new MockColumnMetaData();
                    columnMeta.setColumnLabel("AAA_BBB");
                    rsMeta.addColumnMetaData(columnMeta);
                    MockResultSet rs = new MockResultSet(rsMeta);
                    ArrayMap data = new ArrayMap();
                    data.put("FOO2", "111");
                    data.put("AAA_BBB", "222");
                    rs.addRowData(data);
                    return handler.handle(rs);
                } catch (SQLException e) {
                    throw new SQLRuntimeException(e);
                }
            }
        };
        AaaDto dto = query.limit(10).offset(5).getSingleResult();
        assertNotNull(dto);
        assertEquals("111", dto.foo);
        assertEquals("222", dto.aaaBbb);
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals(
                "select foo2, aaa_bbb from hoge where aaa = '111' and bbb = '222' limit 10 offset 5",
                sqlLog.getCompleteSql());
    }

    /**
     * 
     */
    @Test
    void testPrepareSql() {
        SqlSelectImpl<Aaa> query = new SqlSelectImpl<Aaa>(manager, Aaa.class,
                "select * from aaa");
        query.prepare("getResultList");
        query.prepareSql();
        assertNotNull(query.executedSql);
    }

    /**
     * 
     */
    @Test
    void testPrepareSql_getCount() {
        SqlSelectImpl<Long> query = new SqlSelectImpl<Long>(manager,
                Long.class, "select * from aaa");
        query.count = true;
        query.prepare("getResultList");
        query.prepareSql();
        assertEquals("select count(*) from ( select * from aaa ) COUNT_",
                query.executedSql);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    void testParams_valueType() throws Exception {
        SqlSelectImpl<Aaa> query = new SqlSelectImpl<Aaa>(manager, Aaa.class,
                "select * from aaa where bbb = ? and ccc = ? and ddd = ?",
                "hoge", lob("foo"), time(new Date()));
        assertEquals(3, query.getParamSize());
        assertEquals(ValueTypes.STRING, query.getParam(0).valueType);
        assertEquals(ValueTypes.CLOB, query.getParam(1).valueType);
        assertEquals(ValueTypes.DATE_TIME, query.getParam(2).valueType);
    }
}
