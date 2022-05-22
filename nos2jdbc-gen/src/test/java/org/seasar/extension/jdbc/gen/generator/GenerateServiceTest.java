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
package org.seasar.extension.jdbc.gen.generator;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.gen.internal.model.NamesModelFactory;
import org.seasar.extension.jdbc.gen.internal.model.ServiceModelFactory;
import org.seasar.extension.jdbc.gen.model.ServiceModel;
import org.seasar.extension.jdbc.meta.ColumnMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.EntityMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.PropertyMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.TableMetaFactoryImpl;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;
import org.seasar.framework.util.TextUtil;

/**
 * @author taedium
 * 
 */
class GenerateServiceTest {

    private EntityMetaFactoryImpl entityMetaFactory;

    private GeneratorImplStub generator;

    /**
     * 
     * @throws Exception
     */
    @BeforeEach
    public void setUp() throws Exception {
        PersistenceConventionImpl pc = new PersistenceConventionImpl();
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
        generator = new GeneratorImplStub();
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    void testSingleId() throws Exception {
        EntityMeta entityMeta = entityMetaFactory.getEntityMeta(Aaa.class);
        ServiceModelFactory serviceModelFactory = new ServiceModelFactory("hoge.service", "Service", new NamesModelFactory("hoge.entity", "Names"), true, "jdbcManager", "none");
        ServiceModel model = serviceModelFactory.getServiceModel(entityMeta);
        GenerationContext context = new GenerationContext(model, new File("file"), "java/service.ftl", "UTF-8", false);
        generator.generate(context);
        String path = getClass().getName().replace(".", "/") + "_SingleId.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    void testCompositeId() throws Exception {
        EntityMeta entityMeta = entityMetaFactory.getEntityMeta(Ccc.class);
        ServiceModelFactory serviceModelFactory = new ServiceModelFactory("hoge.service", "Service", new NamesModelFactory("hoge.entity", "Names"), true, "jdbcManager", "none");
        ServiceModel model = serviceModelFactory.getServiceModel(entityMeta);
        GenerationContext context = new GenerationContext(model, new File("file"), "java/service.ftl", "UTF-8", false);
        generator.generate(context);
        String path = getClass().getName().replace(".", "/") + "_CompositeId.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    void testNoId() throws Exception {
        EntityMeta entityMeta = entityMetaFactory.getEntityMeta(Ddd.class);
        ServiceModelFactory serviceModelFactory = new ServiceModelFactory("hoge.service", "Service", new NamesModelFactory("hoge.entity", "Names"), true, "jdbcManager", "none");
        ServiceModel model = serviceModelFactory.getServiceModel(entityMeta);
        GenerationContext context = new GenerationContext(model, new File("file"), "java/service.ftl", "UTF-8", false);
        generator.generate(context);
        String path = getClass().getName().replace(".", "/") + "_NoId.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    void testJdbcManagerName() throws Exception {
        EntityMeta entityMeta = entityMetaFactory.getEntityMeta(Aaa.class);
        ServiceModelFactory serviceModelFactory = new ServiceModelFactory("hoge.service", "Service", new NamesModelFactory("hoge.entity", "Names"), true, "myJdbcManager", "none");
        ServiceModel model = serviceModelFactory.getServiceModel(entityMeta);
        GenerationContext context = new GenerationContext(model, new File("file"), "java/service.ftl", "UTF-8", false);
        generator.generate(context);
        String path = getClass().getName().replace(".", "/") + "_JdbcManagerName.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    void testServiceClassNameSuffix() throws Exception {
        EntityMeta entityMeta = entityMetaFactory.getEntityMeta(Aaa.class);
        ServiceModelFactory serviceModelFactory = new ServiceModelFactory("hoge.service", "Dao", new NamesModelFactory("hoge.entity", "Names"), true, "jdbcManager", "none");
        ServiceModel model = serviceModelFactory.getServiceModel(entityMeta);
        GenerationContext context = new GenerationContext(model, new File("file"), "java/service.ftl", "UTF-8", false);
        generator.generate(context);
        String path = getClass().getName().replace(".", "/") + "_ServiceClassNameSuffix.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }
}
