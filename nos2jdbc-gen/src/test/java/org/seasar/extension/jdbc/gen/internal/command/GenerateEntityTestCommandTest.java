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
package org.seasar.extension.jdbc.gen.internal.command;

import java.io.File;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.seasar.extension.jdbc.gen.internal.exception.RequiredPropertyNullRuntimeException;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author taedium
 * 
 */
class GenerateEntityTestCommandTest {

    /**
     * 
     * @throws Exception
     */
    @AfterEach
    public void tearDown() throws Exception {
    //i        SingletonS2ContainerFactory.destroy();
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    void testValidate() throws Exception {
        GenerateEntityTestCommand command = new GenerateEntityTestCommand();
        command.setConfigPath("s2jdbc-gen-core-test.dicon");
        try {
            command.validate();
            fail();
        } catch (RequiredPropertyNullRuntimeException expected) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    void testInit() throws Exception {
        GenerateEntityTestCommand command = new GenerateEntityTestCommand();
        command.setConfigPath("s2jdbc-gen-core-test.dicon");
        command.setClasspathDir(new File("dir"));
        command.validate();
        command.init();
        assertNotNull(command.entityMetaReader);
        assertNotNull(command.namesModelFactory);
        assertNotNull(command.entityTestModelFactory);
        assertNotNull(command.generator);
    }
}
