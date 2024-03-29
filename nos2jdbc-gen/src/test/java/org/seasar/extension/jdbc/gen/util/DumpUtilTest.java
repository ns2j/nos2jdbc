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
package org.seasar.extension.jdbc.gen.util;

import org.junit.jupiter.api.Test;
import org.seasar.extension.jdbc.gen.exception.IllegalDumpValueRuntimeException;
import org.seasar.extension.jdbc.gen.util.DumpUtil;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author taedium
 * 
 */
class DumpUtilTest {

    /**
     * 
     */
    @Test
    void testEncode() {
        assertEquals("\"aa\na\"", DumpUtil.encode("aa\na"));
        assertEquals("\"aa\"\"a\"", DumpUtil.encode("aa\"a"));
        assertEquals("\"aaa\"", DumpUtil.encode("aaa"));
        assertEquals("\"\"", DumpUtil.encode(""));
        assertEquals("", DumpUtil.encode(null));
    }

    /**
     * 
     */
    @Test
    void testDecode() {
        assertEquals("aa\na", DumpUtil.decode("\"aa\na\""));
        assertEquals("aa\"a", DumpUtil.decode("\"aa\"\"a\""));
        assertEquals("", DumpUtil.decode("\"\""));
        assertNull(DumpUtil.decode(""));
        assertNull(DumpUtil.decode(null));
    }

    /**
     * 
     */
    @Test
    void testDecode_illegalValue() {
        try {
            DumpUtil.decode("\"aaa");
        } catch (IllegalDumpValueRuntimeException expected) {
        }
        try {
            DumpUtil.decode("aa,a");
        } catch (IllegalDumpValueRuntimeException expected) {
        }
        try {
            DumpUtil.decode("aa\na");
        } catch (IllegalDumpValueRuntimeException expected) {
        }
        try {
            DumpUtil.decode("aa\ra");
        } catch (IllegalDumpValueRuntimeException expected) {
        }
    }
}
