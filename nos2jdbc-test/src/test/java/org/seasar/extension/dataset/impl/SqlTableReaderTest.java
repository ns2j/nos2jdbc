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

import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.states.RowStates;

@ExtendWith(NoS2JdbcExtension2.class)
class SqlTableReaderTest {

    private DataSource ds_;

    @Test
    void testRead() {
        SqlTableReader reader = new SqlTableReader(ds_);
        reader.setTable("emp");
        DataTable ret = reader.read();
        System.out.println(ret);
        assertEquals(14, ret.getRowSize());
        assertEquals(RowStates.UNCHANGED, ret.getRow(0).getState());
    }

    @Test
    void testRead4() {
        SqlTableReader reader = new SqlTableReader(ds_);
        reader.setTable("emp", "", "ename");
        DataTable ret = reader.read();
        System.out.println(ret);
        assertEquals(14, ret.getRowSize());
        assertEquals("ADAMS", ret.getRow(0).getValue("ename"));
    }

    @Test
    void testRead2() {
        SqlTableReader reader = new SqlTableReader(ds_);
        reader.setTable("emp", "empno = 7788");
        DataTable ret = reader.read();
        System.out.println(ret);
        assertEquals(1, ret.getRowSize());
    }

    @Test
    void testRead3() {
        SqlTableReader reader = new SqlTableReader(ds_);
        reader.setSql("select * from emp", "emp");
        DataTable ret = reader.read();
        System.out.println(ret);
        assertEquals(14, ret.getRowSize());
    }

}