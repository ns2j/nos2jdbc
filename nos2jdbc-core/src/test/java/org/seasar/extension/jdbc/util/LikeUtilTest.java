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
package org.seasar.extension.jdbc.util;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author koichik
 */
class LikeUtilTest {

    
    @AfterEach
    void tearDown() throws Exception {
        LikeUtil.setWildcardPattern(null);
        LikeUtil.setWildcardReplacementPattern(null);
    }

    /**
     * 
     */
    @Test
    void testContainsWildcard() {
        assertFalse(LikeUtil.containsWildcard("aaa"));
        assertFalse(LikeUtil.containsWildcard("$"));
        assertTrue(LikeUtil.containsWildcard("%"));
        assertTrue(LikeUtil.containsWildcard("_"));
        assertTrue(LikeUtil.containsWildcard("％"));
        assertTrue(LikeUtil.containsWildcard("＿"));
    }

    /**
     * 
     */
    @Test
    void testContainsWildcard_modifiedPattern() {
        LikeUtil.setWildcardPatternAsString("[%_]");

        assertFalse(LikeUtil.containsWildcard("aaa"));
        assertFalse(LikeUtil.containsWildcard("$"));
        assertTrue(LikeUtil.containsWildcard("%"));
        assertTrue(LikeUtil.containsWildcard("_"));
        assertFalse(LikeUtil.containsWildcard("％"));
        assertFalse(LikeUtil.containsWildcard("＿"));
    }

    /**
     * 
     */
    @Test
    void testEscapeWildcard() {
        assertEquals("aaa", LikeUtil.escapeWildcard("aaa"));
        assertEquals("a$$a$%a$_a$％a$＿a", LikeUtil.escapeWildcard("a$a%a_a％a＿a"));
    }

    /**
     * 
     */
    @Test
    void testEscapeWildcard_modifiedPaattern() {
        LikeUtil.setWildcardReplacementPatternAsString("[$%_]");

        assertEquals("aaa", LikeUtil.escapeWildcard("aaa"));
        assertEquals("a$$a$%a$_a％a＿a", LikeUtil.escapeWildcard("a$a%a_a％a＿a"));
    }

}
