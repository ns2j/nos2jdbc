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
package org.seasar.extension.jdbc.gen.dialect;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.seasar.extension.jdbc.gen.sqltype.BinaryType;
import org.seasar.extension.jdbc.gen.sqltype.BlobType;
import org.seasar.extension.jdbc.gen.sqltype.BooleanType;
import org.seasar.extension.jdbc.gen.sqltype.ClobType;
import org.seasar.extension.jdbc.gen.sqltype.DecimalType;
import org.seasar.extension.jdbc.gen.sqltype.FloatType;
import org.seasar.extension.jdbc.util.ConnectionUtil;
import org.seasar.framework.util.ResultSetUtil;
import org.seasar.framework.util.StatementUtil;

import jakarta.persistence.GenerationType;

/**
 * DB2の方言を扱うクラスです。
 * 
 * @author taedium
 */
public class Db2GenDialect extends StandardGenDialect {

    /** テーブルが見つからないことを示すSQLステート */
    protected static String TABLE_NOT_FOUND_SQL_STATE = "42704";

    /** カラムが見つからないことを示すSQLステート */
    protected static String COLUMN_NOT_FOUND_SQL_STATE = "42703";

    /** シーケンスが見つからないことを示すSQLステート */
    protected static String SEQUENCE_NOT_FOUND_SQL_STATE = "42704";

    /**
     * インスタンスを構築します。
     */
    public Db2GenDialect() {
        sqlTypeMap
                .put(Types.BINARY, new BinaryType("varchar($l) for bit data"));
        sqlTypeMap.put(Types.BLOB, new BlobType("blob($l)"));
        sqlTypeMap.put(Types.BOOLEAN, new BooleanType("smallint"));
        sqlTypeMap.put(Types.CLOB, new ClobType("clob($l)"));
        sqlTypeMap.put(Types.DECIMAL, new DecimalType("decimal($p,$s)"));
        sqlTypeMap.put(Types.FLOAT, new FloatType("real"));

        columnTypeMap.put("blob", Db2ColumnType.BLOB);
        columnTypeMap.put("char () for bit data", Db2ColumnType.CHAR_BIT);
        columnTypeMap.put("clob", Db2ColumnType.CLOB);
        columnTypeMap.put("decimal", Db2ColumnType.DECIMAL);
        columnTypeMap.put("long varchar for bit data",
                Db2ColumnType.LONGVARCHAR_BIT);
        columnTypeMap.put("long varchar", Db2ColumnType.LONGVARCHAR);
        columnTypeMap.put("varchar () for bit data", Db2ColumnType.VARCHAR_BIT);
    }

    @Override
    public String getName() {
        return "db2";
    }

    @Override
    public String getDefaultSchemaName(String userName) {
        return userName != null ? userName.toUpperCase() : null;
    }

    @Override
    public GenerationType getDefaultGenerationType() {
        return GenerationType.IDENTITY;
    }

    @Override
    public boolean supportsSequence() {
        return true;
    }

    @Override
    public String getSequenceDefinitionFragment(String dataType,
            long initialValue, int allocationSize) {
        return "as " + dataType + " start with " + initialValue
                + " increment by " + allocationSize;
    }

    @Override
    public String getIdentityColumnDefinition() {
        return "generated by default as identity";
    }

    @Override
    public String getSqlBlockDelimiter() {
        return "@";
    }

    @Override
    public boolean isTableNotFound(Throwable t) {
        for (SQLException e : getAllSQLExceptions(t)) {
            if (TABLE_NOT_FOUND_SQL_STATE.equals(e.getSQLState())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isColumnNotFound(Throwable t) {
        for (SQLException e : getAllSQLExceptions(t)) {
            if (COLUMN_NOT_FOUND_SQL_STATE.equals(e.getSQLState())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isSequenceNotFound(Throwable t) {
        for (SQLException e : getAllSQLExceptions(t)) {
            if (SEQUENCE_NOT_FOUND_SQL_STATE.equals(e.getSQLState())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean supportsIdentityInsert() {
        return true;
    }

    @Override
    public boolean supportsIdentity() {
        return true;
    }

    /**
     * 原因となった、もしくは関連付けられたすべての{@link SQLException}を取得します。
     * 
     * @param t
     *            例外
     * @return 原因となった、もしくは関連付けられた{@link SQLException}のリスト
     */
    protected List<SQLException> getAllSQLExceptions(Throwable t) {
        List<SQLException> sqlExceptionList = new ArrayList<SQLException>();
        while (t != null) {
            if (t instanceof SQLException) {
                SQLException cause = SQLException.class.cast(t);
                sqlExceptionList.add(cause);
                if (cause.getNextException() != null) {
                    cause = cause.getNextException();
                    sqlExceptionList.add(cause);
                    t = cause;
                    continue;
                }
            }
            t = t.getCause();
        }
        return sqlExceptionList;
    }

    @Override
    public SqlBlockContext createSqlBlockContext() {
        return new Db2SqlBlockContext();
    }

    @Override
    public boolean supportsNullableUnique() {
        return false;
    }

    @Override
    public String getSequenceNextValString(String sequenceName,
            int allocationSize) {
        return "values nextval for " + sequenceName;
    }

    @Override
    public boolean supportsCommentInCreateTable() {
        return false;
    }

    @Override
    public boolean supportsCommentOn() {
        return true;
    }

    @Override
    public boolean isAutoIncrement(Connection connection, String catalogName,
            String schemaName, String tableName, String columnName)
            throws SQLException {
        String sql = "select generated from syscat.columns where tabschema = ? and tabname = ? and colname = ?";
        logger.debug(String.format(sql.replace("?", "'%s'"), schemaName,
                tableName, columnName));

        PreparedStatement ps = ConnectionUtil.prepareStatement(connection, sql);
        ps.setString(1, schemaName);
        ps.setString(2, tableName);
        ps.setString(3, columnName);
        try {
            ResultSet rs = ps.executeQuery();
            try {
                if (rs.next()) {
                    String generated = rs.getString(1);
                    return "A".equals(generated) || "D".equals(generated);
                }
                return false;
            } finally {
                ResultSetUtil.close(rs);
            }
        } finally {
            StatementUtil.close(ps);
        }
    }

    /**
     * DB2用の{@link StandardColumnType}の実装です。
     * 
     * @author taedium
     */
    public static class Db2ColumnType extends StandardColumnType {

        private static Db2ColumnType BLOB = new Db2ColumnType("blob($l)",
                byte[].class);

        private static Db2ColumnType CHAR_BIT = new Db2ColumnType(
                "char($l) for bit data", byte[].class);

        private static Db2ColumnType CLOB = new Db2ColumnType("clob($l)",
                String.class);

        private static Db2ColumnType DECIMAL = new Db2ColumnType(
                "decimal($p,$s)", BigDecimal.class);

        private static Db2ColumnType LONGVARCHAR_BIT = new Db2ColumnType(
                "long varchar for bit data", byte[].class);

        private static Db2ColumnType LONGVARCHAR = new Db2ColumnType(
                "long varchar", String.class);

        private static Db2ColumnType VARCHAR_BIT = new Db2ColumnType(
                "varchar($l) for bit data", byte[].class);

        /**
         * インスタンスを構築します。
         * 
         * @param dataType
         *            データ型
         * @param attributeClass
         *            属性のクラス
         */
        public Db2ColumnType(String dataType, Class<?> attributeClass) {
            super(dataType, attributeClass);
        }

        /**
         * インスタンスを構築します。
         * 
         * @param dataType
         *            データ型
         * @param attributeClass
         *            属性のクラス
         * @param lob
         *            LOBの場合{@code true}
         */
        public Db2ColumnType(String dataType, Class<?> attributeClass,
                boolean lob) {
            super(dataType, attributeClass, lob);
        }

    }

    /**
     * DB2用の{@link SqlBlockContext}の実装クラスです。
     * 
     * @author taedium
     */
    public static class Db2SqlBlockContext extends StandardSqlBlockContext {

        /**
         * インスタンスを構築します。
         */
        protected Db2SqlBlockContext() {
            sqlBlockStartKeywordsList.add(Arrays.asList("create", "procedure"));
            sqlBlockStartKeywordsList.add(Arrays.asList("create", "function"));
            sqlBlockStartKeywordsList.add(Arrays.asList("create", "trigger"));
            sqlBlockStartKeywordsList.add(Arrays.asList("alter", "procedure"));
            sqlBlockStartKeywordsList.add(Arrays.asList("alter", "function"));
            sqlBlockStartKeywordsList.add(Arrays.asList("alter", "trigger"));
        }
    }
}
