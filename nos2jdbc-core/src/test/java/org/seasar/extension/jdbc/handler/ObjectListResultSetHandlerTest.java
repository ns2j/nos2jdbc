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
package org.seasar.extension.jdbc.handler;

import java.util.List;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.seasar.extension.jdbc.types.ValueTypes;
import org.seasar.framework.mock.sql.MockColumnMetaData;
import org.seasar.framework.mock.sql.MockResultSet;
import org.seasar.framework.mock.sql.MockResultSetMetaData;
import org.seasar.framework.util.ArrayMap;

/**
 * @author higa
 * 
 */
class ObjectListResultSetHandlerTest {

    /**
     * @throws Exception
     * 
     */
    @Test
    void testHandle() throws Exception {
        ObjectListResultSetHandler handler = new ObjectListResultSetHandler(
                ValueTypes.INTEGER);
        MockResultSetMetaData rsMeta = new MockResultSetMetaData();
        MockColumnMetaData columnMeta = new MockColumnMetaData();
        columnMeta.setColumnLabel("AAA");
        rsMeta.addColumnMetaData(columnMeta);
        MockResultSet rs = new MockResultSet(rsMeta);
        ArrayMap data = new ArrayMap();
        data.put("AAA", Integer.valueOf(5));
        rs.addRowData(data);
        data = new ArrayMap();
        data.put("AAA", Integer.valueOf(6));
        rs.addRowData(data);
        @SuppressWarnings("rawtypes")
        List ret = (List) handler.handle(rs);
        assertEquals(2, ret.size());
        assertEquals(Integer.valueOf(5), ret.get(0));
        assertEquals(Integer.valueOf(6), ret.get(1));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    void testHandleWithLimit() throws Exception {
        ObjectListResultSetHandler handler = new ObjectListResultSetHandler(
                ValueTypes.INTEGER, 1);
        MockResultSetMetaData rsMeta = new MockResultSetMetaData();
        MockColumnMetaData columnMeta = new MockColumnMetaData();
        columnMeta.setColumnLabel("AAA");
        rsMeta.addColumnMetaData(columnMeta);
        MockResultSet rs = new MockResultSet(rsMeta);
        ArrayMap data = new ArrayMap();
        data.put("AAA", Integer.valueOf(5));
        rs.addRowData(data);
        data = new ArrayMap();
        data.put("AAA", Integer.valueOf(6));
        rs.addRowData(data);
        @SuppressWarnings("rawtypes")
        List ret = (List) handler.handle(rs);
        assertEquals(1, ret.size());
        assertEquals(Integer.valueOf(5), ret.get(0));
    }

}
