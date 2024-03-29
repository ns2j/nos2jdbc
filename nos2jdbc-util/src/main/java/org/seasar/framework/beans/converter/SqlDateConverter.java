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
package org.seasar.framework.beans.converter;

import java.util.Date;

import org.seasar.framework.beans.Converter;
import org.seasar.framework.conversion.SqlDateConversionUtil;
import org.seasar.framework.conversion.StringConversionUtil;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.StringUtil;

/**
 * SQLの日付用のコンバータです。
 * 
 * @author higa
 * 
 */
public class SqlDateConverter implements Converter {

    /**
     * 日付のパターンです。
     */
    protected String pattern;

    /**
     * インスタンスを構築します。
     * 
     * @param pattern
     *            日付のパターン
     */
    public SqlDateConverter(String pattern) {
        if (StringUtil.isEmpty(pattern)) {
            throw new EmptyRuntimeException("pattern");
        }
        this.pattern = pattern;
    }

    @Override
    public Object getAsObject(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        return SqlDateConversionUtil.toDate(value, pattern);
    }

    @Override
    public String getAsString(Object value) {
        return StringConversionUtil.toString((Date) value, pattern);
    }

    @Override
    public boolean isTarget(Class<?> clazz) {
        return clazz == java.sql.Date.class;
    }

}
