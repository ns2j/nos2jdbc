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
package org.seasar.extension.dataset.impl;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.seasar.extension.dataset.DataColumn;
import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.types.ColumnTypes;
import org.seasar.framework.conversion.TimestampConversionUtil;
import org.seasar.framework.util.Base64Util;
import org.seasar.framework.util.ResourceUtil;

import static nos2jdbc.unit.DataSetAssertions.*;
class SsReaderTest {

    private static final String PATH = "org/seasar/extension/dataset/impl/SsReaderImplTest.xlsx";

    private static final String MAX_PATH = "org/seasar/extension/dataset/impl/SsReaderImplMaxTest.xls";

    private static final String EMPTY_PATH = "org/seasar/extension/dataset/impl/SsReaderImplEmptyColumnTest.xls";

    private DataSet dataSet_;

    @BeforeEach
    void setUp() throws Exception {
        dataSet_ = new SsReader(PATH).read();
    }

    @Test
    void testCreateTable() {
        assertEquals(6, dataSet_.getTableSize());
    }

    @Test
    void testSetupColumns() {
        DataTable table = dataSet_.getTable(2);
        assertEquals(9, table.getColumnSize());
        for (int i = 0; i < table.getColumnSize(); ++i) {
            assertEquals("COLUMN" + i, table.getColumnName(i));
        }
        assertEquals(ColumnTypes.TIMESTAMP, table.getColumnType(0));
        assertEquals(ColumnTypes.BIGDECIMAL, table.getColumnType(1));
        assertEquals(ColumnTypes.STRING, table.getColumnType(2));
        assertEquals(ColumnTypes.BINARY, table.getColumnType(3));
        assertEquals(ColumnTypes.BIGDECIMAL, table.getColumnType(4));
        assertEquals(ColumnTypes.BOOLEAN, table.getColumnType(5));
        assertEquals(ColumnTypes.STRING, table.getColumnType(6));
        assertEquals(ColumnTypes.STRING, table.getColumnType(7));
        assertEquals(ColumnTypes.BIGDECIMAL, table.getColumnType(8));
    }

    @Test
    void testSetupColumnsForNull() {
        DataTable table = dataSet_.getTable(4);
        assertEquals(ColumnTypes.OBJECT, table.getColumnType(1));
    }

    @Test
    void testSetupRows() {
        DataTable table = dataSet_.getTable(0);
        assertEquals(12, table.getRowSize());
        for (int i = 0; i < table.getRowSize(); ++i) {
            DataRow row = table.getRow(i);
            for (int j = 0; j < table.getColumnSize(); ++j) {
                assertEquals("row " + i + " col " + j, row.getValue(j));
            }
        }
        DataTable table2 = dataSet_.getTable("EMPTY_TABLE");
        assertEquals(0, table2.getRowSize());
    }

    @Test
    void testGetValue() {
        DataTable table = dataSet_.getTable(2);
        DataRow row = table.getRow(0);
        assertEquals(TimestampConversionUtil.toTimestamp("20040322",
                "yyyyMMdd"), row.getValue(0));
        assertEquals(new BigDecimal(123), row.getValue(1));
        assertEquals("\u3042", row.getValue(2));
        assertEquals("YWJj", Base64Util.encode((byte[]) row.getValue(3)));
        assertEquals(new BigDecimal("0.05"), row.getValue(4));
        assertEquals(Boolean.TRUE, row.getValue(5));
        assertEquals("\"    \"", row.getValue(6));
        assertEquals("\"a\"b\"", row.getValue(7));
        assertNull(row.getValue(8));
    }

    @Test
    void testGetValueNoTrim() {
        dataSet_ = new SsReader(PATH, false).read();
        DataTable table = dataSet_.getTable(2);
        DataRow row = table.getRow(0);
        assertEquals(TimestampConversionUtil.toTimestamp("20040322",
                "yyyyMMdd"), row.getValue(0));
        assertEquals(new BigDecimal(123), row.getValue(1));
        assertEquals("\u3042", row.getValue(2));
        assertEquals("YWJj", Base64Util.encode((byte[]) row.getValue(3)));
        assertEquals(new BigDecimal("0.05"), row.getValue(4));
        assertEquals(Boolean.TRUE, row.getValue(5));
        assertEquals("    ", row.getValue(6));
        assertEquals("a\"b", row.getValue(7));
        assertEquals("a\"b", row.getValue(7));
        assertNull(row.getValue(8));
    }

    @Test
    void testGetValueNoTrim_File() {
        URL url = ResourceUtil.getResource(PATH);
        File file = ResourceUtil.getFile(url);
        dataSet_ = new SsReader(file, false).read();
        DataTable table = dataSet_.getTable(2);
        DataRow row = table.getRow(0);
        assertEquals(TimestampConversionUtil.toTimestamp("20040322",
                "yyyyMMdd"), row.getValue(0));
        assertEquals(new BigDecimal(123), row.getValue(1));
        assertEquals("\u3042", row.getValue(2));
        assertEquals("YWJj", Base64Util.encode((byte[]) row.getValue(3)));
        assertEquals(new BigDecimal("0.05"), row.getValue(4));
        assertEquals(Boolean.TRUE, row.getValue(5));
        assertEquals("    ", row.getValue(6));
        assertEquals("a\"b", row.getValue(7));
        assertEquals("a\"b", row.getValue(7));
        assertNull(row.getValue(8));
    }

    @Test
    void testFloatingPoint() {
        DataTable table = dataSet_.getTable("FLOATING_POINT");
        DataSet dataSet = new DataSetImpl();
        dataSet.addTable(table);

        FloatingPointBean[] beans = new FloatingPointBean[11];
        beans[0] = new FloatingPointBean(0.1, 0.123);
        beans[1] = new FloatingPointBean(0.01, 0.0123);
        beans[2] = new FloatingPointBean(0.001, 0.00123);
        beans[3] = new FloatingPointBean(0.0001, 0.000123);
        beans[4] = new FloatingPointBean(0.00001, 0.0000123);
        beans[5] = new FloatingPointBean(0.000001, 0.00000123);
        beans[6] = new FloatingPointBean(0.0000001, 0.000000123);
        beans[7] = new FloatingPointBean(0.00000001, 0.0000000123);
        beans[8] = new FloatingPointBean(0.000000001, 0.00000000123);
        beans[9] = new FloatingPointBean(0.0000000001, 0.000000000123);
        beans[10] = new FloatingPointBean(0.00000000001, 0.0000000000123);

        assertDsEquals(dataSet, beans);
    }

    @Test
    void testMaxRow() {
        DataSet dataSet = new SsReader(MAX_PATH).read();
        DataTable table = dataSet.getTable("MAX");
        assertEquals(65535, table.getRowSize());
    }

    @Test
    void testEmptyColumn() {
        dataSet_ = new SsReader(EMPTY_PATH).read();
        DataTable table = dataSet_.getTable("TEST");
        DataColumn column = table.getColumn("end_date");
        assertEquals(Timestamp.class, column.getColumnType().getType());
    }

    /**
     * 
     */
    public static class FloatingPointBean {
        private Double column0;

        private Double column1;

        /**
         * @param column0
         * @param column1
         */
        public FloatingPointBean(double column0, double column1) {
            super();
            this.column0 = Double.valueOf(column0);
            this.column1 = Double.valueOf(column1);
        }

        /**
         * @return
         */
        public Double getColumn0() {
            return column0;
        }

        /**
         * @param column0
         */
        public void setColumn0(Double column0) {
            this.column0 = column0;
        }

        /**
         * @return
         */
        public Double getColumn1() {
            return column1;
        }

        /**
         * @param column1
         */
        public void setColumn1(Double column1) {
            this.column1 = column1;
        }
    }

}
