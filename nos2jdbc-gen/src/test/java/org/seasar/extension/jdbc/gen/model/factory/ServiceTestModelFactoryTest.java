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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.gen.model.ServiceTestModel;
import org.seasar.extension.jdbc.gen.model.factory.ServiceTestModelFactory;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author taedium
 * 
 */
class ServiceTestModelFactoryTest {

    private ServiceTestModelFactory factory;

    /**
     * 
     * @throws Exception
     */
    @BeforeEach
    public void setUp() throws Exception {
        //i	
        factory = new ServiceTestModelFactory("hoge", "Service", "Test", "rootpackagename", "none");
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    void testGetServiceTestModel() throws Exception {
        EntityMeta entityMeta = new EntityMeta();
        entityMeta.setEntityClass(Foo.class);
        ServiceTestModel model = factory.getServiceTestModel(entityMeta);
        //i        assertEquals("app.dicon", model.getConfigPath());
        assertEquals("hoge", model.getPackageName());
        assertEquals("FooServiceTest", model.getShortClassName());
        assertEquals("FooService", model.getShortServiceClassName());
        //i
        assertEquals("rootpackagename", model.getRootPackageName());
    }
}
