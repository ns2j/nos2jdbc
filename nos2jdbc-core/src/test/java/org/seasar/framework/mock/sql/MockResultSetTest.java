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
package org.seasar.framework.mock.sql;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.seasar.framework.util.ArrayMap;

/**
 * @author higa
 * 
 */
class MockResultSetTest {

    /**
     * Test method for
     * {@link org.seasar.framework.mock.sql.MockResultSet#addRowData(org.seasar.framework.util.ArrayMap)}.
     */
    @Test
    void testAddAndGetRowData() {
        MockResultSet rs = new MockResultSet();
        ArrayMap rowData = new ArrayMap();
        rowData.put("id", Integer.valueOf(1));
        rs.addRowData(rowData);
        assertSame(rowData, rs.getRowData(1));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    void testGetColumnData() throws Exception {
        MockResultSet rs = new MockResultSet();
        ArrayMap rowData = new ArrayMap();
        Integer value = Integer.valueOf(1);
        rowData.put("id", value);
        rs.addRowData(rowData);
        assertTrue(rs.next());
        assertEquals(value, rs.getColumnData(1));
        assertEquals(value, rs.getColumnData("id"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    void testNext() throws Exception {
        MockResultSet rs = new MockResultSet();
        ArrayMap rowData = new ArrayMap();
        Integer value = Integer.valueOf(1);
        rowData.put("id", value);
        rs.addRowData(rowData);
        assertTrue(rs.next());
        assertEquals(1, rs.getRow());
        assertFalse(rs.next());
        assertEquals(2, rs.getRow());
        assertFalse(rs.next());
        assertEquals(2, rs.getRow());
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    void testPrevious() throws Exception {
        MockResultSet rs = new MockResultSet();
        try {
            rs.previous();
            fail();
        } catch (SQLException e) {
            System.out.println(e);
        }
        rs.setType(ResultSet.TYPE_SCROLL_INSENSITIVE);
        ArrayMap rowData = new ArrayMap();
        Integer value = Integer.valueOf(1);
        rowData.put("id", value);
        rs.addRowData(rowData);
        assertTrue(rs.next());
        assertFalse(rs.next());
        assertTrue(rs.previous());
        assertEquals(1, rs.getRow());
        assertFalse(rs.previous());
        assertEquals(0, rs.getRow());
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    void testGetBigDecimal() throws Exception {
        MockResultSet rs = new MockResultSet();
        ArrayMap rowData = new ArrayMap();
        BigDecimal value = new BigDecimal(1);
        rowData.put("id", value);
        rs.addRowData(rowData);
        assertTrue(rs.next());
        assertEquals(value, rs.getBigDecimal(1));
        assertEquals(value, rs.getBigDecimal("id"));
        BigDecimal value2 = rs.getBigDecimal(1, 2);
        assertEquals(2, value2.scale());
        value2 = rs.getBigDecimal("id", 2);
        assertEquals(2, value2.scale());
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    void testGetBoolean() throws Exception {
        MockResultSet rs = new MockResultSet();
        ArrayMap rowData = new ArrayMap();
        Boolean value = Boolean.TRUE;
        rowData.put("hoge", value);
        rs.addRowData(rowData);
        assertTrue(rs.next());
        assertTrue(rs.getBoolean(1));
        assertTrue(rs.getBoolean("hoge"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    void testGetByte() throws Exception {
        MockResultSet rs = new MockResultSet();
        ArrayMap rowData = new ArrayMap();
        Byte value = new Byte((byte) 1);
        rowData.put("hoge", value);
        rs.addRowData(rowData);
        assertTrue(rs.next());
        assertEquals((byte) 1, rs.getByte(1));
        assertEquals((byte) 1, rs.getByte("hoge"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    void testGetBytes() throws Exception {
        MockResultSet rs = new MockResultSet();
        ArrayMap rowData = new ArrayMap();
        byte[] value = new byte[0];
        rowData.put("hoge", value);
        rs.addRowData(rowData);
        assertTrue(rs.next());
        assertSame(value, rs.getBytes(1));
        assertSame(value, rs.getBytes("hoge"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    void testGetDate() throws Exception {
        MockResultSet rs = new MockResultSet();
        ArrayMap rowData = new ArrayMap();
        Date value = new Date(0);
        rowData.put("hoge", value);
        rs.addRowData(rowData);
        assertTrue(rs.next());
        assertSame(value, rs.getDate(1));
        assertSame(value, rs.getDate("hoge"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    void testGetDouble() throws Exception {
        MockResultSet rs = new MockResultSet();
        ArrayMap rowData = new ArrayMap();
        Double value = new Double(0);
        rowData.put("hoge", value);
        rs.addRowData(rowData);
        assertTrue(rs.next());
        assertEquals(value.doubleValue(), rs.getDouble(1));
        assertEquals(value.doubleValue(), rs.getDouble("hoge"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    void testGetFloat() throws Exception {
        MockResultSet rs = new MockResultSet();
        ArrayMap rowData = new ArrayMap();
        Float value = new Float(0);
        rowData.put("hoge", value);
        rs.addRowData(rowData);
        assertTrue(rs.next());
        assertEquals(value.floatValue(), rs.getFloat(1));
        assertEquals(value.floatValue(), rs.getFloat("hoge"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    void testGetInt() throws Exception {
        MockResultSet rs = new MockResultSet();
        ArrayMap rowData = new ArrayMap();
        Integer value = Integer.valueOf(0);
        rowData.put("hoge", value);
        rs.addRowData(rowData);
        assertTrue(rs.next());
        assertEquals(value.intValue(), rs.getInt(1));
        assertEquals(value.intValue(), rs.getInt("hoge"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    void testGetLong() throws Exception {
        MockResultSet rs = new MockResultSet();
        ArrayMap rowData = new ArrayMap();
        Long value = Long.valueOf(0);
        rowData.put("hoge", value);
        rs.addRowData(rowData);
        assertTrue(rs.next());
        assertEquals(value.longValue(), rs.getLong(1));
        assertEquals(value.longValue(), rs.getLong("hoge"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    void testGetObject() throws Exception {
        MockResultSet rs = new MockResultSet();
        ArrayMap rowData = new ArrayMap();
        Object value = new Object();
        rowData.put("hoge", value);
        rs.addRowData(rowData);
        assertTrue(rs.next());
        assertSame(value, rs.getObject(1));
        assertSame(value, rs.getObject("hoge"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    void testGetShort() throws Exception {
        MockResultSet rs = new MockResultSet();
        ArrayMap rowData = new ArrayMap();
        Short value = new Short((short) 0);
        rowData.put("hoge", value);
        rs.addRowData(rowData);
        assertTrue(rs.next());
        assertEquals(value.shortValue(), rs.getShort(1));
        assertEquals(value.shortValue(), rs.getShort("hoge"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    void testGetString() throws Exception {
        MockResultSet rs = new MockResultSet();
        ArrayMap rowData = new ArrayMap();
        String value = "aaa";
        rowData.put("hoge", value);
        rs.addRowData(rowData);
        assertTrue(rs.next());
        assertEquals(value, rs.getString(1));
        assertEquals(value, rs.getString("hoge"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    void testGetTime() throws Exception {
        MockResultSet rs = new MockResultSet();
        ArrayMap rowData = new ArrayMap();
        Time value = new Time(0);
        rowData.put("hoge", value);
        rs.addRowData(rowData);
        assertTrue(rs.next());
        assertEquals(value, rs.getTime(1));
        assertEquals(value, rs.getTime("hoge"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    void testGetTimestamp() throws Exception {
        MockResultSet rs = new MockResultSet();
        ArrayMap rowData = new ArrayMap();
        Timestamp value = new Timestamp(0);
        rowData.put("hoge", value);
        rs.addRowData(rowData);
        assertTrue(rs.next());
        assertEquals(value, rs.getTimestamp(1));
        assertEquals(value, rs.getTimestamp("hoge"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    void testIsAfterLast() throws Exception {
        MockResultSet rs = new MockResultSet();
        ArrayMap rowData = new ArrayMap();
        Timestamp value = new Timestamp(0);
        rowData.put("hoge", value);
        rs.addRowData(rowData);
        assertTrue(rs.next());
        assertFalse(rs.isAfterLast());
        assertFalse(rs.next());
        assertTrue(rs.isAfterLast());
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    void testIsBeforeFirst() throws Exception {
        MockResultSet rs = new MockResultSet();
        ArrayMap rowData = new ArrayMap();
        Timestamp value = new Timestamp(0);
        rowData.put("hoge", value);
        rs.addRowData(rowData);
        assertTrue(rs.isBeforeFirst());
        assertTrue(rs.next());
        assertFalse(rs.isBeforeFirst());
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    void testIsFirst() throws Exception {
        MockResultSet rs = new MockResultSet();
        ArrayMap rowData = new ArrayMap();
        Timestamp value = new Timestamp(0);
        rowData.put("hoge", value);
        rs.addRowData(rowData);
        assertFalse(rs.isFirst());
        assertTrue(rs.next());
        assertTrue(rs.isFirst());
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    void testIsLast() throws Exception {
        MockResultSet rs = new MockResultSet();
        ArrayMap rowData = new ArrayMap();
        Timestamp value = new Timestamp(0);
        rowData.put("hoge", value);
        rs.addRowData(rowData);
        assertFalse(rs.isLast());
        assertTrue(rs.next());
        assertTrue(rs.isLast());
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    void testLast() throws Exception {
        MockResultSet rs = new MockResultSet();
        rs.setType(ResultSet.TYPE_SCROLL_INSENSITIVE);
        ArrayMap rowData = new ArrayMap();
        Timestamp value = new Timestamp(0);
        rowData.put("hoge", value);
        rs.addRowData(rowData);
        assertTrue(rs.last());
        assertTrue(rs.isLast());
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    void testFirst() throws Exception {
        MockResultSet rs = new MockResultSet();
        rs.setType(ResultSet.TYPE_SCROLL_INSENSITIVE);
        ArrayMap rowData = new ArrayMap();
        Timestamp value = new Timestamp(0);
        rowData.put("hoge", value);
        rs.addRowData(rowData);
        assertTrue(rs.first());
        assertTrue(rs.isFirst());
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    void testAbsolute() throws Exception {
        MockResultSet rs = new MockResultSet();
        rs.setType(ResultSet.TYPE_SCROLL_INSENSITIVE);
        ArrayMap rowData = new ArrayMap();
        Timestamp value = new Timestamp(0);
        rowData.put("hoge", value);
        rs.addRowData(rowData);
        rs.addRowData(rowData);
        assertTrue(rs.absolute(1));
        assertEquals(1, rs.getRow());
        assertTrue(rs.absolute(2));
        assertEquals(2, rs.getRow());
        assertFalse(rs.absolute(0));
        assertEquals(2, rs.getRow());
        assertFalse(rs.absolute(3));
        assertEquals(2, rs.getRow());
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    void testRelative() throws Exception {
        MockResultSet rs = new MockResultSet();
        rs.setType(ResultSet.TYPE_SCROLL_INSENSITIVE);
        ArrayMap rowData = new ArrayMap();
        Timestamp value = new Timestamp(0);
        rowData.put("hoge", value);
        rs.addRowData(rowData);
        rs.addRowData(rowData);
        assertTrue(rs.relative(2));
        assertEquals(2, rs.getRow());
        assertTrue(rs.relative(-1));
        assertEquals(1, rs.getRow());
        assertTrue(rs.relative(1));
        assertEquals(2, rs.getRow());
        assertFalse(rs.relative(1));
        assertEquals(2, rs.getRow());
        assertFalse(rs.relative(-2));
        assertEquals(2, rs.getRow());
    }
}
