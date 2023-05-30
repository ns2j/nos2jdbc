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
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.gen.model.ServiceModel;
import org.seasar.extension.jdbc.meta.ColumnMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.EntityMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.PropertyMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.TableMetaFactoryImpl;
import org.seasar.extension.jdbc.operation.Operations;
import org.seasar.framework.convention.PersistenceConvention;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;

import jakarta.annotation.Generated;
import jakarta.annotation.Resource;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 * @author taedium
 * 
 */
class ServiceModelFactoryTest {

    private EntityMetaFactoryImpl entityMetaFactory;

    private NamesModelFactory namesModelFactory;

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
        namesModelFactory = new NamesModelFactory("aaa.ccc", "Names");
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    void testSingleId() throws Exception {
        EntityMeta entityMeta = entityMetaFactory.getEntityMeta(Aaa.class);
        ServiceModelFactory serviceModelFactory = new ServiceModelFactory("aaa.bbb", "Service", namesModelFactory, true, "jdbcManager", "cdi");
        ServiceModel serviceModel = serviceModelFactory.getServiceModel(entityMeta);
        assertNotNull(serviceModel);
        assertEquals("aaa.bbb", serviceModel.getPackageName());
        assertEquals("ServiceModelFactoryTest$AaaService", serviceModel.getShortClassName());
        assertEquals("Aaa", serviceModel.getShortEntityClassName());
        assertEquals(1, serviceModel.getIdPropertyMetaList().size());
        assertEquals("jdbcManager", serviceModel.getJdbcManagerName());
        assertFalse(serviceModel.isJdbcManagerSetterNecessary());
        assertEquals(3, serviceModel.getImportNameSet().size());
        Iterator<String> iterator = serviceModel.getImportNameSet().iterator();
        assertEquals(Generated.class.getCanonicalName(), iterator.next());
        assertEquals(List.class.getCanonicalName(), iterator.next());
        assertEquals(Aaa.class.getCanonicalName(), iterator.next());
        assertEquals(2, serviceModel.getStaticImportNameSet().size());
        iterator = serviceModel.getStaticImportNameSet().iterator();
        assertEquals("aaa.ccc.ServiceModelFactoryTest$AaaNames.*", iterator.next());
        assertEquals(Operations.class.getCanonicalName() + ".*", iterator.next());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    void testCompositeId() throws Exception {
        EntityMeta entityMeta = entityMetaFactory.getEntityMeta(Bbb.class);
        ServiceModelFactory serviceModelFactory = new ServiceModelFactory("aaa.bbb", "Service", namesModelFactory, true, "jdbcManager", "cdi");
        ServiceModel serviceModel = serviceModelFactory.getServiceModel(entityMeta);
        assertNotNull(serviceModel);
        assertEquals("aaa.bbb", serviceModel.getPackageName());
        assertEquals("ServiceModelFactoryTest$BbbService", serviceModel.getShortClassName());
        assertEquals(2, serviceModel.getIdPropertyMetaList().size());
        assertEquals("jdbcManager", serviceModel.getJdbcManagerName());
        assertFalse(serviceModel.isJdbcManagerSetterNecessary());
        assertEquals(4, serviceModel.getImportNameSet().size());
        Iterator<String> iterator = serviceModel.getImportNameSet().iterator();
        assertEquals(Generated.class.getCanonicalName(), iterator.next());
        assertEquals(Date.class.getCanonicalName(), iterator.next());
        assertEquals(List.class.getCanonicalName(), iterator.next());
        assertEquals(Bbb.class.getCanonicalName(), iterator.next());
        assertEquals(2, serviceModel.getStaticImportNameSet().size());
        iterator = serviceModel.getStaticImportNameSet().iterator();
        assertEquals("aaa.ccc.ServiceModelFactoryTest$BbbNames.*", iterator.next());
        assertEquals(Operations.class.getCanonicalName() + ".*", iterator.next());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    void testJdbcManagerName() throws Exception {
        EntityMeta entityMeta = entityMetaFactory.getEntityMeta(Bbb.class);
        ServiceModelFactory serviceModelFactory = new ServiceModelFactory("aaa.bbb", "Service", namesModelFactory, true, "myJdbcManager", "cdi");
        ServiceModel serviceModel = serviceModelFactory.getServiceModel(entityMeta);
        assertNotNull(serviceModel);
        assertEquals("aaa.bbb", serviceModel.getPackageName());
        assertEquals("ServiceModelFactoryTest$BbbService", serviceModel.getShortClassName());
        assertEquals(2, serviceModel.getIdPropertyMetaList().size());
        assertEquals("myJdbcManager", serviceModel.getJdbcManagerName());
        assertTrue(serviceModel.isJdbcManagerSetterNecessary());
        assertEquals(8, serviceModel.getImportNameSet().size());
        Iterator<String> iterator = serviceModel.getImportNameSet().iterator();
        assertEquals(Generated.class.getCanonicalName(), iterator.next());
        assertEquals(Resource.class.getCanonicalName(), iterator.next());
        assertEquals(TransactionAttribute.class.getCanonicalName(), iterator.next());
        assertEquals(TransactionAttributeType.class.getCanonicalName(), iterator.next());
        assertEquals(Date.class.getCanonicalName(), iterator.next());
        assertEquals(List.class.getCanonicalName(), iterator.next());
        assertEquals(JdbcManager.class.getCanonicalName(), iterator.next());
        assertEquals(Bbb.class.getCanonicalName(), iterator.next());
        assertEquals(2, serviceModel.getStaticImportNameSet().size());
        iterator = serviceModel.getStaticImportNameSet().iterator();
        assertEquals("aaa.ccc.ServiceModelFactoryTest$BbbNames.*", iterator.next());
        assertEquals(Operations.class.getCanonicalName() + ".*", iterator.next());
    }

    /** */
    @Entity
    public static class Aaa {

        /** */
        @Id
        protected Integer id;

        /** */
        protected String name;
    }

    /** */
    @Entity
    public static class Bbb {

        /** */
        @Id
        @Column(nullable = false)
        protected Integer id1;

        /** */
        @Id
        @Temporal(TemporalType.DATE)
        @Column(nullable = false)
        protected Date id2;

        /** */
        protected String name;
    }
}
