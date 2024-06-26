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

import java.io.Serializable;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.seasar.extension.jdbc.DbmsDialect;
import org.seasar.extension.jdbc.FromClause;
import org.seasar.extension.jdbc.JoinColumnMeta;
import org.seasar.extension.jdbc.JoinType;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.SelectForUpdateType;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.WhereClause;
import org.seasar.extension.jdbc.exception.OrderByNotFoundRuntimeException;
import org.seasar.extension.jdbc.types.ValueTypes;
import org.seasar.extension.jdbc.util.QueryTokenizer;
import org.seasar.framework.util.CollectionsUtil;
import org.seasar.framework.util.Pair;
import org.seasar.framework.util.StringUtil;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.GenerationType;
import jakarta.persistence.TemporalType;

/**
 * 標準的な方言をあつかうクラスです
 * 
 * @author higa
 * 
 */
public class StandardDialect implements DbmsDialect {

    /**
     * {@link EntityExistsException}に該当するSQLステートです。
     */
    protected static final Set<String> entityExistsExceptionStateCode = CollectionsUtil
            .newHashSet(Arrays.asList("23", "27", "44"));

    protected Map<Boolean, Map<Class<?>, ValueType>> jsr310Map = Map.of(
            false, Map.of(LocalDate.class, ValueTypes.LOCALDATE,
                    LocalTime.class, ValueTypes.LOCALTIME,
                    LocalDateTime.class, ValueTypes.LOCALDATETIME,
                    OffsetDateTime.class, ValueTypes.TIMESTAMP_WITH_TIMEZONE),
            true, Map.of(LocalDate.class, ValueTypes.JDBC42LOCALDATE,
                    LocalTime.class, ValueTypes.JDBC42LOCALTIME,
                    LocalDateTime.class, ValueTypes.JDBC42LOCALDATETIME,
                    OffsetDateTime.class, ValueTypes.JDBC42OFFSETDATETIME));
    
    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean supportsLimit() {
        return false;
    }

    @Override
    public boolean supportsOffset() {
        return supportsLimit();
    }

    @Override
    public boolean supportsOffsetWithoutLimit() {
        return supportsOffset();
    }

    @Override
    public boolean supportsCursor() {
        return true;
    }

    @Override
    public boolean needsParameterForResultSet() {
        return false;
    }

    @Override
    public String convertLimitSql(String sql, int offset, int limit) {
        return sql;
    }

    @Override
    public String convertGetCountSql(String sql) {
        return "select count(*) from ( " + sql + " ) COUNT_";
    }

    @Override
    public String getCountSqlSelectList(List<PropertyMeta> idPropertyMeta) {
        return "count(*)";
    }

    @Override
    public boolean supportsJdbc42AtJsr310() {
        return false;
    }
    
    @Override
    public ValueType getValueType(PropertyMeta propertyMeta) {
        ValueType vt = jsr310Map.get(supportsJdbc42AtJsr310()).get(propertyMeta.getPropertyClass());
        if (vt != null)
            return vt;
        return propertyMeta.getValueType();
    }

    @Override
    public ValueType getValueType(Class<?> clazz, boolean lob,
            TemporalType temporalType) {
        if (lob) {
            if (clazz == String.class) {
                return ValueTypes.CLOB;
            } else if (clazz == byte[].class) {
                return ValueTypes.BLOB;
            } else if (Serializable.class.isAssignableFrom(clazz)) {
                return ValueTypes.SERIALIZABLE_BLOB;
            }
        } else if (clazz == byte[].class) {
            return ValueTypes.BYTE_ARRAY;
        }

        if (temporalType != null) {
            if (Date.class == clazz) {
                switch (temporalType) {
                case DATE:
                    return ValueTypes.DATE_SQLDATE;
                case TIME:
                    return ValueTypes.DATE_TIME;
                case TIMESTAMP:
                    return ValueTypes.DATE_TIMESTAMP;
                }
            }
            if (Calendar.class == clazz) {
                switch (temporalType) {
                case DATE:
                    return ValueTypes.CALENDAR_SQLDATE;
                case TIME:
                    return ValueTypes.CALENDAR_TIME;
                case TIMESTAMP:
                    return ValueTypes.CALENDAR_TIMESTAMP;
                }
            }
        }

        ValueType vt = jsr310Map.get(supportsJdbc42AtJsr310()).get(clazz);
        if (vt != null)
            return vt;
        
        ValueType valueType = getValueTypeInternal(clazz);
        if (valueType == null) {
            valueType = ValueTypes.getValueType(clazz);
        }
        if (valueType == ValueTypes.OBJECT
                && Serializable.class.isAssignableFrom(clazz)) {
            return ValueTypes.SERIALIZABLE_BYTE_ARRAY;
        }
        return valueType;
    }

    /**
     * 値タイプを返します。
     * 
     * @param clazz
     *            クラス
     * @return 値タイプ
     */
    protected ValueType getValueTypeInternal(Class<?> clazz) {
        return null;
    }

    @Override
    public void setupJoin(FromClause fromClause, WhereClause whereClause,
            JoinType joinType, String tableName, String tableAlias,
            String fkTableAlias, String pkTableAlias,
            List<JoinColumnMeta> joinColumnMetaList, String lockHint,
            String condition) {
        fromClause.addSql(joinType, tableName, tableAlias, fkTableAlias,
                pkTableAlias, joinColumnMetaList, lockHint, condition);

    }

    @Override
    public GenerationType getDefaultGenerationType() {
        return GenerationType.TABLE;
    }

    @Override
    public boolean supportsIdentity() {
        return false;
    }

    @Override
    public boolean isInsertIdentityColumn() {
        return false;
    }

    @Override
    public boolean supportsGetGeneratedKeys() {
        return false;
    }

    @Override
    public String getIdentitySelectString(final String tableName,
            final String columnName) {
        return null;
    }

    @Override
    public boolean supportsSequence() {
        return false;
    }

    @Override
    public String getSequenceNextValString(final String sequenceName,
            final int allocationSize) {
        return null;
    }

    @Override
    public int getDefaultBatchSize() {
        return 0;
    }

    @Override
    public boolean supportsBatchUpdateResults() {
        return true;
    }

    /**
     * 行番号ファンクション名を返します。
     * 
     * @return 行番号ファンクション名
     */
    protected String getRowNumberFunctionName() {
        return "row_number()";
    }

    /**
     * 行番号を使ったSQLに変換します。
     * 
     * @param sql
     *            SQL
     * @param offset
     *            オフセット
     * @param limit
     *            リミット
     * @return offset、limitつきのSQL
     * @throws OrderByNotFoundRuntimeException
     *             <code>order by</code>が見つからない場合。
     */
    protected String convertLimitSqlByRowNumber(String sql, int offset,
            int limit) throws OrderByNotFoundRuntimeException {
        StringBuilder buf = new StringBuilder(sql.length() + 150);
        String lowerSql = sql.toLowerCase();
        int startOfSelect = lowerSql.indexOf("select");
        buf.append(sql.substring(0, startOfSelect));
        buf.append("select * from ( select temp_.*, ");
        buf.append(getRowNumberFunctionName()).append(" over(");
        int orderByIndex = lowerSql.lastIndexOf("order by");
        if (orderByIndex > 0) {
            String orderBy = sql.substring(orderByIndex);
            buf.append(convertOrderBy(orderBy));
            sql = StringUtil.rtrim(sql.substring(0, orderByIndex));
        } else {
            throw new OrderByNotFoundRuntimeException(sql);
        }
        buf.append(") as rownumber_ from ( ");
        buf.append(sql.substring(startOfSelect));
        buf.append(" ) as temp_");
        buf.append(" ) as temp2_ where rownumber_ >= ");
        buf.append(offset + 1);
        if (limit > 0) {
            buf.append(" and rownumber_ <= ");
            buf.append(offset + limit);
        }
        return buf.toString();
    }

    /**
     * order by句のテーブルのエイリアスを一時的なテーブル名に変換します。
     * 
     * @param orderBy
     *            order by句
     * @return 変換後のorder by句
     */
    protected String convertOrderBy(String orderBy) {
        StringBuilder sb = new StringBuilder(10 + orderBy.length());
        QueryTokenizer tokenizer = new QueryTokenizer(orderBy);
        for (int type = tokenizer.nextToken(); type != QueryTokenizer.TT_EOF; type = tokenizer
                .nextToken()) {
            String token = tokenizer.getToken();
            if (type == QueryTokenizer.TT_WORD) {
                String[] names = StringUtil.split(token, ".");
                if (names.length == 2) {
                    sb.append("temp_.").append(names[1]);
                } else {
                    sb.append(token);
                }
            } else {
                sb.append(token);
            }
        }
        return sb.toString();
    }

    @Override
    public boolean isUniqueConstraintViolation(Throwable t) {
        final String state = getSQLState(t);
        if (state != null && state.length() >= 2) {
            return entityExistsExceptionStateCode.contains(state
                    .substring(0, 2));
        }
        return false;
    }

    /**
     * 例外チェーンをたどって原因となった{@link SQLException#getSQLState() SQLステート}を返します。
     * <p>
     * 例外チェーンに{@link SQLException SQL例外}が存在しない場合や、SQLステートが設定されていない場合は<code>null</code>を返します。
     * </p>
     * 
     * @param t
     *            例外
     * @return 原因となった{@link SQLException#getSQLState() SQLステート}
     */
    protected String getSQLState(Throwable t) {
        SQLException cause = getCauseSQLException(t);
        if (cause != null && !StringUtil.isEmpty(cause.getSQLState())) {
            return cause.getSQLState();
        }
        return null;
    }

    /**
     * 例外チェーンをたどって原因となった{@link SQLException#getErrorCode() ベンダー固有の例外コード}を返します。
     * <p>
     * 例外チェーンに{@link SQLException SQL例外}が存在しない場合や、例外コードが設定されていない場合は<code>null</code>を返します。
     * </p>
     * 
     * @param t
     *            例外
     * @return 原因となった{@link SQLException#getErrorCode() ベンダー固有の例外コード}
     */
    protected Integer getErrorCode(Throwable t) {
        SQLException cause = getCauseSQLException(t);
        if (cause != null) {
            return cause.getErrorCode();
        }
        return null;
    }

    /**
     * 例外チェーンをたどって原因となった{@link SQLException SQL例外}を返します。
     * <p>
     * 例外チェーンにSQL例外が存在しない場合は<code>null</code>を返します。
     * </p>
     * 
     * @param t
     *            例外
     * @return 原因となった{@link SQLException SQL例外}
     */
    protected SQLException getCauseSQLException(Throwable t) {
        SQLException cause = null;
        while (t != null) {
            if (t instanceof SQLException) {
                cause = SQLException.class.cast(t);
                if (cause.getNextException() != null) {
                    cause = cause.getNextException();
                    t = cause;
                    continue;
                }
            }
            t = t.getCause();
        }
        return cause;
    }

    @Override
    public boolean supportsForUpdate(final SelectForUpdateType type,
            boolean withTarget) {
        return type == SelectForUpdateType.NORMAL && !withTarget;
    }

    @Override
    public String getForUpdateString(final SelectForUpdateType type,
            final int waitSeconds, @SuppressWarnings("unchecked") final Pair<String, String>... aliases) {
        return " for update";
    }

    @Override
    public boolean supportsLockHint() {
        return false;
    }

    @Override
    public String getLockHintString(final SelectForUpdateType type,
            final int waitSeconds) {
        return "";
    }

    @Override
    public boolean supportsInnerJoinForUpdate() {
        return true;
    }

    @Override
    public boolean supportsOuterJoinForUpdate() {
        return true;
    }

    @Override
    public String getHintComment(String hint) {
        return "";
    }

}
