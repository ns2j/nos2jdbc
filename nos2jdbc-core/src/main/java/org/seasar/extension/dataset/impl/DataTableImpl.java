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
package org.seasar.extension.dataset.impl;

import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.seasar.extension.dataset.ColumnType;
import org.seasar.extension.dataset.DataColumn;
import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.states.RowStates;
import org.seasar.extension.dataset.types.ColumnTypes;
import org.seasar.extension.jdbc.ColumnNotFoundRuntimeException;
import org.seasar.extension.jdbc.util.ColumnDesc;
import org.seasar.extension.jdbc.util.DatabaseMetaDataUtil;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.util.ArrayMap;
import org.seasar.framework.util.CaseInsensitiveMap;
import org.seasar.framework.util.StringUtil;

/**
 * {@link DataTable}の実装クラスです。
 * 
 * @author higa
 * 
 */
public class DataTableImpl implements DataTable {

    private String tableName;

    private List<DataRow> rows = new ArrayList<>();

    private List<DataRow> removedRows = new ArrayList<>();

    private ArrayMap<String, DataColumn> columns = new CaseInsensitiveMap<>();

    private boolean hasMetaData = false;

    /**
     * {@link DataTableImpl}を作成します。
     * 
     * @param tableName table name
     */
    public DataTableImpl(String tableName) {
        setTableName(tableName);
    }

    /**
     * @see org.seasar.extension.dataset.DataTable#getTableName()
     */
    @Override
    public String getTableName() {
        return tableName;
    }

    /**
     * @see org.seasar.extension.dataset.DataTable#setTableName(java.lang.String)
     */
    @Override
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * @see org.seasar.extension.dataset.DataTable#getRowSize()
     */
    @Override
    public int getRowSize() {
        return rows.size();
    }

    /**
     * @see org.seasar.extension.dataset.DataTable#getRow(int)
     */
    @Override
    public DataRow getRow(int index) {
        return rows.get(index);
    }

    /**
     * @see org.seasar.extension.dataset.DataTable#addRow()
     */
    @Override
    public DataRow addRow() {
        DataRow row = new DataRowImpl(this);
        rows.add(row);
        row.setState(RowStates.CREATED);
        return row;
    }

    /**
     * @see org.seasar.extension.dataset.DataTable#getRemovedRowSize()
     */
    @Override
    public int getRemovedRowSize() {
        return removedRows.size();
    }

    /**
     * @see org.seasar.extension.dataset.DataTable#getRemovedRow(int)
     */
    @Override
    public DataRow getRemovedRow(int index) {
        return removedRows.get(index);
    }

    /**
     * @see org.seasar.extension.dataset.DataTable#removeRows()
     */
    @Override
    public DataRow[] removeRows() {
        for (int i = 0; i < rows.size();) {
            DataRow row = getRow(i);
            if (row.getState().equals(RowStates.REMOVED)) {
                removedRows.add(row);
                rows.remove(i);
            } else {
                ++i;
            }
        }
        return removedRows.toArray(new DataRow[removedRows.size()]);
    }

    /**
     * @see org.seasar.extension.dataset.DataTable#getColumnSize()
     */
    @Override
    public int getColumnSize() {
        return columns.size();
    }

    /**
     * @see org.seasar.extension.dataset.DataTable#getColumn(int)
     */
    @Override
    public DataColumn getColumn(int index) {
        return columns.get(index);
    }

    /**
     * @see org.seasar.extension.dataset.DataTable#getColumn(java.lang.String)
     */
    @Override
    public DataColumn getColumn(String columnName) {
        DataColumn column = getColumn0(columnName);
        if (column == null) {
            throw new ColumnNotFoundRuntimeException(tableName, columnName);
        }
        return column;
    }

    private DataColumn getColumn0(String columnName) {
        DataColumn column = columns.get(columnName);
        if (column == null) {
            String name = StringUtil.replace(columnName, "_", "");
            column = columns.get(name);
            if (column == null) {
                for (int i = 0; i < columns.size(); ++i) {
                    String key = columns.getKey(i);
                    String key2 = StringUtil.replace(key, "_", "");
                    if (key2.equalsIgnoreCase(name)) {
                        column = columns.get(i);
                        break;
                    }
                }
            }
        }
        return column;
    }

    /**
     * @see org.seasar.extension.dataset.DataTable#hasColumn(java.lang.String)
     */
    @Override
    public boolean hasColumn(String columnName) {
        return getColumn0(columnName) != null;
    }

    /**
     * @see org.seasar.extension.dataset.DataTable#getColumnName(int)
     */
    @Override
    public String getColumnName(int index) {
        return getColumn(index).getColumnName();
    }

    /**
     * @see org.seasar.extension.dataset.DataTable#getColumnType(int)
     */
    @Override
    public ColumnType getColumnType(int index) {
        return getColumn(index).getColumnType();
    }

    /**
     * @see org.seasar.extension.dataset.DataTable#getColumnType(java.lang.String)
     */
    @Override
    public ColumnType getColumnType(String columnName) {
        return getColumn(columnName).getColumnType();
    }

    /**
     * @see org.seasar.extension.dataset.DataTable#addColumn(java.lang.String)
     */
    @Override
    public DataColumn addColumn(String columnName) {
        return addColumn(columnName, ColumnTypes.OBJECT);
    }

    /**
     * @see org.seasar.extension.dataset.DataTable#addColumn(java.lang.String,
     *      org.seasar.extension.dataset.ColumnType)
     */
    @Override
    public DataColumn addColumn(String columnName, ColumnType columnType) {
        DataColumn column = new DataColumnImpl(columnName, columnType, columns
                .size());
        columns.put(columnName, column);
        return column;
    }

    /**
     * @see org.seasar.extension.dataset.DataTable#hasMetaData()
     */
    @Override
    public boolean hasMetaData() {
        return hasMetaData;
    }

    /**
     * @see org.seasar.extension.dataset.DataTable#setupMetaData(java.sql.DatabaseMetaData)
     */
    @Override
    public void setupMetaData(DatabaseMetaData dbMetaData) {
        Set<String> primaryKeySet = DatabaseMetaDataUtil.getPrimaryKeySet(dbMetaData,
                tableName);
        Map<String, ColumnDesc> columnMap = DatabaseMetaDataUtil
                .getColumnMap(dbMetaData, tableName);
        for (int i = 0; i < getColumnSize(); ++i) {
            DataColumn column = getColumn(i);
            if (primaryKeySet.contains(column.getColumnName())) {
                column.setPrimaryKey(true);
            } else {
                column.setPrimaryKey(false);
            }
            if (columnMap.containsKey(column.getColumnName())) {
                column.setWritable(true);
                ColumnDesc cd = columnMap.get(column
                        .getColumnName());
                column
                        .setColumnType(ColumnTypes.getColumnType(cd
                                .getSqlType()));
            } else {
                column.setWritable(false);
            }
        }
        hasMetaData = true;
    }

    /**
     * @see org.seasar.extension.dataset.DataTable#setupColumns(java.lang.Class)
     */
    @Override
    public void setupColumns(Class<?> beanClass) {
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(beanClass);
        for (int i = 0; i < beanDesc.getPropertyDescSize(); ++i) {
            PropertyDesc pd = beanDesc.getPropertyDesc(i);
            addColumn(pd.getPropertyName(), ColumnTypes.getColumnType(pd
                    .getPropertyType()));
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void copyFrom(Object source) {
        if (source instanceof List) {
            copyFromList((List) source);
        } else {
            copyFromBeanOrMap(source);
        }

    }

    private void copyFromList(List<?> source) {
        for (int i = 0; i < source.size(); ++i) {
            DataRow row = addRow();
            row.copyFrom(source.get(i));
            row.setState(RowStates.UNCHANGED);
        }
    }

    private void copyFromBeanOrMap(Object source) {
        DataRow row = addRow();
        row.copyFrom(source);
        row.setState(RowStates.UNCHANGED);
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer(100);
        buf.append(tableName);
        buf.append(":");
        for (int i = 0; i < columns.size(); ++i) {
            buf.append(getColumnName(i));
            buf.append(", ");
        }
        buf.setLength(buf.length() - 2);
        buf.append("\n");
        for (int i = 0; i < rows.size(); ++i) {
            buf.append(getRow(i) + "\n");
        }
        buf.setLength(buf.length() - 1);
        return buf.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DataTable)) {
            return false;
        }
        DataTable other = (DataTable) o;
        if (getRowSize() != other.getRowSize()) {
            return false;
        }
        for (int i = 0; i < getRowSize(); ++i) {
            if (!getRow(i).equals(other.getRow(i))) {
                return false;
            }
        }
        if (getRemovedRowSize() != other.getRemovedRowSize()) {
            return false;
        }
        for (int i = 0; i < getRemovedRowSize(); ++i) {
            if (!getRemovedRow(i).equals(other.getRemovedRow(i))) {
                return false;
            }
        }
        return true;
    }
}