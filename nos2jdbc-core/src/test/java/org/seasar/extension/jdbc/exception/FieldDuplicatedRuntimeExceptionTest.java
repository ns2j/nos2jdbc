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
package org.seasar.extension.jdbc.exception;

import java.lang.reflect.Field;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author taedium
 * 
 */
class FieldDuplicatedRuntimeExceptionTest {

    @SuppressWarnings("unused")
    private String hoge;

    /**
     * 
     * @throws Exception
     */
    @Test
    void testAll() throws Exception {
        Field field = getClass().getDeclaredField("hoge");
        FieldDuplicatedRuntimeException ex = new FieldDuplicatedRuntimeException(
                field);
        System.out.println(ex.getMessage());
        assertEquals(field, field);
    }
}
