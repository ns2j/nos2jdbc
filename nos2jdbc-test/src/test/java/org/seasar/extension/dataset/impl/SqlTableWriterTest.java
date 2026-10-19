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

import javax.sql.DataSource;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.PrimaryKeyNotFoundRuntimeException;

@ExtendWith(NoS2JdbcExtension2.class)
class SqlTableWriterTest {

    private DataSource ds_;

    @Test
    void testCreatedTx() throws Exception {
        DataTable table = new DataTableImpl("emp");
        table.addColumn("empno");
        table.addColumn("ename");
        table.addColumn("dname");
        DataRow row = table.addRow();
        row.setValue("empno", Integer.valueOf(9900));
        row.setValue("ename", "hoge");
        row.setValue("dname", "aaa");
        SqlTableWriter writer = new SqlTableWriter(ds_);
        writer.write(table);
        SqlTableReader reader = new SqlTableReader(ds_);
        reader.setTable("emp", "empno = 9900");
        DataTable ret = reader.read();
        System.out.println(ret);
        assertNotNull(ret);
    }

    @Test
    void testModifiedTx() {
        SqlTableReader reader = new SqlTableReader(ds_);
        String sql = "SELECT empno, ename, dname FROM emp, dept WHERE empno = 7788 AND emp.deptno = dept.deptno";
        reader.setSql(sql, "emp");
        DataTable table = reader.read();
        DataRow row = table.getRow(0);
        row.setValue("ename", "hoge");
        row.setValue("dname", "aaa");
        SqlTableWriter writer = new SqlTableWriter(ds_);
        writer.write(table);
        SqlTableReader reader2 = new SqlTableReader(ds_);
        reader2.setTable("emp", "empno = 7788");
        DataTable table2 = reader2.read();
        System.out.println(table2);
        assertEquals("hoge", table2.getRow(0).getValue("ename"));
    }

    @Test
    void testModifiedNoPrimaryKeyTx() {
        SqlTableReader reader = new SqlTableReader(ds_);
        String sql = "SELECT empno, ename, dname FROM emp, dept WHERE empno = 7788 AND emp.deptno = dept.deptno";
        reader.setSql(sql, "emp");
        DataTable table = reader.read();
        DataRow row = table.getRow(0);
        row.setValue("ename", "hoge");
        row.setValue("dname", "aaa");
        for (int i = 0; i < table.getColumnSize(); ++i) {
            DataColumnImpl column = (DataColumnImpl) table.getColumn(i);
            column.setPrimaryKey(false);
        }
        SqlTableWriter writer = new SqlTableWriter(ds_);
        try {
            writer.write(table);
            fail();
        } catch (PrimaryKeyNotFoundRuntimeException expected) {
            expected.printStackTrace();
        }
    }

    @Test
    void testRemovedTx() {
        SqlTableReader reader = new SqlTableReader(ds_);
        String sql = "SELECT empno, ename, dname FROM emp, dept WHERE empno = 7788 AND emp.deptno = dept.deptno";
        reader.setSql(sql, "emp");
        DataTable table = reader.read();
        DataRow row = table.getRow(0);
        row.remove();
        SqlTableWriter writer = new SqlTableWriter(ds_);
        writer.write(table);
        SqlTableReader reader2 = new SqlTableReader(ds_);
        reader2.setTable("emp", "empno = 7788");
        DataTable table2 = reader2.read();
        assertEquals(0, table2.getRowSize());
    }

    @Test
    void testRemovedNoPrimaryKeyTx() {
        SqlTableReader reader = new SqlTableReader(ds_);
        String sql = "SELECT empno, ename, dname FROM emp, dept WHERE empno = 7788 AND emp.deptno = dept.deptno";
        reader.setSql(sql, "emp");
        DataTable table = reader.read();
        DataRow row = table.getRow(0);
        row.remove();
        for (int i = 0; i < table.getColumnSize(); ++i) {
            DataColumnImpl column = (DataColumnImpl) table.getColumn(i);
            column.setPrimaryKey(false);
        }
        SqlTableWriter writer = new SqlTableWriter(ds_);
        try {
            writer.write(table);
            fail();
        } catch (PrimaryKeyNotFoundRuntimeException expected) {
            expected.printStackTrace();
        }
    }
}