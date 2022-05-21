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

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.seasar.extension.jdbc.gen.internal.exception.RequiredPropertyEmptyRuntimeException;

/**
 * @author taedium
 * 
 */
class ExecuteSqlCommandTest {

    /**
     * 
     * @throws Exception
     */
    @Test
    void validate() throws Exception {
        ExecuteSqlCommand command = new ExecuteSqlCommand();
        try {
            command.validate();
            fail();
        } catch (RequiredPropertyEmptyRuntimeException expected) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    void init() throws Exception {
        ExecuteSqlCommand command = new ExecuteSqlCommand();
        command.getSqlFileList().add(new File("aaa"));
        command.validate();
        command.init();
        assertNotNull(command.sqlFileExecutor);
        assertNotNull(command.sqlUnitExecutor);
    }
}
