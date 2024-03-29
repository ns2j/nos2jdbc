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

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.extension.jdbc.types.ValueTypes;
import org.seasar.framework.mock.sql.MockColumnMetaData;
import org.seasar.framework.mock.sql.MockResultSet;
import org.seasar.framework.mock.sql.MockResultSetMetaData;
import org.seasar.framework.util.ArrayMap;

/**
 * @author higa
 * 
 */
class ObjectIterationResultSetHandlerTest {

    int count;

    List<Integer> list = new ArrayList<Integer>();

    /**
     * @throws Exception
     * 
     */
    @Test
    void testHandle() throws Exception {
        ObjectIterationResultSetHandler handler = new ObjectIterationResultSetHandler(
                ValueTypes.INTEGER, 0,
                new IterationCallback<Integer, Integer>() {

                    @Override
                    public Integer iterate(Integer entity,
                            IterationContext context) {
                        ++count;
                        list.add(entity);
                        return count;
                    }
                });
        MockResultSet rs = createResultSet();
        Integer count = (Integer) handler.handle(rs);
        assertEquals(Integer.valueOf(4), count);
        assertEquals(4, list.size());

        assertEquals(Integer.valueOf(1), list.get(0));
        assertEquals(Integer.valueOf(2), list.get(1));
        assertEquals(Integer.valueOf(3), list.get(2));
        assertEquals(Integer.valueOf(4), list.get(3));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    void testHandle_WithLimit() throws Exception {
        ObjectIterationResultSetHandler handler = new ObjectIterationResultSetHandler(
                ValueTypes.INTEGER, 2,
                new IterationCallback<Integer, Integer>() {

                    @Override
                    public Integer iterate(Integer entity,
                            IterationContext context) {
                        ++count;
                        list.add(entity);
                        return count;
                    }
                });
        MockResultSet rs = createResultSet();
        Integer count = (Integer) handler.handle(rs);
        assertEquals(Integer.valueOf(2), count);
        assertEquals(2, list.size());

        assertEquals(Integer.valueOf(1), list.get(0));
        assertEquals(Integer.valueOf(2), list.get(1));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    void testHandle_WithExit() throws Exception {
        ObjectIterationResultSetHandler handler = new ObjectIterationResultSetHandler(
                ValueTypes.INTEGER, 0,
                new IterationCallback<Integer, Integer>() {

                    @Override
                    public Integer iterate(Integer entity,
                            IterationContext context) {
                        ++count;
                        list.add(entity);
                        if (list.size() == 2) {
                            context.setExit(true);
                        }
                        return count;
                    }
                });
        MockResultSet rs = createResultSet();
        Integer count = (Integer) handler.handle(rs);
        assertEquals(Integer.valueOf(2), count);
        assertEquals(2, list.size());

        assertEquals(Integer.valueOf(1), list.get(0));
        assertEquals(Integer.valueOf(2), list.get(1));
    }

    /**
     * @return
     */
    private MockResultSet createResultSet() {
        MockResultSetMetaData rsMeta = new MockResultSetMetaData();
        MockColumnMetaData columnMeta = new MockColumnMetaData();
        columnMeta.setColumnLabel("AAA");
        rsMeta.addColumnMetaData(columnMeta);
        MockResultSet rs = new MockResultSet(rsMeta);

        ArrayMap data = new ArrayMap();
        data.put("AAA", Integer.valueOf(1));
        rs.addRowData(data);

        data = new ArrayMap();
        data.put("AAA", Integer.valueOf(2));
        rs.addRowData(data);

        data = new ArrayMap();
        data.put("AAA", Integer.valueOf(3));
        rs.addRowData(data);

        data = new ArrayMap();
        data.put("AAA", Integer.valueOf(4));
        rs.addRowData(data);
        return rs;
    }

}
