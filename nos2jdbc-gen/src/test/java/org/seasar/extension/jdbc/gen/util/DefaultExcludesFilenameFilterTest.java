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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.seasar.extension.jdbc.gen.util.DefaultExcludesFilenameFilter;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author taedium
 * 
 */
class DefaultExcludesFilenameFilterTest {

    /**
     * 
     */
    @AfterEach
    public void tearDown() {
        DefaultExcludesFilenameFilter.resetFilterPattern();
    }

    /**
     * 
     */
    @Test
    void testAccept() {
        DefaultExcludesFilenameFilter filter = new DefaultExcludesFilenameFilter();
        assertTrue(filter.accept(null, "aaa"));
        assertFalse(filter.accept(null, "aaa~"));
        assertFalse(filter.accept(null, "#aaa#"));
        assertFalse(filter.accept(null, ".#aaa"));
        assertFalse(filter.accept(null, "%aaa%"));
        assertFalse(filter.accept(null, "._aaa"));
        assertFalse(filter.accept(null, "CVS"));
        assertFalse(filter.accept(null, ".cvsignore"));
        assertFalse(filter.accept(null, "SCCS"));
        assertFalse(filter.accept(null, "vssver.scc"));
        assertFalse(filter.accept(null, ".svn"));
        assertFalse(filter.accept(null, ".DS_Store"));
    }

    /**
     * 
     */
    @Test
    void testSetFilterRegex() {
        DefaultExcludesFilenameFilter.setFilterRegex("test");
        DefaultExcludesFilenameFilter filter = new DefaultExcludesFilenameFilter();
        assertTrue(filter.accept(null, "aaa"));
        assertFalse(filter.accept(null, "test"));
    }
}
