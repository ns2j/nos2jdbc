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

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Set;

import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.types.ValueTypes;
import org.seasar.framework.util.CollectionsUtil;

import jakarta.persistence.GenerationType;
import jakarta.persistence.TemporalType;

/**
 * MySQL用の方言をあつかうクラスです。
 * 
 * @author higa
 * 
 */
public class MysqlDialect extends StandardDialect {

    /**
     * 一意制約違反を表す例外コード
     */
    protected static final Set<Integer> uniqueConstraintViolationCode = CollectionsUtil
            .newHashSet(Arrays.asList(1022, 1062));

    @Override
    public String getName() {
        return "mysql";
    }

    @Override
    public boolean supportsLimit() {
        return true;
    }

    @Override
    public boolean supportsOffsetWithoutLimit() {
        return false;
    }

    @Override
    public String convertLimitSql(String sql, int offset, int limit) {
        StringBuilder buf = new StringBuilder(sql.length() + 20);
        buf.append(sql);
        if (offset > 0 && limit > 0) {
            buf.append(" limit ");
            buf.append(offset);
            buf.append(", ");
            buf.append(limit);
        } else if (offset == 0 && limit > 0) {
            buf.append(" limit ");
            buf.append(limit);
        } else if (offset > 0 && limit == 0) {
            throw new IllegalArgumentException("limit is zero");
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
    public boolean isUniqueConstraintViolation(Throwable t) {
        final Integer code = getErrorCode(t);
        if (code != null) {
            return uniqueConstraintViolationCode.contains(code);
        }
        return false;
    }
    
    @Override
    public boolean supportsJdbc42AtJsr310() {
        return true;
    }

    @Override
    public ValueType getValueType(PropertyMeta propertyMeta) {
        if (propertyMeta.getPropertyClass() == OffsetDateTime.class) {
            return ValueTypes.MYSQLOFFSETDATETIME;
        }
        return super.getValueType(propertyMeta);
    }
    @Override
    public ValueType getValueType(Class<?> clazz, boolean lob,
            TemporalType temporalType) {
        if (clazz == OffsetDateTime.class) {
            return ValueTypes.MYSQLOFFSETDATETIME;
        }
        return super.getValueType(clazz, lob, temporalType);
    }
}
