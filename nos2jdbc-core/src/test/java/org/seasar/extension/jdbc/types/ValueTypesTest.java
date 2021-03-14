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
package org.seasar.extension.jdbc.types;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.impl.ResultSetWrapper;

class ValueTypesTest {

    @Test
    void testGetValueType()  {
        assertEquals(ValueTypes.TIMESTAMP, ValueTypes
                .getValueType(GregorianCalendar.class));

    }

    @Test
    void testGetValueType_unknownClass()  {
        assertEquals(ValueTypes.OBJECT, ValueTypes.getValueType(getClass()));
    }

    @Test
    void testUserDefineType() throws SQLException  {
        ValueType valueType = ValueTypes.getValueType(Authority.class);
        assertNotNull(valueType);
        assertTrue(valueType instanceof UserDefineType);
        final ResultSet resultSet = new MockResultSet() {
            @Override
            public Object getObject(int columnIndex) throws SQLException {
                return Integer.valueOf(2);
            }
        };
        final Authority value = (Authority) valueType.getValue(resultSet, 0);
        assertEquals(2, value.value());
    }

    @Test
    void testIsSimpleType()  {
        assertFalse(ValueTypes.isSimpleType(HashMap.class));
        assertTrue(ValueTypes.isSimpleType(byte[].class));
        assertTrue(ValueTypes.isSimpleType(InputStream.class));
    }
    
    @Test
    void testGetValueType_LocalDate()  {
        assertEquals(ValueTypes.LOCALDATE, ValueTypes.getValueType(LocalDate.class));
    }
    @Test
    void testGetValueType_LocalTime()  {
        assertEquals(ValueTypes.LOCALTIME, ValueTypes.getValueType(LocalTime.class));
    }
    @Test
    void testGetValueType_LocalDateTime()  {
        assertEquals(ValueTypes.LOCALDATETIME, ValueTypes.getValueType(LocalDateTime.class));
    }
    @Test
    void testGetValueType_OffsetDateTime()  {
        assertEquals(ValueTypes.TIMESTAMP_WITH_TIMEZONE, ValueTypes.getValueType(OffsetDateTime.class));
    }
    
    private static class MockResultSet extends ResultSetWrapper {
        MockResultSet() {
            super(null);
        }
    }
}