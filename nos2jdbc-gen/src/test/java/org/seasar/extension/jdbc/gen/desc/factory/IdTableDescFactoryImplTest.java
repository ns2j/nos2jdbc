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

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.TableMeta;
import org.seasar.extension.jdbc.gen.desc.ColumnDesc;
import org.seasar.extension.jdbc.gen.desc.TableDesc;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.dialect.StandardGenDialect;
import org.seasar.extension.jdbc.meta.ColumnMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.PropertyMetaFactoryImpl;
import org.seasar.framework.convention.PersistenceConvention;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.TableGenerator;

/**
 * @author taedium
 * 
 */
@TableGenerator(name = "generator2", catalog = "FFF", schema = "GGG", table = "HHH", pkColumnName = "III", valueColumnName = "JJJ")
class IdTableDescFactoryImplTest {

    private PropertyMetaFactoryImpl propertyMetaFactory;

    private IdTableDescFactory idTableDescFactory;

    @SuppressWarnings("unused")
    @Id
    @TableGenerator(name = "generator", catalog = "AAA", schema = "BBB", table = "CCC", pkColumnName = "DDD", valueColumnName = "EEE")
    @GeneratedValue(generator = "generator", strategy = GenerationType.TABLE)
    private Integer tableId;

    @SuppressWarnings("unused")
    @Id
    @GeneratedValue(generator = "generator2", strategy = GenerationType.TABLE)
    private Integer classAnnotatedTableId;

    @SuppressWarnings("unused")
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Integer noGeneraterTableId;

    /**
     * 
     * @throws Exception
     */
    @BeforeEach
    public void setUp() throws Exception {
        PersistenceConvention pc = new PersistenceConventionImpl();
        ColumnMetaFactoryImpl cmf = new ColumnMetaFactoryImpl();
        cmf.setPersistenceConvention(pc);
        propertyMetaFactory = new PropertyMetaFactoryImpl();
        propertyMetaFactory.setPersistenceConvention(pc);
        propertyMetaFactory.setColumnMetaFactory(cmf);
        GenDialect dialect = new StandardGenDialect();
        UniqueKeyDescFactory ukdf = new UniqueKeyDescFactory(dialect);
        idTableDescFactory = new IdTableDescFactory(dialect, ukdf);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    void testGetTableDesc_tableId() throws Exception {
        Field field = getClass().getDeclaredField("tableId");
        TableMeta tableMeta = new TableMeta();
        tableMeta.setCatalog("HOGE");
        tableMeta.setSchema("FOO");
        tableMeta.setName("BAR");
        EntityMeta entityMeta = new EntityMeta();
        entityMeta.setTableMeta(tableMeta);
        PropertyMeta propertyMeta = propertyMetaFactory.createPropertyMeta(field, entityMeta);
        TableDesc tableDesc = idTableDescFactory.getTableDesc(entityMeta, propertyMeta);
        assertEquals("AAA", tableDesc.getCatalogName());
        assertEquals("BBB", tableDesc.getSchemaName());
        assertEquals("CCC", tableDesc.getName());
        assertNotNull(tableDesc.getPrimaryKeyDesc());
        assertEquals(2, tableDesc.getColumnDescList().size());
        ColumnDesc columnDesc = tableDesc.getColumnDescList().get(0);
        assertEquals("DDD", columnDesc.getName());
        assertEquals("varchar(255)", columnDesc.getDefinition());
        columnDesc = tableDesc.getColumnDescList().get(1);
        assertEquals("EEE", columnDesc.getName());
        assertEquals("bigint", columnDesc.getDefinition());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    void testGetTableDesc_classAnnotatedTableId() throws Exception {
        Field field = getClass().getDeclaredField("classAnnotatedTableId");
        TableMeta tableMeta = new TableMeta();
        tableMeta.setCatalog("HOGE");
        tableMeta.setSchema("FOO");
        tableMeta.setName("BAR");
        EntityMeta entityMeta = new EntityMeta();
        entityMeta.setTableMeta(tableMeta);
        entityMeta.setEntityClass(getClass());
        PropertyMeta propertyMeta = propertyMetaFactory.createPropertyMeta(field, entityMeta);
        TableDesc tableDesc = idTableDescFactory.getTableDesc(entityMeta, propertyMeta);
        assertEquals("FFF", tableDesc.getCatalogName());
        assertEquals("GGG", tableDesc.getSchemaName());
        assertEquals("HHH", tableDesc.getName());
        assertNotNull(tableDesc.getPrimaryKeyDesc());
        assertEquals(2, tableDesc.getColumnDescList().size());
        ColumnDesc columnDesc = tableDesc.getColumnDescList().get(0);
        assertEquals("III", columnDesc.getName());
        assertEquals("varchar(255)", columnDesc.getDefinition());
        columnDesc = tableDesc.getColumnDescList().get(1);
        assertEquals("JJJ", columnDesc.getName());
        assertEquals("bigint", columnDesc.getDefinition());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    void testGetTableDesc_noGeneraterTableId() throws Exception {
        Field field = getClass().getDeclaredField("noGeneraterTableId");
        TableMeta tableMeta = new TableMeta();
        tableMeta.setCatalog("HOGE");
        tableMeta.setSchema("FOO");
        tableMeta.setName("BAR");
        EntityMeta entityMeta = new EntityMeta();
        entityMeta.setTableMeta(tableMeta);
        PropertyMeta propertyMeta = propertyMetaFactory.createPropertyMeta(field, entityMeta);
        TableDesc tableDesc = idTableDescFactory.getTableDesc(entityMeta, propertyMeta);
        assertEquals("HOGE", tableDesc.getCatalogName());
        assertEquals("FOO", tableDesc.getSchemaName());
        assertEquals("ID_GENERATOR", tableDesc.getName());
        assertNotNull(tableDesc.getPrimaryKeyDesc());
        assertEquals(2, tableDesc.getColumnDescList().size());
        assertEquals("PK", tableDesc.getColumnDescList().get(0).getName());
        assertEquals("VALUE1", tableDesc.getColumnDescList().get(1).getName());
    }
}
