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
package org.seasar.framework.util;

import java.util.Map;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.seasar.framework.util.Maps.*;

/**
 * @author koichik
 */
class MapsTest {

    /**
     * @throws Exception
     */
    @Test
    void test() throws Exception {
        Map<String, Integer> map = map("a", 1).$("b", 2).$("c", 3).$();
        assertEquals(3, map.size());
        assertEquals(Integer.valueOf(1), map.get("a"));
        assertEquals(Integer.valueOf(2), map.get("b"));
        assertEquals(Integer.valueOf(3), map.get("c"));
    }

}
