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

import org.seasar.extension.jdbc.exception.FieldNotGenericsRuntimeException;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author higa
 * 
 */
class FieldNotGenericsRuntimeExceptionTest {

    String aaa;

    /**
     * @throws Exception
     * 
     */
    @Test
    void testGetField() throws Exception {
        Field field = getClass().getDeclaredField("aaa");
        FieldNotGenericsRuntimeException ex = new FieldNotGenericsRuntimeException(
                field);
        System.out.println(ex);
        assertEquals(field, ex.getField());
    }

}
