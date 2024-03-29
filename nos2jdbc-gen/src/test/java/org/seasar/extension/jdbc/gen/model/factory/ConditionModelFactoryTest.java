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

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.gen.model.ConditionModel;
import org.seasar.extension.jdbc.meta.ColumnMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.EntityMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.PropertyMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.TableMetaFactoryImpl;
import org.seasar.extension.jdbc.where.ComplexWhere;
import org.seasar.extension.jdbc.where.condition.AbstractEntityCondition;
import org.seasar.extension.jdbc.where.condition.NotNullableCondition;
import org.seasar.extension.jdbc.where.condition.NotNullableStringCondition;
import org.seasar.extension.jdbc.where.condition.NullableCondition;
import org.seasar.extension.jdbc.where.condition.NullableStringCondition;
import org.seasar.framework.convention.PersistenceConvention;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 * @author taedium
 * 
 */
class ConditionModelFactoryTest {

    private EntityMetaFactoryImpl entityMetaFactory;

    private ConditionModelFactory conditionModelfactory;

    /**
     * 
     * @throws Exception
     */
    @BeforeEach
    public void setUp() throws Exception {
        PersistenceConvention pc = new PersistenceConventionImpl();
        ColumnMetaFactoryImpl cmf = new ColumnMetaFactoryImpl();
        cmf.setPersistenceConvention(pc);
        PropertyMetaFactoryImpl propertyMetaFactory = new PropertyMetaFactoryImpl();
        propertyMetaFactory.setPersistenceConvention(pc);
        propertyMetaFactory.setColumnMetaFactory(cmf);
        TableMetaFactoryImpl tmf = new TableMetaFactoryImpl();
        tmf.setPersistenceConvention(pc);
        entityMetaFactory = new EntityMetaFactoryImpl();
        entityMetaFactory.setPersistenceConvention(pc);
        entityMetaFactory.setPropertyMetaFactory(propertyMetaFactory);
        entityMetaFactory.setTableMetaFactory(tmf);
        ConditionAttributeModelFactory attrFactory = new ConditionAttributeModelFactory();
        ConditionAssociationModelFactory assoFactory = new ConditionAssociationModelFactory("Condition");
        conditionModelfactory = new ConditionModelFactory(attrFactory, assoFactory, "aaa.bbb", "Condition");
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    void testGetConditionModel() throws Exception {
        EntityMeta entityMeta = entityMetaFactory.getEntityMeta(Aaa.class);
        ConditionModel model = conditionModelfactory.getConditionModel(entityMeta);
        assertEquals("aaa.bbb", model.getPackageName());
        assertEquals("ConditionModelFactoryTest$AaaCondition", model.getShortClassName());
        assertEquals(6, model.getConditionAttributeModelList().size());
        assertEquals(1, model.getConditionAssociationModelList().size());
        Set<String> set = model.getImportNameSet();
        assertEquals(8, set.size());
        Iterator<String> iterator = set.iterator();
        assertEquals(Generated.class.getCanonicalName(), iterator.next());
        assertEquals(Date.class.getCanonicalName(), iterator.next());
        assertEquals(ComplexWhere.class.getCanonicalName(), iterator.next());
        assertEquals(AbstractEntityCondition.class.getCanonicalName(), iterator.next());
        assertEquals(NotNullableCondition.class.getCanonicalName(), iterator.next());
        assertEquals(NotNullableStringCondition.class.getCanonicalName(), iterator.next());
        assertEquals(NullableCondition.class.getCanonicalName(), iterator.next());
        assertEquals(NullableStringCondition.class.getCanonicalName(), iterator.next());
    }

    /** */
    @Entity
    public static class Aaa {

        /** */
        @Id
        @Column(nullable = false)
        protected Integer id;

        /** */
        @Column(nullable = false)
        protected String name;

        /** */
        @Column(nullable = true)
        protected String nullableName;

        /** */
        @Temporal(TemporalType.DATE)
        @Column(nullable = false)
        protected Date date;

        /** */
        @Temporal(TemporalType.DATE)
        protected Date nullableDate;

        /** */
        @Column
        protected Integer bbbId;

        /** */
        @ManyToOne
        protected Bbb bbb;
    }

    /** */
    @Entity
    public static class Bbb {

        /** */
        @Id
        @Column(nullable = false)
        protected Integer id;
    }
}
