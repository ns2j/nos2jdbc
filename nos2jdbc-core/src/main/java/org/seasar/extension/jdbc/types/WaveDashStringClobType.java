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
package org.seasar.extension.jdbc.types;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.seasar.framework.conversion.StringConversionUtil;

/**
 * <p>
 * WAVE DASH(U+301C)をFULLWIDTH TILDE(U+FF5E)に変換する値タイプです。
 * </p>
 * <p>
 * オラクルのバグ対策用です。
 * </p>
 * 
 * @author taedium
 * 
 */
public class WaveDashStringClobType extends StringClobType {

    public Object getValue(ResultSet resultSet, int index) throws SQLException {
        return StringConversionUtil.fromWaveDashToFullwidthTilde((String) super
                .getValue(resultSet, index));
    }

    public Object getValue(ResultSet resultSet, String columnName)
            throws SQLException {
        return StringConversionUtil.fromWaveDashToFullwidthTilde((String) super
                .getValue(resultSet, columnName));
    }

}
