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

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

import javax.sql.DataSource;

import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.TableWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(NoS2JdbcExtension2.class)
class SqlDeleteTableWriterTest {
    final static private Logger logger = LoggerFactory.getLogger(SqlDeleteTableWriter.class);
    static private DataSource ds_;

    @Test
    void testWriteTx() {
        DataTable table = new DataTableImpl("emp");
        table.addColumn("empno");
        table.addColumn("ename");
        DataRow row = table.addRow();
        row.setValue("empno", Integer.valueOf(9900));
        row.setValue("ename", "hoge");
        TableWriter writer = new SqlTableWriter(ds_);
        writer.write(table);
        TableWriter writer2 = new SqlDeleteTableWriter(ds_);
        table.getColumn("empno").setPrimaryKey(true);
        writer2.write(table);

        SqlTableReader reader = new SqlTableReader(ds_);
        reader.setTable("emp", "empno = 7934");
        DataTable table2 = reader.read();
        assertEquals(1, table2.getRowSize());
    }

}