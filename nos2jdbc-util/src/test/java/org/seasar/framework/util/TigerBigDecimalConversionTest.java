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

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.jupiter.api.*;
import org.seasar.framework.conversion.BigDecimalConversionUtil;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author koichik
 * 
 */
class TigerBigDecimalConversionTest {

    /**
     * @throws Exception
     */
    @Test
    void testBigDouble() throws Exception {
        assertEquals("12500000", BigDecimalConversionUtil.toBigDecimal(
                12500000D).toString());
    }

    /**
     * @throws Exception
     */
    @Test
    void testToString() throws Exception {
        BigDecimal d = new BigDecimal(new BigInteger("125"), -1);
        assertEquals("1250", BigDecimalConversionUtil.toString(d));
    }
}
