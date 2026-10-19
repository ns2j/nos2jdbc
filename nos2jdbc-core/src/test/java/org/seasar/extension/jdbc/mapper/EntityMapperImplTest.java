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
package org.seasar.extension.jdbc.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.seasar.extension.jdbc.MappingContext;
import org.seasar.extension.jdbc.PropertyMapper;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.entity.Aaa;
import org.seasar.extension.jdbc.entity.Aaaa;
import org.seasar.extension.jdbc.entity.Bbb;
import org.seasar.extension.jdbc.entity.Ddd;

/**
 * @author higa
 * 
 */
class EntityMapperImplTest {

    /**
     * 
     * @throws Exception
     */
    @Test
    void testMap() throws Exception {
        Field field1 = Aaa.class.getDeclaredField("id");
        PropertyMapperImpl propertyMapper = createPropertyMapper(field1, 0);
        Field field2 = Aaa.class.getDeclaredField("name");
        PropertyMapperImpl propertyMapper2 = createPropertyMapper(field2, 1);
        Field field7 = Aaa.class.getDeclaredField("aaaa");
        Aaaa aaaa = new Aaaa();
        aaaa.bbbb = "AAAA";
        PropertyMapperImpl propertyMapper7 = createPropertyMapper(field7, 2);
        EntityMapperImpl entityMapper = new EntityMapperImpl(Aaa.class,
                new PropertyMapper[] { propertyMapper, propertyMapper2, propertyMapper7 },
                new int[] { 0 });

        Field field3 = Bbb.class.getDeclaredField("id");
        PropertyMapperImpl propertyMapper3 = createPropertyMapper(field3, 3);
        Field field4 = Bbb.class.getDeclaredField("name");
        PropertyMapperImpl propertyMapper4 = createPropertyMapper(field4, 4);
        OneToOneEntityMapperImpl bbbEntityMapper = new OneToOneEntityMapperImpl(
                Bbb.class, new PropertyMapper[] { propertyMapper3,
                        propertyMapper4 }, new int[] { 1 }, Aaa.class
                        .getDeclaredField("bbb"), Bbb.class
                        .getDeclaredField("aaa"));
        entityMapper.addRelationshipEntityMapper(bbbEntityMapper);

        Field field5 = Ddd.class.getDeclaredField("id");
        PropertyMapperImpl propertyMapper5 = createPropertyMapper(field5, 5);
        Field field6 = Ddd.class.getDeclaredField("name");
        PropertyMapperImpl propertyMapper6 = createPropertyMapper(field6, 6);
        OneToManyEntityMapperImpl dddsEntityMapper = new OneToManyEntityMapperImpl(
                Ddd.class, new PropertyMapper[] { propertyMapper5,
                        propertyMapper6 }, new int[] { 5 }, Bbb.class
                        .getDeclaredField("ddds"), Ddd.class
                        .getDeclaredField("bbb"));
        bbbEntityMapper.addRelationshipEntityMapper(dddsEntityMapper);

        MappingContext mappingContext = new MappingContext(10);
        Object[] values = new Object[] { 1, "AAA", aaaa, 11, "BBB", 111, "DDD" };
        Aaa aaa = (Aaa) entityMapper.map(values, mappingContext);
        assertNotNull(aaa);
        assertEquals(Integer.valueOf(1), aaa.id);
        assertEquals("AAA", aaa.name);
        Bbb bbb = aaa.bbb;
        assertNotNull(bbb);
        assertEquals(Integer.valueOf(11), bbb.id);
        assertEquals("BBB", bbb.name);
        assertSame(aaa, bbb.aaa);
        List<Ddd> ddds = bbb.ddds;
        assertNotNull(ddds);
        assertEquals(1, ddds.size());
        Ddd ddd = ddds.get(0);
        assertNotNull(ddd);
        assertEquals(Integer.valueOf(111), ddd.id);
        assertEquals("DDD", ddd.name);
        assertSame(bbb, ddd.bbb);

        Object[] values2 = new Object[] { 1, "AAA", aaaa, 11, "BBB", 222, "DDD2" };
        assertNull(entityMapper.map(values2, mappingContext));
        assertEquals(2, ddds.size());
        assertSame(ddd, ddds.get(0));
        Ddd ddd2 = ddds.get(1);
        assertNotNull(ddd2);
        assertEquals(Integer.valueOf(222), ddd2.id);
        assertEquals("DDD2", ddd2.name);
        assertSame(bbb, ddd2.bbb);

        Object[] values3 = new Object[] { 2, "AAA2", aaaa, 22, "BBB2", 333, "DDD3" };
        Aaa aaa2 = (Aaa) entityMapper.map(values3, mappingContext);
        assertNotNull(aaa2);
        assertEquals(Integer.valueOf(2), aaa2.id);
        assertEquals("AAA2", aaa2.name);
        Bbb bbb2 = aaa2.bbb;
        assertNotNull(bbb2);
        assertEquals(Integer.valueOf(22), bbb2.id);
        assertEquals("BBB2", bbb2.name);
        assertSame(aaa2, bbb2.aaa);
        List<Ddd> ddds2 = bbb2.ddds;
        assertNotNull(ddds2);
        assertEquals(1, ddds2.size());
        Ddd ddd3 = ddds2.get(0);
        assertNotNull(ddd3);
        assertEquals(Integer.valueOf(333), ddd3.id);
        assertEquals("DDD3", ddd3.name);
        assertSame(bbb2, ddd3.bbb);

        Object[] values4 = new Object[] { 1, "AAA", aaaa, 11, "BBB", 222, "DDD2" };
        assertNull(entityMapper.map(values4, mappingContext));
    }

    protected PropertyMapperImpl createPropertyMapper(Field field, int index) {
        PropertyMeta pm = new PropertyMeta();
        pm.setField(field);
        pm.setName(field.getName());
        return new PropertyMapperImpl(pm, index);
    }
}