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

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Types;

import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.util.BindVariableUtil;
import org.seasar.framework.conversion.TimeConversionUtil;

/**
 * Time用の {@link ValueType}です。
 * 
 * @author higa
 * 
 */
public class TimeType extends AbstractValueType {

    /**
     * インスタンスを構築します。
     */
    public TimeType() {
        super(Types.TIME);
    }

    public Object getValue(ResultSet resultSet, int index) throws SQLException {
        return resultSet.getTime(index);
    }

    public Object getValue(ResultSet resultSet, String columnName)
            throws SQLException {
        return resultSet.getTime(columnName);
    }

    public Object getValue(CallableStatement cs, int index) throws SQLException {
        return cs.getTime(index);
    }

    public Object getValue(CallableStatement cs, String parameterName)
            throws SQLException {
        return cs.getTime(parameterName);
    }

    public void bindValue(PreparedStatement ps, int index, Object value)
            throws SQLException {
        if (value == null) {
            setNull(ps, index);
        } else {
            ps.setTime(index, toTime(value));
        }
    }

    public void bindValue(CallableStatement cs, String parameterName,
            Object value) throws SQLException {
        if (value == null) {
            setNull(cs, parameterName);
        } else {
            cs.setTime(parameterName, toTime(value));
        }
    }

    /**
     * {@link Time}に変換します
     * 
     * @param value
     *            値
     * @return {@link Time}
     */
    protected Time toTime(Object value) {
        return TimeConversionUtil.toTime(value);
    }

    public String toText(Object value) {
        if (value == null) {
            return BindVariableUtil.nullText();
        }
        return BindVariableUtil.toText(toTime(value));
    }

}