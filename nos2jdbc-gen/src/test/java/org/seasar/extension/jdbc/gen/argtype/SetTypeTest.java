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
package org.seasar.extension.jdbc.gen.argtype;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.seasar.extension.jdbc.gen.argtype.NumberType;
import org.seasar.extension.jdbc.gen.argtype.SetType;
import org.seasar.extension.jdbc.gen.argtype.StringType;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author taedium
 * 
 */
class SetTypeTest {

    private SetType<String> stringSetType = new SetType<String>(new StringType());

    private SetType<Integer> numberSetType = new SetType<Integer>(new NumberType<Integer>(Integer.class));

    /**
     * 
     */
    @Test
    void testToObject_stringValue() {
        Collection<? extends String> collection = stringSetType.toObject("['aaa','bbb']");
        assertEquals(2, collection.size());
        assertTrue(collection.contains("aaa"));
        assertTrue(collection.contains("bbb"));
    }

    /**
     * 
     */
    @Test
    void testToObject_integerValue() {
        Collection<? extends Integer> collection = numberSetType.toObject("[1,2]");
        assertEquals(2, collection.size());
        assertTrue(collection.contains(1));
        assertTrue(collection.contains(2));
    }

    /**
     * 
     */
    @Test
    void testToObject_empty() {
        Collection<? extends String> collection = stringSetType.toObject("[]");
        assertTrue(collection.isEmpty());
    }

    /**
     * 
     */
    @Test
    void testToText() {
        Set<String> set = new LinkedHashSet<String>();
        set.add("aaa");
        set.add("bbb");
        String s = stringSetType.toText(set);
        assertEquals("['aaa','bbb']", s);
    }

    /**
     * 
     */
    @Test
    void testToText_empty() {
        Set<String> set = new LinkedHashSet<String>();
        String s = stringSetType.toText(set);
        assertEquals("[]", s);
    }
}
