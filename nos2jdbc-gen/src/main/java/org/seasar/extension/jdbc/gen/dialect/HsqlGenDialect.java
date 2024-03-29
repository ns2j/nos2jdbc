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

import org.seasar.extension.jdbc.gen.sqltype.BinaryType;
import org.seasar.extension.jdbc.gen.sqltype.BlobType;
import org.seasar.extension.jdbc.gen.sqltype.ClobType;
import org.seasar.extension.jdbc.gen.sqltype.Jdbc42TimestampWithTimezoneType;

import jakarta.persistence.GenerationType;

/**
 * HSQLDBの方言を扱うクラスです。
 * 
 * @author taedium
 */
public class HsqlGenDialect extends StandardGenDialect {

    /** テーブルが見つからないことを示すエラーコード */
    //protected static int TABLE_NOT_FOUND_ERROR_CODE = -22;
    protected static int TABLE_NOT_FOUND_ERROR_CODE = -5501;

    /** カラムが見つからないことを示すエラーコード */
    protected static int COLUMN_NOT_FOUND_ERROR_CODE = -28;

    /** シーケンスが見つからないことを示すエラーコード */
    protected static int SEQUENCE_NOT_FOUND_ERROR_CODE = -191;

    /**
     * インスタンスを構築します。
     */
    public HsqlGenDialect() {
        sqlTypeMap.put(Types.BINARY, new BinaryType("varbinary($l)"));
        sqlTypeMap.put(Types.BLOB, new BlobType("longvarbinary"));
        sqlTypeMap.put(Types.CLOB, new ClobType("longvarchar"));
        sqlTypeMap.put(Types.TIMESTAMP_WITH_TIMEZONE, new Jdbc42TimestampWithTimezoneType());

        columnTypeMap.put("int", HsqlColumnType.INT);
        columnTypeMap.put("varchar_ignorecase",
                HsqlColumnType.VARCHAR_IGNORECASE);
    }

    @Override
    public String getName() {
        return "hsql";
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
    public boolean supportsSequence() {
        return true;
    }

    @Override
    public String getSequenceDefinitionFragment(String dataType,
            long initialValue, int allocationSize) {
        return "as " + dataType + " start with " + initialValue
                + " increment By " + allocationSize;
    }

    @Override
    public String getIdentityColumnDefinition() {
        return "generated by default as identity(start with 1)";
    }

    @Override
    public boolean isTableNotFound(Throwable throwable) {
        Integer errorCode = getErrorCode(throwable);
        return errorCode != null
                && errorCode.intValue() == TABLE_NOT_FOUND_ERROR_CODE;
    }

    @Override
    public boolean isColumnNotFound(Throwable throwable) {
        Integer errorCode = getErrorCode(throwable);
        return errorCode != null
                && errorCode.intValue() == COLUMN_NOT_FOUND_ERROR_CODE;
    }

    @Override
    public boolean isSequenceNotFound(Throwable throwable) {
        Integer errorCode = getErrorCode(throwable);
        return errorCode != null
                && errorCode.intValue() == SEQUENCE_NOT_FOUND_ERROR_CODE;
    }

    @Override
    public boolean supportsIdentity() {
        return true;
    }

    @Override
    public String getSequenceNextValString(String sequenceName,
            int allocationSize) {
        return "SELECT NEXT VALUE FOR "
                + sequenceName
                + " FROM INFORMATION_SCHEMA.SYSTEM_TABLES WHERE table_name = 'SYSTEM_TABLES'";
    }

    /**
     * HSQLDB用の{@link ColumnType}の実装です。
     * 
     * @author taedium
     */
    public static class HsqlColumnType extends StandardColumnType {

        private static HsqlColumnType INT = new HsqlColumnType("int",
                Integer.class);

        private static HsqlColumnType VARCHAR_IGNORECASE = new HsqlColumnType(
                "varchar_ignorecase", String.class);

        /**
         * インスタンスを構築します。
         * 
         * @param dataType
         *            データ型
         * @param attributeClass
         *            属性のクラス
         */
        public HsqlColumnType(String dataType, Class<?> attributeClass) {
            super(dataType, attributeClass);
        }

    }
}
