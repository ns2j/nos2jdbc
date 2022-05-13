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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.extension.jdbc.PropertyMapper;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.entity.Aaa;
import org.seasar.extension.jdbc.mapper.EntityMapperImpl;
import org.seasar.extension.jdbc.mapper.PropertyMapperImpl;
import org.seasar.extension.jdbc.types.ValueTypes;
import org.seasar.framework.mock.sql.MockColumnMetaData;
import org.seasar.framework.mock.sql.MockResultSet;
import org.seasar.framework.mock.sql.MockResultSetMetaData;
import org.seasar.framework.util.ArrayMap;

/**
 * @author higa
 * 
 */
class BeanIterationAutoResultSetHandlerTest {

    int count;

    List<Aaa> list = new ArrayList<Aaa>();

    /**
     * @throws Exception
     */
    @Test
    void testHandle() throws Exception {
        ValueType[] valueTypes = new ValueType[] { ValueTypes.INTEGER,
                ValueTypes.STRING };
        EntityMapperImpl entityMapper = createEntityMapper();

        BeanIterationAutoResultSetHandler handler = new BeanIterationAutoResultSetHandler(
                valueTypes, entityMapper, "select * from aaa", 0,
                new IterationCallback<Aaa, Integer>() {

                    @Override
                    public Integer iterate(Aaa entity, IterationContext context) {
                        ++count;
                        list.add(entity);
                        return count;
                    }
                });
        MockResultSet rs = createResultSet();

        Integer count = (Integer) handler.handle(rs);
        assertEquals(Integer.valueOf(4), count);
        assertEquals(4, list.size());

        Aaa aaa = list.get(0);
        assertEquals(Integer.valueOf(1), aaa.id);
        assertEquals("AAA", aaa.name);

        aaa = list.get(1);
        assertEquals(Integer.valueOf(2), aaa.id);
        assertEquals("BBB", aaa.name);

        aaa = list.get(2);
        assertEquals(Integer.valueOf(3), aaa.id);
        assertEquals("CCC", aaa.name);

        aaa = list.get(3);
        assertEquals(Integer.valueOf(4), aaa.id);
        assertEquals("DDD", aaa.name);
    }

    /**
     * @throws Exception
     */
    @Test
    void testHandle_WithLimit() throws Exception {
        ValueType[] valueTypes = new ValueType[] { ValueTypes.INTEGER,
                ValueTypes.STRING };
        EntityMapperImpl entityMapper = createEntityMapper();

        BeanIterationAutoResultSetHandler handler = new BeanIterationAutoResultSetHandler(
                valueTypes, entityMapper, "select * from aaa", 2,
                new IterationCallback<Aaa, Integer>() {

                    @Override
                    public Integer iterate(Aaa entity, IterationContext context) {
                        ++count;
                        list.add(entity);
                        return count;
                    }
                });
        MockResultSet rs = createResultSet();

        Integer count = (Integer) handler.handle(rs);
        assertEquals(Integer.valueOf(2), count);
        assertEquals(2, list.size());

        Aaa aaa = list.get(0);
        assertEquals(Integer.valueOf(1), aaa.id);
        assertEquals("AAA", aaa.name);

        aaa = list.get(1);
        assertEquals(Integer.valueOf(2), aaa.id);
        assertEquals("BBB", aaa.name);
    }

    /**
     * @throws Exception
     */
    @Test
    void testHandle_WithExit() throws Exception {
        ValueType[] valueTypes = new ValueType[] { ValueTypes.INTEGER,
                ValueTypes.STRING };
        EntityMapperImpl entityMapper = createEntityMapper();

        BeanIterationAutoResultSetHandler handler = new BeanIterationAutoResultSetHandler(
                valueTypes, entityMapper, "select * from aaa", 0,
                new IterationCallback<Aaa, Integer>() {

                    @Override
                    public Integer iterate(Aaa entity, IterationContext context) {
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

        Aaa aaa = list.get(0);
        assertEquals(Integer.valueOf(1), aaa.id);
        assertEquals("AAA", aaa.name);

        aaa = list.get(1);
        assertEquals(Integer.valueOf(2), aaa.id);
        assertEquals("BBB", aaa.name);
    }

    /**
     * @throws Exception
     */
    @Test
    void testHandle_WithToMany() throws Exception {
        ValueType[] valueTypes = new ValueType[] { ValueTypes.INTEGER,
                ValueTypes.STRING };
        EntityMapperImpl entityMapper = createEntityMapper();

        BeanIterationAutoResultSetHandler handler = new BeanIterationAutoResultSetHandler(
                valueTypes, entityMapper, "select * from aaa", 0,
                new IterationCallback<Aaa, Integer>() {

                    @Override
                    public Integer iterate(Aaa entity, IterationContext context) {
                        ++count;
                        list.add(entity);
                        return count;
                    }
                });
        MockResultSet rs = createResultSet2();

        Integer count = (Integer) handler.handle(rs);
        assertEquals(Integer.valueOf(3), count);
        assertEquals(3, list.size());

        Aaa aaa = list.get(0);
        assertEquals(Integer.valueOf(1), aaa.id);
        assertEquals("AAA", aaa.name);

        aaa = list.get(1);
        assertEquals(Integer.valueOf(3), aaa.id);
        assertEquals("CCC", aaa.name);

        aaa = list.get(2);
        assertEquals(Integer.valueOf(4), aaa.id);
        assertEquals("DDD", aaa.name);
    }

    private EntityMapperImpl createEntityMapper() throws NoSuchFieldException {
        Field field1 = Aaa.class.getDeclaredField("id");
        PropertyMapperImpl propertyMapper = new PropertyMapperImpl(field1, 0);
        Field field2 = Aaa.class.getDeclaredField("name");
        PropertyMapperImpl propertyMapper2 = new PropertyMapperImpl(field2, 1);
        EntityMapperImpl entityMapper = new EntityMapperImpl(Aaa.class,
                new PropertyMapper[] { propertyMapper, propertyMapper2 },
                new int[] { 0 });
        return entityMapper;
    }

    private MockResultSet createResultSet() {
        MockResultSetMetaData rsMeta = new MockResultSetMetaData();
        MockColumnMetaData columnMeta = new MockColumnMetaData();
        columnMeta.setColumnLabel("ID");
        rsMeta.addColumnMetaData(columnMeta);
        columnMeta = new MockColumnMetaData();
        columnMeta.setColumnLabel("NAME");
        rsMeta.addColumnMetaData(columnMeta);
        MockResultSet rs = new MockResultSet(rsMeta);

        ArrayMap data = new ArrayMap();
        data.put("ID", Integer.valueOf(1));
        data.put("NAME", "AAA");
        rs.addRowData(data);

        data = new ArrayMap();
        data.put("ID", Integer.valueOf(2));
        data.put("NAME", "BBB");
        rs.addRowData(data);

        data = new ArrayMap();
        data.put("ID", Integer.valueOf(3));
        data.put("NAME", "CCC");
        rs.addRowData(data);

        data = new ArrayMap();
        data.put("ID", Integer.valueOf(4));
        data.put("NAME", "DDD");
        rs.addRowData(data);
        return rs;
    }

    private MockResultSet createResultSet2() {
        MockResultSetMetaData rsMeta = new MockResultSetMetaData();
        MockColumnMetaData columnMeta = new MockColumnMetaData();
        columnMeta.setColumnLabel("ID");
        rsMeta.addColumnMetaData(columnMeta);
        columnMeta = new MockColumnMetaData();
        columnMeta.setColumnLabel("NAME");
        rsMeta.addColumnMetaData(columnMeta);
        MockResultSet rs = new MockResultSet(rsMeta);

        ArrayMap data = new ArrayMap();
        data.put("ID", Integer.valueOf(1));
        data.put("NAME", "AAA");
        rs.addRowData(data);

        data = new ArrayMap();
        data.put("ID", Integer.valueOf(1));
        data.put("NAME", "AAA");
        rs.addRowData(data);

        data = new ArrayMap();
        data.put("ID", Integer.valueOf(3));
        data.put("NAME", "CCC");
        rs.addRowData(data);

        data = new ArrayMap();
        data.put("ID", Integer.valueOf(4));
        data.put("NAME", "DDD");
        rs.addRowData(data);

        data = new ArrayMap();
        data.put("ID", Integer.valueOf(4));
        data.put("NAME", "DDD");
        rs.addRowData(data);

        return rs;
    }

}
