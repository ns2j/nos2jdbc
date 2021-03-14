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
package org.seasar.extension.jdbc.dialect;

import javax.persistence.GenerationType;
import javax.persistence.TemporalType;

import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.SelectForUpdateType;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.exception.OrderByNotFoundRuntimeException;
import org.seasar.extension.jdbc.types.LocalDateTimeType;
import org.seasar.extension.jdbc.types.LocalDateType;
import org.seasar.extension.jdbc.types.OffsetDateTimeType;
import org.seasar.extension.jdbc.types.ValueTypes;
import org.seasar.framework.util.tiger.Pair;

/**
 * MS SQLServer用の方言をあつかうクラスです。
 * 
 * @author higa
 * 
 */
public class MssqlDialect extends StandardDialect {

    /**
     * 一意制約違反を表す例外コード
     */
    protected static final int uniqueConstraintViolationCode = 2627;

    @Override
    public String getName() {
        return "mssql";
    }

    @Override
    public boolean supportsLimit() {
        return true;
    }

    @Override
    public boolean supportsOffset() {
        return true;
    }

    @Override
    public String convertLimitSql(String sql, int offset, int limit) {
        if (sql.toLowerCase().lastIndexOf("order by") < 0)
            throw new OrderByNotFoundRuntimeException(sql);
        StringBuilder buf = new StringBuilder(sql.length() + 20);
        buf.append(sql);
        if (offset > 0) {
            buf.append(" offset ");
            buf.append(offset);
            buf.append(" rows");
        }
        if (limit > 0) {
            if (offset == 0)
                buf.append(" offset 0 rows ");
            buf.append(" fetch next ");
            buf.append(limit);
            buf.append(" rows only");
        }
        return buf.toString();
    }

    @Override
    public GenerationType getDefaultGenerationType() {
        return GenerationType.IDENTITY;
    }

    @Override
    public boolean supportsIdentity() {
        return true;
    }

    @Override
    public boolean supportsGetGeneratedKeys() {
        return true;
    }

    @Override
    public boolean supportsForUpdate(final SelectForUpdateType type,
            boolean withTarget) {
        return type != SelectForUpdateType.WAIT;
    }

    @Override
    public String getForUpdateString(final SelectForUpdateType type,
            final int waitSeconds, final Pair<String, String>... aliases) {
        return "";
    }

    @Override
    public boolean supportsLockHint() {
        return true;
    }

    @Override
    public String getLockHintString(final SelectForUpdateType type,
            final int waitSeconds) {
        final StringBuilder buf = new StringBuilder(100)
                .append(" with (updlock, rowlock");
        if (type == SelectForUpdateType.NOWAIT) {
            buf.append(", nowait");
        }
        buf.append(")");
        return new String(buf);
    }

    @Override
    public boolean isUniqueConstraintViolation(Throwable t) {
        final Integer code = getErrorCode(t);
        if (code != null) {
            return uniqueConstraintViolationCode == code.intValue();
        }
        return false;
    }
    
    @Override
    public ValueType getValueType(PropertyMeta propertyMeta) {
        ValueType vt = super.getValueType(propertyMeta);
        if (vt.getClass() == LocalDateType.class)
            return ValueTypes.JDBC42LOCALDATE;
        if (vt.getClass() == LocalDateTimeType.class)
            return ValueTypes.JDBC42LOCALDATETIME;
        if (vt.getClass() == OffsetDateTimeType.class)
            return ValueTypes.JDBC42OFFSETDATETIME;
        return vt;
    }

    @Override
    public ValueType getValueType(Class<?> clazz, boolean lob,
            TemporalType temporalType) {
        ValueType vt = super.getValueType(clazz, lob, temporalType);
        if (vt.getClass() == LocalDateType.class)
            return ValueTypes.JDBC42LOCALDATE;
        if (vt.getClass() == LocalDateTimeType.class)
            return ValueTypes.JDBC42LOCALDATETIME;
        if (vt.getClass() == OffsetDateTimeType.class)
            return ValueTypes.JDBC42OFFSETDATETIME;
        return vt;
    }
}
