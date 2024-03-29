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

import org.seasar.framework.conversion.BooleanConversionUtil;

/**
 * 論理値用の型です。
 * 
 * @author manhole
 */
public class BooleanType extends ObjectType {

    BooleanType() {
    }

    @Override
    public Object convert(Object value, String formatPattern) {
        return BooleanConversionUtil.toBoolean(value);
    }

    @Override
    public Object convert(Class<?> clazz, Object value) {
        if (clazz == Boolean.class) {
            return BooleanConversionUtil.toBoolean(value);
        } else if (clazz == boolean.class) {
            return BooleanConversionUtil.toPrimitiveBoolean(value);
        }
        return value;
    }
    
    @Override
    public Class<Boolean> getType() {
        return Boolean.class;
    }

}
