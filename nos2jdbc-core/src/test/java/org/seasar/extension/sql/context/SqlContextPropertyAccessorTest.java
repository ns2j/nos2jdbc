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
package org.seasar.extension.sql.context;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.seasar.extension.sql.SqlContext;
import org.seasar.framework.util.OgnlUtil;

/**
 * @author higa
 * 
 */
class SqlContextPropertyAccessorTest {

    /**
     * @throws Exception
     */
    @Test
    void testGetProperty() throws Exception {
        SqlContext ctx = new SqlContextImpl();
        ctx.addArg("aaa", "111", String.class);
        assertEquals("111", OgnlUtil.getValue("aaa", ctx));
        assertEquals(Boolean.TRUE, OgnlUtil.getValue("has_aaa", ctx));
        assertEquals(Boolean.TRUE, OgnlUtil.getValue("has_aaa == true", ctx));
        assertEquals(Boolean.FALSE, OgnlUtil.getValue("has_bbb", ctx));
    }
}