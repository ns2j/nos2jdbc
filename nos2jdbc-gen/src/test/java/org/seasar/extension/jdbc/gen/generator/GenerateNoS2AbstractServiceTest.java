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
import org.seasar.extension.jdbc.gen.model.NoS2AbstServiceModel;
import org.seasar.extension.jdbc.gen.model.factory.NoS2AbstServiceModelFactory;
import org.seasar.framework.util.TextUtil;

/**
 * @author taedium
 * 
 */
class GenerateNoS2AbstractServiceTest {

    private NoS2AbstServiceModelFactory abstServiceModelFactory;

    private GeneratorImplStub generator;

    /**
     * 
     * @throws Exception
     */
    @BeforeEach
    public void setUp() throws Exception {
        abstServiceModelFactory = new NoS2AbstServiceModelFactory("hoge.service", "Service", "none");
        generator = new GeneratorImplStub();
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    void test() throws Exception {
        NoS2AbstServiceModel model = abstServiceModelFactory.getAbstServiceModel();
        GenerationContext context = new GenerationContext(model, new File("file"), "java/nos2-abstract-service.ftl", "UTF-8", false);
        generator.generate(context);
        String path = getClass().getName().replace(".", "/") + ".txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }
}
