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
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ResourceUtil;

/**
 * @author taedium
 * 
 */
class GeneratorImplTest {

    private GeneratorImplStub generator;

    /**
     * 
     * @throws Exception
     */
    @BeforeEach
    public void setUp() throws Exception {
        String packageName = ClassUtil.getPackageName(getClass());
        File dir = ResourceUtil.getResourceAsFile(packageName.replace('.', '/'));
        generator = new GeneratorImplStub("UTF-8", dir);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    void testGenerate() throws Exception {
        GenerationContext context = new GenerationContext(new MyModel("hoge"), new File("file"), getClass().getSimpleName() + "_Generate.ftl", "UTF-8", false);
        generator.generate(context);
        assertEquals("hoge", generator.getResult());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    void testInclude() throws Exception {
        Foo foo = new Foo("foo");
        Hoge hoge = new Hoge("hoge", foo);
        GenerationContext context = new GenerationContext(hoge, new File("file"), getClass().getSimpleName() + "_Include.ftl", "UTF-8", false);
        generator.generate(context);
        assertEquals("hoge foo hoge", generator.getResult());
    }

    /**
     * 
     * @author taedium
     */
    public static class MyModel {

        private String name;

        /**
         * 
         * @param name
         */
        public MyModel(String name) {
            this.name = name;
        }

        /**
         * 
         * @return
         */
        public String getName() {
            return name;
        }
    }

    /**
     * 
     * @author taedium
     */
    public static class Hoge {

        private String name;

        private Foo foo;

        /**
         * 
         * @param name
         * @param foo
         */
        public Hoge(String name, Foo foo) {
            this.name = name;
            this.foo = foo;
        }

        /**
         * 
         * @return
         */
        public String getName() {
            return name;
        }

        /**
         * @return Returns the foo.
         */
        public Foo getFoo() {
            return foo;
        }
    }

    /**
     * 
     * @author taedium
     */
    public static class Foo {

        private String name;

        /**
         * 
         * @param name
         */
        public Foo(String name) {
            this.name = name;
        }

        /**
         * 
         * @return
         */
        public String getName() {
            return name;
        }
    }
}
