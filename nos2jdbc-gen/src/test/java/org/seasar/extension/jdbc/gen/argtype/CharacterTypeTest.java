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

import org.junit.jupiter.api.Test;
import org.seasar.extension.jdbc.gen.argtype.CharacterType;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author taedium
 * 
 */
class CharacterTypeTest {

    private CharacterType characterType = new CharacterType();

    /**
     * 
     */
    @Test
    void testToObject() {
        assertEquals(Character.valueOf(' '), characterType.toObject("\\u0000"));
        assertEquals(Character.valueOf('a'), characterType.toObject("\\u0097"));
    }

    /**
     * 
     */
    @Test
    void testToText() {
        assertEquals("\\u0000", characterType.toText(Character.valueOf(' ')));
        assertEquals("\\u0097", characterType.toText(Character.valueOf('a')));
    }
}
