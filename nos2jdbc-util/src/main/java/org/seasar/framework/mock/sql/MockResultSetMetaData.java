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
package org.seasar.framework.mock.sql;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link ResultSetMetaData}用のモッククラスです。
 * 
 * @author higa
 * 
 */
public class MockResultSetMetaData implements ResultSetMetaData {

    private List<MockColumnMetaData> columnMetaDataList = new ArrayList<>();

    /**
     * カラムのメタデータを返します。
     * 
     * @param index
     *            インデックス
     * @return カラムのメタデータ
     */
    public MockColumnMetaData getColumnMetaData(int index) {
        return columnMetaDataList.get(index - 1);
    }

    /**
     * カラムのメタデータを追加します。
     * 
     * @param columnMetaData
     *            カラムのメタデータ
     */
    public void addColumnMetaData(MockColumnMetaData columnMetaData) {
        columnMetaDataList.add(columnMetaData);
    }

    @Override
    public String getCatalogName(int column) throws SQLException {
        return getColumnMetaData(column).getCatalogName();
    }

    @Override
    public String getColumnClassName(int column) throws SQLException {
        return getColumnMetaData(column).getColumnClassName();
    }

    @Override
    public int getColumnCount() throws SQLException {
        return columnMetaDataList.size();
    }

    @Override
    public int getColumnDisplaySize(int column) throws SQLException {
        return getColumnMetaData(column).getColumnDisplaySize();
    }

    @Override
    public String getColumnLabel(int column) throws SQLException {
        return getColumnMetaData(column).getColumnLabel();
    }

    @Override
    public String getColumnName(int column) throws SQLException {
        return getColumnMetaData(column).getColumnName();
    }

    @Override
    public int getColumnType(int column) throws SQLException {
        return getColumnMetaData(column).getColumnType();
    }

    @Override
    public String getColumnTypeName(int column) throws SQLException {
        return getColumnMetaData(column).getColumnTypeName();
    }

    @Override
    public int getPrecision(int column) throws SQLException {
        return getColumnMetaData(column).getPrecision();
    }

    @Override
    public int getScale(int column) throws SQLException {
        return getColumnMetaData(column).getScale();
    }

    @Override
    public String getSchemaName(int column) throws SQLException {
        return getColumnMetaData(column).getSchemaName();
    }

    @Override
    public String getTableName(int column) throws SQLException {
        return getColumnMetaData(column).getTableName();
    }

    @Override
    public boolean isAutoIncrement(int column) throws SQLException {
        return getColumnMetaData(column).isAutoIncrement();
    }

    @Override
    public boolean isCaseSensitive(int column) throws SQLException {
        return getColumnMetaData(column).isCaseSensitive();
    }

    @Override
    public boolean isCurrency(int column) throws SQLException {
        return getColumnMetaData(column).isCurrency();
    }

    @Override
    public boolean isDefinitelyWritable(int column) throws SQLException {
        return getColumnMetaData(column).isDefinitelyWritable();
    }

    @Override
    public int isNullable(int column) throws SQLException {
        return getColumnMetaData(column).isNullable();
    }

    @Override
    public boolean isReadOnly(int column) throws SQLException {
        return getColumnMetaData(column).isReadOnly();
    }

    @Override
    public boolean isSearchable(int column) throws SQLException {
        return getColumnMetaData(column).isSearchable();
    }

    @Override
    public boolean isSigned(int column) throws SQLException {
        return getColumnMetaData(column).isSigned();
    }

    @Override
    public boolean isWritable(int column) throws SQLException {
        return getColumnMetaData(column).isWritable();
    }

    /**
     * カラム番号を返します。
     * 
     * @param columnName
     *            カラム名
     * @return カラム番号
     * @throws SQLException
     *             カラム名が見つからなかった場合。
     */
    public int findColumn(String columnName) throws SQLException {
        for (int i = 1; i <= getColumnCount(); i++) {
            MockColumnMetaData columnMetaData = getColumnMetaData(i);
            if (columnName.equals(columnMetaData.getColumnName())) {
                return i;
            }
        }
        throw new SQLException(columnName + " not found.");
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
	// TODO Auto-generated method stub
	return false;
    }
}