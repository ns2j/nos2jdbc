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

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.TemporalType;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.types.ValueTypes;
import org.seasar.framework.exception.SQLRuntimeException;

/**
 * @author higa
 * 
 */
class OracleDialectTest {

    private OracleDialect dialect = new OracleDialect();

    public String stringField;

    public boolean booleanField;

    public List<?> listField;

    public ArrayList<?> arrayListField;

    public Date utilDateField;

    public Calendar utilCalendarField;

    public java.sql.Date sqlDateField;

    public java.sql.Time sqlTimeField;

    public java.sql.Timestamp sqlTimestampField;

    public LocalDate localDateField;
    public LocalTime localTimeField;
    public LocalDateTime localDateTimeField;
    public OffsetDateTime offsetDateTimeField;
    
    @Test
    void testConvertLimitSql_limitOnly()  {
        String sql = "select * from emp order by id for update";
        String expected = "select * from ( select temp_.*, rownum rownumber_ from ( select * from emp order by id ) temp_ ) where rownumber_ <= 5 for update";
        assertEquals(expected, dialect.convertLimitSql(sql, 0, 5));

    }

    @Test
    void testConvertLimitSql_offsetLimit()  {
        String sql = "select e.* from emp e order by id for update";
        String expected = "select * from ( select temp_.*, rownum rownumber_ from ( select e.* from emp e order by id ) temp_ ) where rownumber_ > 5 and rownumber_ <= 15 for update";
        assertEquals(expected, dialect.convertLimitSql(sql, 5, 10));

    }

    @Test
    void testConvertLimitSql_offsetOnly()  {
        String sql = "select e.* from emp e order by id for update";
        String expected = "select * from ( select temp_.*, rownum rownumber_ from ( select e.* from emp e order by id ) temp_ ) where rownumber_ > 5 for update";
        assertEquals(expected, dialect.convertLimitSql(sql, 5, 0));

    }

    @Test
    void testGetValueType()  {
        assertEquals(ValueTypes.WAVE_DASH_STRING,
                dialect.getValueType(String.class, false, null));
        assertEquals(ValueTypes.BOOLEAN_INTEGER,
                dialect.getValueType(boolean.class, false, null));
        assertEquals(ValueTypes.ORACLE_RESULT_SET,
                dialect.getValueType(List.class, false, null));
        assertEquals(ValueTypes.ORACLE_RESULT_SET,
                dialect.getValueType(ArrayList.class, false, null));
        assertEquals(ValueTypes.WAVE_DASH_CLOB,
                dialect.getValueType(String.class, true, null));

        assertEquals(ValueTypes.DATE_SQLDATE,
                dialect.getValueType(Date.class, false, TemporalType.DATE));
        assertEquals(ValueTypes.DATE_TIME,
                dialect.getValueType(Date.class, false, TemporalType.TIME));
        assertEquals(OracleDialect.ORACLE_DATE_TYPE,
                dialect.getValueType(Date.class, false, TemporalType.TIMESTAMP));

        assertEquals(ValueTypes.CALENDAR_SQLDATE,
                dialect.getValueType(Calendar.class, false, TemporalType.DATE));
        assertEquals(ValueTypes.CALENDAR_TIME,
                dialect.getValueType(Calendar.class, false, TemporalType.TIME));
        assertEquals(OracleDialect.ORACLE_DATE_CALENDAR_TYPE, dialect.getValueType(
                Calendar.class, false, TemporalType.TIMESTAMP));

        assertEquals(ValueTypes.SQLDATE,
                dialect.getValueType(java.sql.Date.class, false, null));
        assertEquals(ValueTypes.TIME,
                dialect.getValueType(java.sql.Time.class, false, null));
        assertEquals(ValueTypes.TIMESTAMP,
                dialect.getValueType(java.sql.Timestamp.class, false, null));
        assertEquals(ValueTypes.JDBC42LOCALDATE,
                dialect.getValueType(LocalDate.class, false, null));
        assertEquals(ValueTypes.JDBC42LOCALTIME,
                dialect.getValueType(LocalTime.class, false, null));
        assertEquals(ValueTypes.JDBC42LOCALDATETIME,
                dialect.getValueType(LocalDateTime.class, false, null));
        assertEquals(ValueTypes.JDBC42OFFSETDATETIME,
                dialect.getValueType(OffsetDateTime.class, false, null));
    }

    /**
     * @throws SecurityException 
     * @throws NoSuchFieldException 
     */
    @Test
    void testGetValueType_propertyMeta() throws NoSuchFieldException, SecurityException  {
        PropertyMeta pm = new PropertyMeta();
        pm.setField(getClass().getField("stringField"));
        pm.setValueType(ValueTypes.STRING);
        assertEquals(ValueTypes.WAVE_DASH_STRING, dialect.getValueType(pm));

        pm.setField(getClass().getField("booleanField"));
        pm.setValueType(ValueTypes.BOOLEAN);
        assertEquals(ValueTypes.BOOLEAN_INTEGER, dialect.getValueType(pm));

        pm.setField(getClass().getField("listField"));
        assertEquals(ValueTypes.ORACLE_RESULT_SET, dialect.getValueType(pm));

        pm.setField(getClass().getField("arrayListField"));
        assertEquals(ValueTypes.ORACLE_RESULT_SET, dialect.getValueType(pm));

        pm.setField(getClass().getField("stringField"));
        pm.setLob(true);
        pm.setValueType(ValueTypes.CLOB);
        assertEquals(ValueTypes.WAVE_DASH_CLOB, dialect.getValueType(pm));

        pm.setField(getClass().getField("utilDateField"));
        pm.setTemporalType(TemporalType.DATE);
        pm.setValueType(ValueTypes.SQLDATE);
        assertEquals(ValueTypes.SQLDATE, dialect.getValueType(pm));

        pm.setField(getClass().getField("utilDateField"));
        pm.setTemporalType(TemporalType.TIME);
        pm.setValueType(ValueTypes.TIME);
        assertEquals(ValueTypes.TIME, dialect.getValueType(pm));

        pm.setField(getClass().getField("utilDateField"));
        pm.setTemporalType(TemporalType.TIMESTAMP);
        pm.setValueType(ValueTypes.TIMESTAMP);
        assertEquals(OracleDialect.ORACLE_DATE_TYPE, dialect.getValueType(pm));

        pm.setField(getClass().getField("utilCalendarField"));
        pm.setTemporalType(TemporalType.DATE);
        pm.setValueType(ValueTypes.SQLDATE);
        assertEquals(ValueTypes.SQLDATE, dialect.getValueType(pm));

        pm.setField(getClass().getField("utilCalendarField"));
        pm.setTemporalType(TemporalType.TIME);
        pm.setValueType(ValueTypes.TIME);
        assertEquals(ValueTypes.TIME, dialect.getValueType(pm));

        pm.setField(getClass().getField("utilCalendarField"));
        pm.setTemporalType(TemporalType.TIMESTAMP);
        pm.setValueType(ValueTypes.TIMESTAMP);
        assertEquals(OracleDialect.ORACLE_DATE_CALENDAR_TYPE, dialect.getValueType(pm));

        pm.setField(getClass().getField("sqlDateField"));
        pm.setTemporalType(null);
        pm.setValueType(ValueTypes.SQLDATE);
        assertEquals(ValueTypes.SQLDATE, dialect.getValueType(pm));

        pm.setField(getClass().getField("sqlTimeField"));
        pm.setTemporalType(null);
        pm.setValueType(ValueTypes.TIME);
        assertEquals(ValueTypes.TIME, dialect.getValueType(pm));

        pm.setField(getClass().getField("sqlTimestampField"));
        pm.setTemporalType(null);
        pm.setValueType(ValueTypes.TIMESTAMP);
        assertEquals(ValueTypes.TIMESTAMP, dialect.getValueType(pm));

        pm.setField(getClass().getField("localDateField"));
        //pm.setTemporalType(TemporalType.DATE);
        pm.setValueType(ValueTypes.LOCALDATE);
        assertEquals(ValueTypes.JDBC42LOCALDATE, dialect.getValueType(pm));

        pm.setField(getClass().getField("localTimeField"));
        //pm.setTemporalType(TemporalType.DATE);
        pm.setValueType(ValueTypes.LOCALTIME);
        assertEquals(ValueTypes.JDBC42LOCALTIME, dialect.getValueType(pm));

        pm.setField(getClass().getField("localDateTimeField"));
        //pm.setTemporalType(TemporalType.DATE);
        pm.setValueType(ValueTypes.LOCALDATETIME);
        assertEquals(ValueTypes.JDBC42LOCALDATETIME, dialect.getValueType(pm));

        pm.setField(getClass().getField("offsetDateTimeField"));
        //pm.setTemporalType(TemporalType.DATE);
        pm.setValueType(ValueTypes.TIMESTAMP_WITH_TIMEZONE);
        assertEquals(ValueTypes.JDBC42OFFSETDATETIME, dialect.getValueType(pm));
}

    @Test
    void testNeedsParameterForResultSet()  {
        assertTrue(dialect.needsParameterForResultSet());
    }

    @Test
    void testIsUniqueConstraintViolation()  {
        assertTrue(dialect
                .isUniqueConstraintViolation(new Exception(
                        new SQLRuntimeException(SQLException.class
                                .cast(new SQLException("foo", "XXX")
                                        .initCause(new SQLException("bar",
                                                "23000", 1)))))));
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
