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
package org.seasar.extension.jdbc.gen.desc.factory;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seasar.extension.jdbc.gen.desc.AttributeDesc;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.dialect.StandardGenDialect;
import org.seasar.extension.jdbc.gen.meta.DbColumnMeta;
import org.seasar.extension.jdbc.gen.meta.DbTableMeta;
import org.seasar.framework.convention.PersistenceConvention;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;

import jakarta.persistence.GenerationType;
import jakarta.persistence.TemporalType;

/**
 * @author taedium
 * 
 */
class AttributeDescFactoryImplTest {

    private AttributeDescFactory factory;

    /**
     * 
     */
    @BeforeEach
    public void setUp() {
        PersistenceConvention convention = new PersistenceConventionImpl();
        GenDialect dialect = new StandardGenDialect();
        factory = new AttributeDescFactory(convention, dialect, "VERSION([_]?NO)?", "CREATED_AT", "UPDATED_AT", GenerationType.TABLE, 100, 200);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    void testName() throws Exception {
        DbColumnMeta columnMeta = new DbColumnMeta();
        columnMeta.setName("HOGE");
        columnMeta.setTypeName("varchar");
        AttributeDesc attributeDesc = factory.getAttributeDesc(new DbTableMeta(), columnMeta);
        assertEquals("hoge", attributeDesc.getName());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    void testIsId_integer() throws Exception {
        DbColumnMeta columnMeta = new DbColumnMeta();
        columnMeta.setName("hoge");
        columnMeta.setTypeName("integer");
        columnMeta.setPrimaryKey(true);
        AttributeDesc attributeDesc = factory.getAttributeDesc(new DbTableMeta(), columnMeta);
        assertTrue(attributeDesc.isId());
        assertEquals(GenerationType.TABLE, attributeDesc.getGenerationType());
        assertEquals(100, attributeDesc.getInitialValue());
        assertEquals(200, attributeDesc.getAllocationSize());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    void testIsId_varchar() throws Exception {
        DbColumnMeta columnMeta = new DbColumnMeta();
        columnMeta.setName("hoge");
        columnMeta.setTypeName("varchar");
        columnMeta.setPrimaryKey(true);
        AttributeDesc attributeDesc = factory.getAttributeDesc(new DbTableMeta(), columnMeta);
        assertTrue(attributeDesc.isId());
        assertNull(attributeDesc.getGenerationType());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    void testIsId_integer_autoIncrement() throws Exception {
        DbColumnMeta columnMeta = new DbColumnMeta();
        columnMeta.setName("hoge");
        columnMeta.setTypeName("integer");
        columnMeta.setPrimaryKey(true);
        columnMeta.setAutoIncrement(true);
        AttributeDesc attributeDesc = factory.getAttributeDesc(new DbTableMeta(), columnMeta);
        assertTrue(attributeDesc.isId());
        assertEquals(GenerationType.IDENTITY, attributeDesc.getGenerationType());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    void testIsId_varchar_autoIncrement() throws Exception {
        DbColumnMeta columnMeta = new DbColumnMeta();
        columnMeta.setName("hoge");
        columnMeta.setTypeName("varchar");
        columnMeta.setPrimaryKey(true);
        columnMeta.setAutoIncrement(true);
        AttributeDesc attributeDesc = factory.getAttributeDesc(new DbTableMeta(), columnMeta);
        assertTrue(attributeDesc.isId());
        assertNull(attributeDesc.getGenerationType());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    void testGetAttributeClass() throws Exception {
        DbColumnMeta columnMeta = new DbColumnMeta();
        columnMeta.setName("hoge");
        columnMeta.setTypeName("varchar");
        AttributeDesc attributeDesc = factory.getAttributeDesc(new DbTableMeta(), columnMeta);
        assertEquals(String.class, attributeDesc.getAttributeClass());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    void testGetTemporalType() throws Exception {
        DbColumnMeta columnMeta = new DbColumnMeta();
        columnMeta.setName("hoge");
        columnMeta.setTypeName("date");
        AttributeDesc attributeDesc = factory.getAttributeDesc(new DbTableMeta(), columnMeta);
        assertEquals(TemporalType.DATE, attributeDesc.getTemporalType());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    void testIsLob() throws Exception {
        DbColumnMeta columnMeta = new DbColumnMeta();
        columnMeta.setName("hoge");
        columnMeta.setTypeName("blob");
        AttributeDesc attributeDesc = factory.getAttributeDesc(new DbTableMeta(), columnMeta);
        assertTrue(attributeDesc.isLob());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    void testIsVersion_illegalType() throws Exception {
        DbColumnMeta columnMeta = new DbColumnMeta();
        columnMeta.setName("VERSION");
        columnMeta.setTypeName("verchar");
        AttributeDesc attributeDesc = factory.getAttributeDesc(new DbTableMeta(), columnMeta);
        assertFalse(attributeDesc.isVersion());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    void testIsVersion_VERSION() throws Exception {
        DbColumnMeta columnMeta = new DbColumnMeta();
        columnMeta.setName("VERSION");
        columnMeta.setTypeName("integer");
        AttributeDesc attributeDesc = factory.getAttributeDesc(new DbTableMeta(), columnMeta);
        assertTrue(attributeDesc.isVersion());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    void testIsVersion_VERSION_NO() throws Exception {
        DbColumnMeta columnMeta = new DbColumnMeta();
        columnMeta.setName("VERSION_NO");
        columnMeta.setTypeName("integer");
        AttributeDesc attributeDesc = factory.getAttributeDesc(new DbTableMeta(), columnMeta);
        assertTrue(attributeDesc.isVersion());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    void testIsVersion_VERSIONNO() throws Exception {
        DbColumnMeta columnMeta = new DbColumnMeta();
        columnMeta.setName("VERSIONNO");
        columnMeta.setTypeName("integer");
        AttributeDesc attributeDesc = factory.getAttributeDesc(new DbTableMeta(), columnMeta);
        assertTrue(attributeDesc.isVersion());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    void testColumn() throws Exception {
        DbColumnMeta columnMeta = new DbColumnMeta();
        columnMeta.setName("HOGE");
        columnMeta.setTypeName("varchar");
        columnMeta.setLength(10);
        columnMeta.setScale(5);
        columnMeta.setNullable(true);
        AttributeDesc attributeDesc = factory.getAttributeDesc(new DbTableMeta(), columnMeta);
        assertEquals("HOGE", attributeDesc.getColumnName());
        assertEquals(10, attributeDesc.getLength());
        assertEquals(10, attributeDesc.getPrecision());
        assertEquals(5, attributeDesc.getScale());
        assertTrue(attributeDesc.isNullable());
        assertFalse(attributeDesc.isUnsupportedColumnType());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    void testColumn_unsupportedType() throws Exception {
        DbColumnMeta columnMeta = new DbColumnMeta();
        columnMeta.setName("HOGE");
        columnMeta.setTypeName("unsupported");
        columnMeta.setLength(10);
        columnMeta.setScale(5);
        columnMeta.setNullable(true);
        AttributeDesc attributeDesc = factory.getAttributeDesc(new DbTableMeta(), columnMeta);
        assertTrue(attributeDesc.isUnsupportedColumnType());
    }
}
