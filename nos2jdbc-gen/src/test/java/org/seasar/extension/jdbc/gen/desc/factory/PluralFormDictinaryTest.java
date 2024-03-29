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
package org.seasar.extension.jdbc.gen.desc.factory;

import java.util.LinkedHashMap;
import org.junit.jupiter.api.Test;
import org.seasar.extension.jdbc.gen.desc.factory.PluralFormDictinary;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author taedium
 * 
 */
class PluralFormDictinaryTest {

    /**
     * 
     * @throws Exception
     */
    @Test
    void testLookup() throws Exception {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put("^person$", "people");
        map.put("(.*)$", "$1s");
        PluralFormDictinary dictinary = new PluralFormDictinary(map);
        assertEquals("people", dictinary.lookup("person"));
        assertEquals("employees", dictinary.lookup("employee"));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    void testLookup_notFound() throws Exception {
        PluralFormDictinary dictinary = new PluralFormDictinary();
        assertNull(dictinary.lookup("employee"));
    }
}
