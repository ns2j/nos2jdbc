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

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.ParamType;
import org.seasar.extension.jdbc.SqlLog;
import org.seasar.extension.jdbc.SqlLogRegistry;
import org.seasar.extension.jdbc.SqlLogRegistryLocator;
import org.seasar.extension.jdbc.annotation.InOut;
import org.seasar.extension.jdbc.annotation.Out;
import org.seasar.extension.jdbc.annotation.ResultSet;
import org.seasar.extension.jdbc.dialect.StandardDialect;
import org.seasar.extension.jdbc.entity.Aaa;
import org.seasar.extension.jdbc.exception.QueryTwiceExecutionRuntimeException;
import org.seasar.extension.jdbc.manager.JdbcManagerImpl;
import org.seasar.extension.jdbc.types.ValueTypes;
import org.seasar.extension.jta.TransactionManagerImpl;
import org.seasar.extension.jta.TransactionSynchronizationRegistryImpl;
import org.seasar.framework.mock.sql.MockCallableStatement;
import org.seasar.framework.mock.sql.MockDataSource;

import jakarta.persistence.Lob;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 * @author koichik
 */
class AutoFunctionCallImplTest {

    private JdbcManagerImpl manager;

    
    @BeforeEach
    void setUp() throws Exception {
        manager = new JdbcManagerImpl();
        manager.setSyncRegistry(new TransactionSynchronizationRegistryImpl(
                new TransactionManagerImpl()));
        manager.setDataSource(new MockDataSource());
        manager.setDialect(new StandardDialect());
    }

    
    @AfterEach
    void tearDown() throws Exception {
        SqlLogRegistry regisry = SqlLogRegistryLocator.getInstance();
        regisry.clear();
        manager = null;
    }

    /**
     * @throws Exception
     */
    @Test
    void testPrepareReturnParameter_simpleType() throws Exception {
        AutoFunctionCallImpl<Integer> query = new AutoFunctionCallImpl<Integer>(
                manager, Integer.class, "hoge");
        query.prepare("getSingleResult");
        assertEquals("{? = call hoge()}", query.executedSql);
        assertEquals(1, query.getParamSize());
        assertNull(query.getParam(0).value);
        assertEquals(Integer.class, query.getParam(0).paramClass);
        assertEquals(ValueTypes.INTEGER, query.getParam(0).valueType);
    }

    /**
     * @throws Exception
     */
    @Test
    void testPrepareReturnParameter_simpleType_temporalType()
            throws Exception {
        AutoFunctionCallImpl<Calendar> query = (AutoFunctionCallImpl<Calendar>) new AutoFunctionCallImpl<Calendar>(
                manager, Calendar.class, "hoge").temporal(TemporalType.TIME);
        query.prepare("getSingleResult");
        assertEquals("{? = call hoge()}", query.executedSql);
        assertEquals(1, query.getParamSize());
        assertNull(query.getParam(0).value);
        assertEquals(Calendar.class, query.getParam(0).paramClass);
        assertEquals(ValueTypes.CALENDAR_TIME, query.getParam(0).valueType);
    }

    /**
     * @throws Exception
     */
    @Test
    void testPrepareReturnParameter_simpleList() throws Exception {
        AutoFunctionCallImpl<Integer> query = new AutoFunctionCallImpl<Integer>(
                manager, Integer.class, "hoge");
        query.resultList = true;
        query.prepare("getResultList");
        assertEquals("{? = call hoge()}", query.executedSql);
        assertEquals(1, query.getParamSize());
        assertNull(query.getParam(0).value);
        assertEquals(Integer.class, query.getParam(0).paramClass);
        assertEquals(ValueTypes.OBJECT, query.getParam(0).valueType);
    }

    /**
     * @throws Exception
     */
    @Test
    void testPrepareReturnParameter_dtoList() throws Exception {
        AutoFunctionCallImpl<MyDto3> query = new AutoFunctionCallImpl<MyDto3>(
                manager, MyDto3.class, "hoge");
        query.resultList = true;
        query.prepare("getResultList");
        assertEquals("{? = call hoge()}", query.executedSql);
        assertEquals(1, query.getParamSize());
        assertNull(query.getParam(0).value);
        assertEquals(MyDto3.class, query.getParam(0).paramClass);
        assertEquals(ValueTypes.OBJECT, query.getParam(0).valueType);
    }

    /**
     * @throws Exception
     */
    @Test
    void testPrepareParameter_simpleType() throws Exception {
        AutoFunctionCallImpl<String> query = new AutoFunctionCallImpl<String>(
                manager, String.class, "hoge", 1);
        query.prepare("getSingleResult");
        assertEquals("{? = call hoge(?)}", query.executedSql);
        assertEquals(2, query.getParamSize());
        assertNull(query.getParam(0).value);
        assertEquals(String.class, query.getParam(0).paramClass);
        assertEquals(ValueTypes.STRING, query.getParam(0).valueType);

        assertEquals(1, query.getParam(1).value);
        assertEquals(Integer.class, query.getParam(1).paramClass);
        assertEquals(ValueTypes.INTEGER, query.getParam(1).valueType);
    }

    /**
     * @throws Exception
     */
    @Test
    void testPrepareParameter_dto() throws Exception {
        MyDto dto = new MyDto();
        dto.arg1 = "aaa";
        dto.arg2 = "bbb";
        AutoFunctionCallImpl<Integer> query = new AutoFunctionCallImpl<Integer>(
                manager, Integer.class, "hoge", dto);
        query.prepare("getSingleResult");
        assertEquals("{? = call hoge(?, ?, ?)}", query.executedSql);
        assertEquals(4, query.getParamSize());
        assertEquals(null, query.getParam(0).value);
        assertEquals(Integer.class, query.getParam(0).paramClass);
        assertEquals(ValueTypes.INTEGER, query.getParam(0).valueType);
        assertEquals(ParamType.OUT, query.getParam(0).paramType);
        assertNull(query.getParam(0).field);

        assertEquals("aaa", query.getParam(1).value);
        assertEquals(String.class, query.getParam(1).paramClass);
        assertEquals(ValueTypes.STRING, query.getParam(1).valueType);
        assertEquals(ParamType.IN_OUT, query.getParam(1).paramType);
        assertEquals(MyDto.class.getDeclaredField("arg1"),
                query.getParam(1).field);

        assertEquals("bbb", query.getParam(2).value);
        assertEquals(String.class, query.getParam(2).paramClass);
        assertEquals(ValueTypes.STRING, query.getParam(2).valueType);
        assertEquals(ParamType.IN, query.getParam(2).paramType);
        assertNull(query.getParam(2).field);

        assertEquals(null, query.getParam(3).value);
        assertEquals(String.class, query.getParam(3).paramClass);
        assertEquals(ValueTypes.STRING, query.getParam(3).valueType);
        assertEquals(ParamType.OUT, query.getParam(3).paramType);
        assertEquals(MyDto.class.getDeclaredField("arg3"),
                query.getParam(3).field);
    }

    /**
     * @throws Exception
     */
    @Test
    void testPrepareParameter_resultSet() throws Exception {
        MyDto2 dto = new MyDto2();
        AutoFunctionCallImpl<Integer> query = new AutoFunctionCallImpl<Integer>(
                manager, Integer.class, "hoge", dto);
        query.prepare("getSingleResult");
        assertEquals("{? = call hoge()}", query.executedSql);
        assertEquals(1, query.getParamSize());
        assertEquals(null, query.getParam(0).value);
        assertEquals(Integer.class, query.getParam(0).paramClass);
        assertEquals(ValueTypes.INTEGER, query.getParam(0).valueType);
        assertEquals(ParamType.OUT, query.getParam(0).paramType);
        assertNull(query.getParam(0).field);

        assertEquals(1, query.getNonParamSize());
        assertEquals(ParamType.OUT, query.getNonParam(0).paramType);
        assertEquals(MyDto2.class.getDeclaredField("arg1"), query
                .getNonParam(0).field);
    }

    /**
     * @throws Exception
     */
    @Test
    void testPrepareParameter_clob() throws Exception {
        MyDto3 dto = new MyDto3();
        dto.largeName = "aaa";
        AutoFunctionCallImpl<Integer> query = new AutoFunctionCallImpl<Integer>(
                manager, Integer.class, "hoge", dto);
        query.prepare("getSingleResult");
        assertEquals("{? = call hoge(?)}", query.executedSql);
        assertEquals(2, query.getParamSize());
        assertEquals(null, query.getParam(0).value);
        assertEquals(Integer.class, query.getParam(0).paramClass);
        assertEquals(ValueTypes.INTEGER, query.getParam(0).valueType);
        assertEquals(ParamType.OUT, query.getParam(0).paramType);
        assertNull(query.getParam(0).field);

        assertEquals("aaa", query.getParam(1).value);
        assertEquals(ParamType.IN, query.getParam(1).paramType);
        assertEquals(ValueTypes.CLOB, query.getParam(1).valueType);
    }

    /**
     * @throws Exception
     */
    @Test
    void testPrepareParameter_temporalType() throws Exception {
        MyDto4 dto = new MyDto4();
        Calendar calendar = Calendar.getInstance();
        dto.time = calendar;
        AutoFunctionCallImpl<Integer> query = new AutoFunctionCallImpl<Integer>(
                manager, Integer.class, "hoge", dto);
        query.prepare("getSingleResult");
        assertEquals("{? = call hoge(?)}", query.executedSql);
        assertEquals(2, query.getParamSize());
        assertEquals(null, query.getParam(0).value);
        assertEquals(Integer.class, query.getParam(0).paramClass);
        assertEquals(ValueTypes.INTEGER, query.getParam(0).valueType);
        assertEquals(ParamType.OUT, query.getParam(0).paramType);
        assertNull(query.getParam(0).field);

        assertEquals(calendar, query.getParam(1).value);
        assertEquals(ParamType.IN, query.getParam(1).paramType);
        assertEquals(ValueTypes.CALENDAR_TIME, query.getParam(1).valueType);
    }

    /**
     * @throws Exception
     */
    @Test
    void testCall() throws Exception {
        MyDto dto = new MyDto();
        dto.arg1 = "aaa";
        dto.arg2 = "bbb";
        AutoFunctionCallImpl<String> query = new AutoFunctionCallImpl<String>(
                manager, String.class, "hoge", dto) {

            
            @Override
            protected CallableStatement getCallableStatement(
                    JdbcContext jdbcContext) {
                MockCallableStatement cs = new MockCallableStatement(null, null) {

                    
                    @Override
                    public String getString(int parameterIndex)
                            throws SQLException {
                        return "aaa" + parameterIndex;
                    }
                };
                return cs;
            }
        };

        String result = query.getSingleResult();
        assertEquals("{? = call hoge(?, ?, ?)}", query.executedSql);
        assertEquals("aaa1", result);
        assertEquals("aaa2", dto.arg1);
        assertEquals("aaa4", dto.arg3);
        SqlLog sqlLog = SqlLogRegistryLocator.getInstance().getLast();
        assertEquals("{null = call hoge('aaa', 'bbb', null)}", sqlLog
                .getCompleteSql());

        try {
            query.getSingleResult();
            fail();
        } catch (QueryTwiceExecutionRuntimeException expected) {
        }
    }

    private static final class MyDto {

        /** */
        @InOut
        public String arg1;

        /** */
        public String arg2;

        /** */
        @Out
        public String arg3;

    }

    private static final class MyDto2 {

        /** */
        @ResultSet
        public List<Aaa> arg1;

    }

    private static final class MyDto3 {

        /** */
        @Lob
        public String largeName;

    }

    private static final class MyDto4 {

        /** */
        @Temporal(TemporalType.TIME)
        public Calendar time;

    }

}
