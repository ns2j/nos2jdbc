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

import java.sql.Types;

import javax.persistence.GenerationType;
import javax.persistence.TemporalType;

import org.seasar.extension.jdbc.gen.sqltype.DoubleType;
import org.seasar.extension.jdbc.gen.sqltype.FloatType;
import org.seasar.extension.jdbc.gen.sqltype.IntegerType;
import org.seasar.extension.jdbc.gen.sqltype.VarcharType;


/**
 * SQLiteの方言を扱うクラスです。
 * 
 * @author koichik
 */
public class SqliteGenDialect extends StandardGenDialect {
    protected static int TABLE_NOT_FOUND_ERROR_CODE = 1;

    public SqliteGenDialect() {
        sqlTypeMap.put(Types.BIGINT, new IntegerType());
        sqlTypeMap.put(Types.VARCHAR, new VarcharType("text"));
        sqlTypeMap.put(Types.CLOB, new VarcharType("text"));
        sqlTypeMap.put(Types.DOUBLE, new DoubleType("real"));
        sqlTypeMap.put(Types.FLOAT, new FloatType("real"));
    }

    @Override
    public String getName() {
        return "sqlite";
    }

    @Override
    public String getDefaultSchemaName(String userName) {
        return null;
    }

    @Override
    public GenerationType getDefaultGenerationType() {
        return GenerationType.IDENTITY;
    }

    @Override
    public String getIdentityColumnDefinition() {
        return "primary key autoincrement";
    }

    @Override
    public boolean supportsIdentity() {
        return true;
    }

    @Override
    public boolean isTableNotFound(Throwable throwable) {
        Integer errorCode = getErrorCode(throwable);
        return TABLE_NOT_FOUND_ERROR_CODE == errorCode;
    }

    @Override
    public boolean supportsConstraintKey() {
        return false;
    }
    
    public static class SqliteColumnType implements ColumnType {

        private static SqliteColumnType LONG = new SqliteColumnType(
                "integer", Long.class);

        private static SqliteColumnType INTEGER = new SqliteColumnType(
                "integer", Integer.class);

        private static SqliteColumnType TEXT = new SqliteColumnType(
                "text", String.class);


        /** カラム定義 */
        protected String dataType;

        /** 属性のクラス */
        protected Class<?> attributeClass;

        /** LOBの場合{@code true} */
        protected boolean lob;

        /** 時制の種別 */
        protected TemporalType temporalType;


        /**
         * インスタンスを構築します。
         * 
         * @param dataType
         *            データ型
         * @param attributeClass
         *            属性のクラス
         */
        protected SqliteColumnType(String dataType, Class<?> attributeClass) {
            this(dataType, attributeClass, false);
        }

        /**
         * インスタンスを構築します。
         * 
         * @param dataType
         *            カラム定義
         * @param attributeClass
         *            属性のクラス
         * @param lob
         *            LOBの場合{@code true}
         */
        protected SqliteColumnType(String dataType, Class<?> attributeClass,
                boolean lob) {
            this(dataType, attributeClass, lob, null);
        }

        /**
         * インスタンスを構築します。
         * 
         * @param dataType
         *            カラム定義
         * @param attributeClass
         *            属性のクラス
         * @param lob
         *            LOBの場合{@code true}
         * @param temporalType
         *            時制の種別
         */
        protected SqliteColumnType(String dataType, Class<?> attributeClass,
                boolean lob, TemporalType temporalType) {
            this.dataType = dataType;
            this.attributeClass = attributeClass;
            this.lob = lob;
            this.temporalType = temporalType;
        }

        @Override
        public Class<?> getAttributeClass(int length, int precision, int scale) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getColumnDefinition(int length, int precision, int scale, String defaultValue) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public boolean isLob() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public TemporalType getTemporalType() {
            // TODO Auto-generated method stub
            return null;
        }

    }
}
