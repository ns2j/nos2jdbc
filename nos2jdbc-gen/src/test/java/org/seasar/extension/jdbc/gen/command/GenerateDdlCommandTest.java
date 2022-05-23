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
package org.seasar.extension.jdbc.gen.command;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.seasar.extension.jdbc.gen.command.GenerateDdlCommand;
import org.seasar.extension.jdbc.gen.exception.RequiredPropertyNullRuntimeException;

/**
 * @author taedium
 * 
 */
class GenerateDdlCommandTest {

    /**
     * 
     * @throws Exception
     */
    @Test
    void validate() throws Exception {
        GenerateDdlCommand command = new GenerateDdlCommand();
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
    void init() throws Exception {
        GenerateDdlCommand command = new GenerateDdlCommand();
        command.setClasspathDir(new File("dir"));
        command.validate();
        command.init();
        assertNotNull(command.dialect);
        assertNotNull(command.ddlVersionDirectoryTree);
        assertNotNull(command.ddlVersionIncrementer);
        assertNotNull(command.tableModelFactory);
        assertNotNull(command.generator);
        assertNotNull(command.entityMetaReader);
        assertNotNull(command.databaseDescFactory);
        assertNotNull(command.sqlUnitExecutor);
        assertNotNull(command.generator);
    }
}
