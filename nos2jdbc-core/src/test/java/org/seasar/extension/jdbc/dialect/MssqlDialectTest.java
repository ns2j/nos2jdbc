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
package org.seasar.extension.jdbc.dialect;

import org.junit.jupiter.api.*;
import org.seasar.extension.jdbc.exception.OrderByNotFoundRuntimeException;
import org.seasar.framework.exception.SQLRuntimeException;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

/**
 * @author higa
 * 
 */
class MssqlDialectTest {

    private MssqlDialect dialect = new MssqlDialect();

    /**
     * @throws Exception
     */
    @Test
    void testConvertLimitSql_limitOnly() throws Exception {
        String sql = "select * from emp order by id";
        String expected = "select * from emp order by id offset 0 rows  fetch next 5 rows only";
        assertEquals(expected, dialect.convertLimitSql(sql, 0, 5));
    }

    /**
     * @throws Exception
     */
    @Test
    void testConvertLimitSql_limitOnly_distinct() throws Exception {
        String sql = "select distinct * from emp order by id";
        String expected = "select distinct * from emp order by id offset 0 rows  fetch next 5 rows only";
        assertEquals(expected, dialect.convertLimitSql(sql, 0, 5));

    }


    /**
     * @throws Exception
     */
    @Test
    void testConvertLimitSqlByRowNumber_offsetLimit() throws Exception {
        String sql = "select * from emp order by id";
        String expected = "select * from ( select "
                + "temp_.*, row_number() over(order by id) as rownumber_ from ( select * from emp ) as temp_ ) as temp2_"
                + " where rownumber_ >= 6 and rownumber_ <= 15";
        assertEquals(expected, dialect.convertLimitSqlByRowNumber(sql, 5, 10));
    }

    /**
     * @throws Exception
     */
    @Test
    void testConvertLimitSqlByRowNumber_offsetOnly() throws Exception {
        String sql = "select * from emp order by id";
        String expected = "select * from ( select "
                + "temp_.*, row_number() over(order by id) as rownumber_ from ( select * from emp ) as temp_ ) as temp2_"
                + " where rownumber_ >= 6";
        assertEquals(expected, dialect.convertLimitSqlByRowNumber(sql, 5, 0));
    }

    /**
     * @throws Exception
     */
    @Test
    void testConvertLimitSql_offsetLimit_notFoundOrderBy()
            throws Exception {
        String sql = "select * from emp";
        try {
            dialect.convertLimitSql(sql, 5, 10);
            fail();
        } catch (OrderByNotFoundRuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    @Test
    void testIsUniqueConstraintViolation() throws Exception {
        assertTrue(dialect.isUniqueConstraintViolation(new Exception(
                new SQLRuntimeException(SQLException.class
                        .cast(new SQLException("foo", "XXX")
                                .initCause(new SQLException("bar", "23000",
                                        2627)))))));
        assertFalse(dialect
                .isUniqueConstraintViolation(new Exception(
                        new SQLRuntimeException(SQLException.class
                                .cast(new SQLException("foo", "XXX")
                                        .initCause(new SQLException("bar",
                                                "23000")))))));
        assertFalse(dialect.isUniqueConstraintViolation(new Exception(
                new RuntimeException())));
    }

}
