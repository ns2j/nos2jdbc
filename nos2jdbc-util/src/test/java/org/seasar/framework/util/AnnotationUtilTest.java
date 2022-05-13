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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.seasar.framework.util.AnnotationUtil;
import org.seasar.framework.util.ClassUtil;

/**
 * @author higa
 */
class AnnotationUtilTest {

    /**
     * @throws Exception
     */
    @Hoge(bbb = "3")
    @Test
    public void testGetProperties() throws Exception {
        Method m = ClassUtil.getMethod(getClass(), "testGetProperties", null);
        Annotation anno = m.getAnnotation(Hoge.class);
        Map<String, Object> props = AnnotationUtil.getProperties(anno);
        assertEquals("123", props.get("aaa"));
        assertEquals("3", props.get("bbb"));
        assertNull(props.get("ccc"));
    }
}