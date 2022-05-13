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
package org.seasar.framework.beans.converter;

import java.sql.Timestamp;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author higa
 * 
 */
class NumberConverterTest {

    /**
     * @throws Exception
     */
    @Test
    void testGetAsObject() throws Exception {
        NumberConverter converter = new NumberConverter("##0");
        assertEquals(Long.valueOf("100"), converter.getAsObject("100"));
    }

    /**
     * @throws Exception
     */
    @Test
    void testGetAsString() throws Exception {
        NumberConverter converter = new NumberConverter("##0");
        assertEquals("100", converter.getAsString(Integer.valueOf("100")));
    }

    /**
     * @throws Exception
     */
    @Test
    void testIsTarget() throws Exception {
        NumberConverter converter = new NumberConverter("##0");
        assertTrue(converter.isTarget(Integer.class));
        assertFalse(converter.isTarget(Timestamp.class));
    }
}