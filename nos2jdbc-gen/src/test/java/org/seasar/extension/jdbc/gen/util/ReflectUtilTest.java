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
import org.seasar.extension.jdbc.gen.exception.ClassUnmatchRuntimeException;
import org.seasar.extension.jdbc.gen.util.ReflectUtil;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author taedium
 * 
 */
class ReflectUtilTest {

    /**
     * 
     * @throws Exception
     */
    @Test
    void testNewInstance() throws Exception {
        String baseName = getClass().getName();
        Aaa aaa = ReflectUtil.newInstance(Aaa.class, baseName + "$Bbb");
        assertNotNull(aaa);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    void testNewInstance_classUnmatch() throws Exception {
        String baseName = getClass().getName();
        try {
            ReflectUtil.newInstance(Aaa.class, baseName + "$Ccc");
            fail();
        } catch (ClassUnmatchRuntimeException expected) {
        }
    }

    /** */
    public static class Aaa {
    }

    /** */
    public static class Bbb extends Aaa {
    }

    /** */
    public static class Ccc {
    }
}
