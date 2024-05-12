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

import java.math.BigDecimal;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.states.RowStates;
import org.seasar.extension.dataset.types.ColumnTypes;
import org.seasar.extension.jdbc.util.ConnectionUtil;
import org.seasar.framework.exception.SQLRuntimeException;

@ExtendWith(NoS2JdbcExtension2.class)
class DataTableImplTest {
    static private DataSource ds_;

    @Test
    void testRemoveRows() {
        DataTable table = new DataTableImpl("hoge");
        table.addColumn("aaa", ColumnTypes.STRING);
        DataRow row = table.addRow();
        row.setValue("aaa", "1");
        DataRow row2 = table.addRow();
        row2.setValue("aaa", "2");
        row2.remove();
        DataRow row3 = table.addRow();
        row3.setValue("aaa", "3");
        row3.remove();
        DataRow[] rows = table.removeRows();
        assertEquals(2, rows.length);
        assertEquals(2, table.getRemovedRowSize());
        assertEquals(1, table.getRowSize());
        assertEquals("1", table.getRow(0).getValue("aaa"));
        assertSame(row2, table.getRemovedRow(0));
        assertSame(row3, table.getRemovedRow(1));
    }

    @Test
    void testEquals() {
        DataTable table = new DataTableImpl("hoge");
        DataTable table2 = new DataTableImpl("hoge");
        table.addColumn("aaa");
        table2.addColumn("aaa");
        DataRow row = table.addRow();
        row.setValue("aaa", "1");
        row = table.addRow();
        row.setValue("aaa", "2");
        row.remove();
        DataRow row2 = table2.addRow();
        row2.setValue("aaa", "1");
        row2 = table2.addRow();
        row2.setValue("aaa", "2");
        row2.remove();
        assertEquals(table, table2);
        table.removeRows();
        table2.removeRows();
        assertEquals(table, table2);
        table2.getRow(0).setValue(0, "11");
        assertEquals(false, table.equals(table2));
    }

    @Test
    void testEquals2() {
        DataTable table = new DataTableImpl("hoge");
        table.addColumn("aaa");
        table.addColumn("bbb");
        DataTable table2 = new DataTableImpl("hoge2");
        table2.addColumn("ccc");
        table2.addColumn("aaa");
        table2.addColumn("bbb");
        DataRow row = table.addRow();
        DataRow row2 = table2.addRow();
        row.setValue("aaa", "111");
        row.setValue("bbb", "222");
        row2.setValue("aaa", "111");
        row2.setValue("bbb", "222");
        row2.setValue("ccc", "333");
        assertEquals(table, table2);
    }

    @Test
    void testEquals3() {
        DataTable table = new DataTableImpl("hoge");
        table.addColumn("aaa");
        DataTable table2 = new DataTableImpl("hoge2");
        table2.addColumn("aaa", ColumnTypes.STRING);
        DataRow row = table.addRow();
        DataRow row2 = table2.addRow();
        row.setValue("aaa", new BigDecimal("111"));
        row2.setValue("aaa", "111");
        assertEquals(table, table2);
    }

    @Test
    void testSetupColumns() {
        DataTable table = new DataTableImpl("hoge");
        table.setupColumns(MyBean.class);
        assertEquals(2, table.getColumnSize());
        assertEquals(ColumnTypes.BIGDECIMAL, table.getColumnType("myInt"));
        assertEquals(ColumnTypes.STRING, table.getColumnType("myString"));
    }

    @Test
    void testCopyFromList() {
        DataTable table = new DataTableImpl("hoge");
        table.setupColumns(MyBean.class);
        List<MyBean> list = new ArrayList<>();
        MyBean bean = new MyBean();
        bean.setMyInt(1);
        bean.setMyString("a");
        list.add(bean);
        bean = new MyBean();
        bean.setMyInt(2);
        bean.setMyString("b");
        list.add(bean);
        table.copyFrom(list);
        assertEquals(2, table.getRowSize());
        assertEquals(new BigDecimal(1), table.getRow(0).getValue("myInt"));
        assertEquals("a", table.getRow(0).getValue("myString"));
        assertEquals(new BigDecimal(2), table.getRow(1).getValue("myInt"));
        assertEquals("b", table.getRow(1).getValue("myString"));
        assertEquals(RowStates.UNCHANGED, table.getRow(0).getState());
    }

    @Test
    void testCopyFromBeanOrMap() {
        DataTable table = new DataTableImpl("hoge");
        table.setupColumns(MyBean.class);
        MyBean bean = new MyBean();
        bean.setMyInt(1);
        bean.setMyString("a");
        table.copyFrom(bean);
        assertEquals(1, table.getRowSize());
        assertEquals(new BigDecimal(1), table.getRow(0).getValue("myInt"));
        assertEquals("a", table.getRow(0).getValue("myString"));
        assertEquals(RowStates.UNCHANGED, table.getRow(0).getState());
    }

    @Test
    void testHasColumn() {
        DataTable table = new DataTableImpl("hoge");
        table.addColumn("aaa_0");
        table.addColumn("bbbCcc");
        assertEquals(true, table.hasColumn("aaa_0"));
        assertEquals(true, table.hasColumn("bbb_ccc"));
    }

    @Test
    void testGetColumn() {
        DataTable table = new DataTableImpl("hoge");
        table.addColumn("aaa_0");
        table.addColumn("bbbCcc");
        assertNotNull(table.getColumn("aaa_0"));
        assertNotNull(table.getColumn("bbb_ccc"));
    }

    @Test
    void testGetColumn2() {
        DataTable table = new DataTableImpl("hoge");
        table.addColumn("aaa_bbb");
        assertNotNull(table.getColumn("aaaBbb"));
    }

    private DatabaseMetaData getDatabaseMetaData() throws SQLRuntimeException, SQLException {
        return ConnectionUtil.getMetaData(ds_.getConnection());
    }

    @Test
    void testSetupMetaData() throws Exception {
        DataTable table = new DataTableImpl("emp");
        table.addColumn("empno");
        table.setupMetaData(getDatabaseMetaData());
        assertEquals(ColumnTypes.BIGDECIMAL, table.getColumnType("empno"));
    }

    /**
     *
     */
    public static class MyBean {

        private int myInt_;

        private String myString_;

        /**
         * @return
         */
        public int getMyInt() {
            return myInt_;
        }

        /**
         * @param myInt
         */
        public void setMyInt(int myInt) {
            myInt_ = myInt;
        }

        /**
         * @return
         */
        public String getMyString() {
            return myString_;
        }

        /**
         * @param myString
         */
        public void setMyString(String myString) {
            myString_ = myString;
        }
    }
}