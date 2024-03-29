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
import org.seasar.extension.jdbc.gen.model.EntityTestModel;
import org.seasar.extension.jdbc.gen.model.factory.EntityTestModelFactory;
import org.seasar.extension.jdbc.gen.model.factory.NamesModelFactory;
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
class GenerateEntityTestTest {

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
    void testCompositeId() throws Exception {
        //i	
        EntityTestModelFactory entityTestModelFactory = new EntityTestModelFactory("jdbcManager", "Test", new NamesModelFactory("hoge.entity", "Names"), false, "rootpackagename", "none");
        EntityMeta entityMeta = entityMetaFactory.getEntityMeta(Ccc.class);
        EntityTestModel model = entityTestModelFactory.getEntityTestModel(entityMeta);
        GenerationContext context = new GenerationContext(model, new File("file"), "java/entitytest.ftl", "UTF-8", false);
        generator.generate(context);
        String path = getClass().getName().replace(".", "/") + "_CompositeId.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

    /**
     * 
     * @throws Exception
     */
    /*i    
    @Test
    void testCompositeId_s2junit4() throws Exception {
//i	
        EntityTestModelFactoryImpl entityTestModelFactory = new EntityTestModelFactoryImpl(
                "jdbcManager", "Test",
                new NamesModelFactoryImpl("hoge.entity", "Names"), false, "rootpackagename", "none", "");
        EntityMeta entityMeta = entityMetaFactory.getEntityMeta(Ccc.class);
        EntityTestModel model = entityTestModelFactory
                .getEntityTestModel(entityMeta);
        GenerationContextImpl context = new GenerationContextImpl(model, new File(
                "file"), "java/entitytest.ftl", "UTF-8", false);
        generator.generate(context);

        String path = getClass().getName().replace(".", "/")
                + "_CompositeId_s2junit4.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }
*/
    /**
     * 
     * @throws Exception
     */
    @Test
    void testNoId() throws Exception {
        //i
        EntityTestModelFactory entityTestModelFactory = new EntityTestModelFactory("jdbcManager", "Test", new NamesModelFactory("hoge.entity", "Names"), false, "rootpackagename", "none");
        EntityMeta entityMeta = entityMetaFactory.getEntityMeta(Ddd.class);
        EntityTestModel model = entityTestModelFactory.getEntityTestModel(entityMeta);
        GenerationContext context = new GenerationContext(model, new File("file"), "java/entitytest.ftl", "UTF-8", false);
        generator.generate(context);
        String path = getClass().getName().replace(".", "/") + "_NoId.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    void testLeftOuterJoin() throws Exception {
        //i
        EntityTestModelFactory entityTestModelFactory = new EntityTestModelFactory("jdbcManager", "Test", new NamesModelFactory("hoge.entity", "Names"), false, "rootpackagename", "none");
        EntityMeta entityMeta = entityMetaFactory.getEntityMeta(Aaa.class);
        EntityTestModel model = entityTestModelFactory.getEntityTestModel(entityMeta);
        GenerationContext context = new GenerationContext(model, new File("file"), "java/entitytest.ftl", "UTF-8", false);
        generator.generate(context);
        String path = getClass().getName().replace(".", "/") + "_LeftOuterJoin.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    void testLeftOuterJoin_useNamesClass() throws Exception {
        //i
        EntityTestModelFactory entityTestModelFactory = new EntityTestModelFactory("jdbcManager", "Test", new NamesModelFactory("hoge.entity", "Names"), true, "rootpackagename", "none");
        EntityMeta entityMeta = entityMetaFactory.getEntityMeta(Aaa.class);
        EntityTestModel model = entityTestModelFactory.getEntityTestModel(entityMeta);
        GenerationContext context = new GenerationContext(model, new File("file"), "java/entitytest.ftl", "UTF-8", false);
        generator.generate(context);
        String path = getClass().getName().replace(".", "/") + "_LeftOuterJoin_useNamesClass.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }
}
