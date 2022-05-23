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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.seasar.extension.jdbc.gen.argtype.ListType;
import org.seasar.extension.jdbc.gen.argtype.NumberType;
import org.seasar.extension.jdbc.gen.argtype.StringType;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author taedium
 * 
 */
class ListTypeTest {

    private ListType<String> stringListType = new ListType<String>(new StringType());

    private ListType<Integer> numberListType = new ListType<Integer>(new NumberType<Integer>(Integer.class));

    /**
     * 
     */
    @Test
    void testToObject_stringValue() {
        Collection<? extends String> collection = stringListType.toObject("['aaa','bbb']");
        assertEquals(2, collection.size());
        Iterator<? extends String> it = collection.iterator();
        assertEquals("aaa", it.next());
        assertEquals("bbb", it.next());
    }

    /**
     * 
     */
    @Test
    void testToObject_integerValue() {
        Collection<? extends Integer> collection = numberListType.toObject("[1,2]");
        assertEquals(2, collection.size());
        Iterator<? extends Integer> it = collection.iterator();
        assertEquals(Integer.valueOf(1), it.next());
        assertEquals(Integer.valueOf(2), it.next());
    }

    /**
     * 
     */
    @Test
    void testToObject_empty() {
        Collection<? extends String> collection = stringListType.toObject("[]");
        assertTrue(collection.isEmpty());
    }

    /**
     * 
     */
    @Test
    void testToText() {
        List<String> list = new ArrayList<String>();
        list.add("aaa");
        list.add("bbb");
        String s = stringListType.toText(list);
        assertEquals("['aaa','bbb']", s);
    }

    /**
     * 
     */
    @Test
    void testToText_empty() {
        List<String> list = new ArrayList<String>();
        String s = stringListType.toText(list);
        assertEquals("[]", s);
    }
}
