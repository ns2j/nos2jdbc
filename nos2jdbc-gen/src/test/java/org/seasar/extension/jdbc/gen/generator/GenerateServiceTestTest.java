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
import org.seasar.extension.jdbc.gen.model.ServiceTestModel;
import org.seasar.extension.jdbc.gen.model.factory.ServiceTestModelFactory;
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
class GenerateServiceTestTest {

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
    void test() throws Exception {
        EntityMeta entityMeta = entityMetaFactory.getEntityMeta(Aaa.class);
        ServiceTestModelFactory serviceTestModelFactory = new ServiceTestModelFactory("hoge.service", "Service", "Test", "rootpackagename", "none");
        ServiceTestModel model = serviceTestModelFactory.getServiceTestModel(entityMeta);
        GenerationContext context = new GenerationContext(model, new File("file"), "java/servicetest.ftl", "UTF-8", false);
        generator.generate(context);
        String path = getClass().getName().replace(".", "/") + ".txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }
    /**
     * 
     * @throws Exception
     */
    /*i    
    @Test
    void test_s2junit4() throws Exception {
        EntityMeta entityMeta = entityMetaFactory.getEntityMeta(Aaa.class);
        ServiceTestModelFactoryImpl serviceTestModelFactory = new ServiceTestModelFactoryImpl(
                "hoge.service", "Service", "Test", "rootpackagename", "none", "");
        ServiceTestModel model = serviceTestModelFactory
                .getServiceTestModel(entityMeta);
        GenerationContextImpl context = new GenerationContextImpl(model, new File(
                "file"), "java/servicetest.ftl", "UTF-8", false);
        generator.generate(context);
        String path = getClass().getName().replace(".", "/") + "_s2junit4.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }
*/
}
