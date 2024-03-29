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

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.seasar.extension.jdbc.gen.desc.AttributeDesc;
import org.seasar.extension.jdbc.gen.desc.EntityDesc;
import org.seasar.extension.jdbc.gen.model.EntityModel;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;

/**
 * @author taedium
 * 
 */
class EntityModelFactoryTest {

    private EntityModelFactory factory = new EntityModelFactory("aaa.bbb", Superclass.class, new AttributeModelFactory(true, true, true, new PersistenceConventionImpl()), new AssociationModelFactory(true), new CompositeUniqueConstraintModelFactory(), false, false, true, true, true);

    /**
     * 
     * @throws Exception
     */
    @Test
    void testGetEntityModel() throws Exception {
        AttributeDesc id = new AttributeDesc();
        id.setName("id");
        id.setId(true);
        id.setGenerationType(GenerationType.AUTO);
        id.setAttributeClass(int.class);
        AttributeDesc name = new AttributeDesc();
        name.setName("name");
        name.setAttributeClass(String.class);
        AttributeDesc lob = new AttributeDesc();
        lob.setName("lob");
        lob.setLob(true);
        lob.setAttributeClass(byte[].class);
        AttributeDesc date = new AttributeDesc();
        date.setName("date");
        date.setAttributeClass(java.sql.Date.class);
        AttributeDesc temp = new AttributeDesc();
        temp.setName("temp");
        temp.setTransient(true);
        temp.setAttributeClass(String.class);
        AttributeDesc version = new AttributeDesc();
        version.setName("version");
        version.setVersion(true);
        version.setAttributeClass(Integer.class);
        EntityDesc entityDesc = new EntityDesc();
        entityDesc.setCatalogName("AAA");
        entityDesc.setSchemaName("BBB");
        entityDesc.setTableName("FOO");
        entityDesc.setName("Foo");
        entityDesc.addAttributeDesc(id);
        entityDesc.addAttributeDesc(name);
        entityDesc.addAttributeDesc(lob);
        entityDesc.addAttributeDesc(date);
        entityDesc.addAttributeDesc(temp);
        entityDesc.addAttributeDesc(version);
        EntityModel model = factory.getEntityModel(entityDesc);
        assertEquals("aaa.bbb", model.getPackageName());
        assertEquals("Foo", model.getShortClassName());
        assertEquals("AAA", model.getCatalogName());
        assertEquals("BBB", model.getSchemaName());
        assertEquals("FOO", model.getTableName());
        Set<String> set = model.getImportNameSet();
        assertEquals(15, set.size());
        Iterator<String> iterator = set.iterator();
        assertEquals(Generated.class.getCanonicalName(), iterator.next());
        assertEquals(Column.class.getCanonicalName(), iterator.next());
        assertEquals(Entity.class.getCanonicalName(), iterator.next());
        assertEquals(GeneratedValue.class.getCanonicalName(), iterator.next());
        assertEquals(GenerationType.class.getCanonicalName(), iterator.next());
        assertEquals(Id.class.getCanonicalName(), iterator.next());
        assertEquals(Lob.class.getCanonicalName(), iterator.next());
        assertEquals(Table.class.getCanonicalName(), iterator.next());
        assertEquals(Temporal.class.getCanonicalName(), iterator.next());
        assertEquals(TemporalType.class.getCanonicalName(), iterator.next());
        assertEquals(Transient.class.getCanonicalName(), iterator.next());
        assertEquals(Version.class.getCanonicalName(), iterator.next());
        assertEquals(Serializable.class.getCanonicalName(), iterator.next());
        assertEquals(Date.class.getCanonicalName(), iterator.next());
        assertEquals(Superclass.class.getCanonicalName(), iterator.next());
    }

    /** */
    public static class Superclass {
    }
}
