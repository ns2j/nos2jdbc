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
package org.seasar.extension.jdbc.gen.sqltype;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.seasar.framework.conversion.BigDecimalConversionUtil;
import org.seasar.framework.conversion.BigIntegerConversionUtil;
import org.seasar.framework.conversion.StringConversionUtil;

/**
 * {@link Types#BIGINT}に対応する{@link SqlType}です。
 * 
 * @author taedium
 */
public class BigIntType extends AbstractSqlType {

    /**
     * インスタンスを構築します。
     */
    public BigIntType() {
        this("bigint");
    }

    /**
     * インスタンスを構築します。
     * 
     * @param dataType
     *            データ型
     */
    public BigIntType(String dataType) {
        super(dataType);
    }

    public void bindValue(PreparedStatement ps, int index, String value)
            throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.BIGINT);
        } else {
            ps.setBigDecimal(index, BigDecimalConversionUtil
                    .toBigDecimal(value));
        }
    }

    public String getValue(ResultSet resultSet, int index) throws SQLException {
        BigInteger value = BigIntegerConversionUtil.toBigInteger(resultSet
                .getBigDecimal(index));
        return StringConversionUtil.toString(value);
    }

}
