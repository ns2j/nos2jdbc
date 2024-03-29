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
package org.seasar.extension.jdbc.gen.model.factory;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.gen.model.EntityTestModel;

/**
 * @author taedium
 * 
 */
class EntityTestModelFactoryTest {

    private EntityTestModelFactory factory;

    /**
     * 
     * @throws Exception
     */
    @BeforeEach
    public void setUp() throws Exception {
        //i	
        factory = new EntityTestModelFactory("jdbcManager", "Test", new NamesModelFactory("hoge.entity", "Names"), true, "rootpackagename", "none");
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    void testGetEntityTestModel() throws Exception {
        EntityMeta entityMeta = new EntityMeta();
        entityMeta.setName("Foo");
        entityMeta.setEntityClass(getClass());
        EntityTestModel entityTestModel = factory.getEntityTestModel(entityMeta);
        //i        assertEquals("s2jdbc.dicon", entityTestModel.getConfigPath());
        assertEquals("jdbcManager", entityTestModel.getJdbcManagerName());
        assertEquals("org.seasar.extension.jdbc.gen.model.factory", entityTestModel.getPackageName());
        assertEquals("FooTest", entityTestModel.getShortClassName());
        assertEquals("Foo", entityTestModel.getShortEntityClassName());
        //i        assertEquals(3, entityTestModel.getImportNameSet().size());
        assertEquals(2, entityTestModel.getImportNameSet().size());
        assertEquals("rootpackagename", entityTestModel.getRootPackageName());
    }

    /**
     * 
     */
    @Test
    void testGetIdExpression_Boolean() {
        assertEquals("true", factory.getExpression(Boolean.class));
    }

    /**
     * 
     */
    @Test
    void testGetIdExpression_Character() {
        assertEquals("'a'", factory.getExpression(Character.class));
    }

    /**
     * 
     */
    @Test
    void testGetIdExpression_Byte() {
        assertEquals("(byte) 1", factory.getExpression(Byte.class));
    }

    /**
     * 
     */
    @Test
    void testGetIdExpression_Short() {
        assertEquals("(short) 1", factory.getExpression(Short.class));
    }

    /**
     * 
     */
    @Test
    void testGetIdExpression_Integer() {
        assertEquals("1", factory.getExpression(Integer.class));
    }

    /**
     * 
     */
    @Test
    void testGetIdExpression_Long() {
        assertEquals("1L", factory.getExpression(Long.class));
    }

    /**
     * 
     */
    @Test
    void testGetIdExpression_Float() {
        assertEquals("1f", factory.getExpression(Float.class));
    }

    /**
     * 
     */
    @Test
    void testGetIdExpression_Double() {
        assertEquals("1d", factory.getExpression(Double.class));
    }

    /**
     * 
     */
    @Test
    void testGetIdExpression_BigDecimal() {
        assertEquals("BigDecimal.ONE", factory.getExpression(BigDecimal.class));
    }

    /**
     * 
     */
    @Test
    void testGetIdExpression_BigInteger() {
        assertEquals("BigInteger.ONE", factory.getExpression(BigInteger.class));
    }

    /**
     * 
     */
    @Test
    void testGetIdExpression_String() {
        assertEquals("\"aaa\"", factory.getExpression(String.class));
    }

    /**
     * 
     */
    @Test
    void testGetIdExpression_Date() {
        assertEquals("new Date()", factory.getExpression(Date.class));
    }

    /**
     * 
     */
    @Test
    void testGetIdExpression_Calender() {
        assertEquals("Calendar.getInstance()", factory.getExpression(Calendar.class));
    }

    /**
     * 
     */
    @Test
    void testGetIdExpression_sqlDate() {
        assertEquals("Date.valueOf(\"2008-01-01\")", factory.getExpression(java.sql.Date.class));
    }

    /**
     * 
     */
    @Test
    void testGetIdExpression_Time() {
        assertEquals("Time.valueOf(\"12:00:00\")", factory.getExpression(Time.class));
    }

    /**
     * 
     */
    @Test
    void testGetIdExpression_Timestamp() {
        assertEquals("Timestamp.valueOf(\"2008-01-01 12:00:00\")", factory.getExpression(Timestamp.class));
    }

    /**
     * 
     */
    @Test
    void testGetIdExpression_bytes() {
        assertEquals("new byte[0]", factory.getExpression(byte[].class));
    }
}
