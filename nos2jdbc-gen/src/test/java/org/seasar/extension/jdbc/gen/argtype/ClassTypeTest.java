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

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * @author taedium
 * 
 */
class ClassTypeTest {

    private ClassType classType = new ClassType();

    /**
     * 
     */
    @Test
    void testToObject() {
        assertSame(Aaa.class, classType.toObject(Aaa.class.getName()));
    }

    /**
     * 
     */
    @Test
    void testToText() {
        assertEquals("org.seasar.extension.jdbc.gen.argtype.ClassTypeTest$Aaa", classType.toText(Aaa.class));
    }

    /**
     * 
     */
    public static class Aaa {
    }
}
