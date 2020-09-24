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
package org.seasar.extension.dataset.types;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.seasar.framework.util.BigDecimalConversionUtil;
import org.seasar.framework.util.NumberConversionUtil;

/**
 * 数値用の型です。
 * 
 * @author higa
 * 
 */
public class BigDecimalType extends ObjectType {

    BigDecimalType() {
    }

    @Override
    public Object convert(Object value, String formatPattern) {
        return BigDecimalConversionUtil.toBigDecimal(value, formatPattern);
    }
    
    @Override
    public Object convert(Class<?> clazz, Object value) {
        if (clazz == Integer.class || clazz == Long.class ||
                clazz == Float.class || clazz == Double.class||
                clazz == BigDecimal.class || clazz == BigInteger.class) {
            return NumberConversionUtil.convertNumber(clazz, value);
        } else if (clazz == int.class || clazz == long.class ||
                    clazz == float.class || clazz == double.class) {
            return NumberConversionUtil.convertPrimitiveWrapper(clazz, value);
        } else if (clazz == String.class) {
            return value.toString();
        }
        return value;
    }

    @Override
    public Class getType() {
        return BigDecimal.class;
    }
}
