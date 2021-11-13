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
package nos2jdbc.unit;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.seasar.extension.dataset.ColumnType;
import org.seasar.extension.dataset.DataColumn;
import org.seasar.extension.dataset.DataReader;
import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.DataWriter;
import org.seasar.extension.dataset.impl.DataSetImpl;
import org.seasar.extension.dataset.impl.SqlDeleteTableWriter;
import org.seasar.extension.dataset.impl.SqlReader;
import org.seasar.extension.dataset.impl.SqlReloadReader;
import org.seasar.extension.dataset.impl.SqlReloadTableReader;
import org.seasar.extension.dataset.impl.SqlTableReader;
import org.seasar.extension.dataset.impl.SqlWriter;
import org.seasar.extension.dataset.impl.SsReader;
import org.seasar.extension.dataset.impl.SsWriter;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.UpdateHandler;
import org.seasar.extension.jdbc.impl.BasicUpdateHandler;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.extension.jdbc.util.ConnectionUtil;
import org.seasar.extension.jdbc.util.DataSourceUtil;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.convention.PersistenceConvention;
import org.seasar.framework.util.FileOutputStreamUtil;
import org.seasar.framework.util.ResourceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataSetOpe {
    final static private Logger logger = LoggerFactory.getLogger(DataSetOpe.class);

    private JdbcManagerImplementor jdbcManager;

    private DataSource dataSource;

    private Connection connection;

    private DatabaseMetaData dbMetaData;

    /**
     * {@link DataSetOpe}を作成します。
     * @param jdbcManager JdbcManager
     */
    public DataSetOpe(JdbcManager jdbcManager) {
        this.jdbcManager = (JdbcManagerImplementor)jdbcManager;
        this.dataSource = this.jdbcManager.getDataSource();
    }

    public Connection getConnection() {
        if (connection != null) {
            return connection;
        }
        connection = DataSourceUtil.getConnection(dataSource);
        return connection;
    }

    /**
     * データベースメタデータを返します。
     * @return データベースメタデータ
     */
    public DatabaseMetaData getDatabaseMetaData() {
        if (dbMetaData != null) {
            return dbMetaData;
        }
        dbMetaData = ConnectionUtil.getMetaData(getConnection());
        return dbMetaData;
    }

    /**
     * エクセルを読み込みます。
     * 
     * @param path
     *            パス
     * @return エクセルのデータ
     */
    public DataSet readSs(String path) {
        return readSs(path, true);
    }

    /**
     * エクセルを読み込みます。
     * 
     * @param path
     *            パス
     * @param trimString
     *            文字列をトリムするかどうか
     * @return エクセルのデータ
     */
    public DataSet readSs(String path, boolean trimString) {
        DataReader reader = new SsReader(path, trimString);
        return reader.read();
    }

    /**
     * エクセルに書き込みます。
     * 
     * @param path
     *            パス
     * @param dataSet
     *            データセット
     */
    public void writeSs(String path, DataSet dataSet) {
        File dir = ResourceUtil.getBuildDir(getClass());
        File file = new File(dir, path);
        DataWriter writer = new SsWriter(FileOutputStreamUtil.create(file));
        writer.write(dataSet);
    }

    /**
     * データベースに書き込みます。
     * 
     * @param dataSet
     *            データセット
     */
    public void writeDb(DataSet dataSet) {
        SqlWriter writer = getSqlWriter();
        writer.write(dataSet);
    }

    /**
     * {@link SqlWriter}を返します。
     * 
     * @return {@link SqlWriter}
     */
    protected SqlWriter getSqlWriter() {
        return new SqlWriter(dataSource);
    }

    /**
     * データベースの内容を読み込みます。
     * 
     * @param dataSet
     *            データセット
     * @return データベースの内容
     */
    public DataSet readDb(DataSet dataSet) {
        SqlReader reader = new SqlReader(dataSource);
        reader.addDataSet(dataSet);
        return reader.read();
    }

    /**
     * テーブルの内容を読み込みます。
     * 
     * @param table
     *            テーブル
     * @return テーブルの内容
     */
    public DataTable readDbByTable(String table) {
        return readDbByTable(table, null);
    }

    /**
     * テーブルの内容を読み込みます。
     * 
     * @param table
     *            テーブル名
     * @param condition
     *            条件
     * @return テーブルの内容
     */
    public DataTable readDbByTable(String table, String condition) {
        SqlTableReader reader = new SqlTableReader(dataSource);
        reader.setTable(table, condition);
        return reader.read();
    }

    /**
     * テーブルの内容を読み込みます。
     * 
     * @param sql
     *            SQL
     * @param tableName
     *            テーブル名
     * @return テーブルの内容
     */
    public DataTable readDbBySql(String sql, String tableName) {
        SqlTableReader reader = new SqlTableReader(dataSource);
        reader.setSql(sql, tableName);
        return reader.read();
    }

    /**
     * エクセルから読み込んだデータをデータベースに書き出します。
     * 
     * @param path
     *            パス
     */
    public void readSsWriteDb(String path) {
        readSsWriteDb(path, true);
    }

    /**
     * エクセルから読み込んだデータをデータベースに書き出します。
     * 
     * @param path
     *            パス
     * @param trimString
     *            文字列をトリムするかどうか
     */
    public void readSsWriteDb(String path, boolean trimString) {
        writeDb(readSs(path, trimString));
    }

    /**
     * エクセルから読み込んだデータでそのテーブルの内容を置き換えます。
     * 
     * @param path
     *            パス
     */
    public void readSsReplaceDb(String path) {
        readSsReplaceDb(path, true);
    }

    /**
     * エクセルから読み込んだデータでそのテーブルの内容を置き換えます。
     * 
     * @param path
     *            パス
     * @param trimString
     *            文字列をトリムするかどうか
     */
    public void readSsReplaceDb(String path, boolean trimString) {
        DataSet dataSet = readSs(path, trimString);
        deleteDb(dataSet);
        writeDb(dataSet);
    }

    /**
     * エクセルから読み込んだデータでそのテーブルの内容を全部置き換えます。
     * 
     * @param path
     *            パス
     */
    public void readSsAllReplaceDb(String path) {
        readSsAllReplaceDb(path, true);
    }

    /**
     * エクセルから読み込んだデータでそのテーブルの内容を全部置き換えます。
     * 
     * @param path
     *            パス
     * @param trimString
     *            文字列をトリムするかどうか
     */
    public void readSsAllReplaceDb(String path, boolean trimString) {
        DataSet dataSet = readSs(path, trimString);
        for (int i = dataSet.getTableSize() - 1; i >= 0; --i) {
            deleteTable(dataSet.getTable(i).getTableName());
        }
        writeDb(dataSet);
    }

    /**
     * リロードします。
     * 
     * @param dataSet
     *            データセット
     * @return リロードした結果
     */
    public DataSet reload(DataSet dataSet) {
        return new SqlReloadReader(dataSource, dataSet).read();
    }

    /**
     * @param table DataTable
     * @return DataTable
     */
    public DataTable reload(DataTable table) {
        return new SqlReloadTableReader(dataSource, table).read();
    }

    /**
     * テーブルの内容をリロードもしくは読み込みます。
     * 
     * @param dataSet
     *            データセット
     * @return 読み込んだ結果
     */
    public DataSet reloadOrReadDb(DataSet dataSet) {
        DataSet newDataSet = new DataSetImpl();
        outer: for (int i = 0; i < dataSet.getTableSize(); i++) {
            DataTable table = dataSet.getTable(i);
            if (!table.hasMetaData()) {
                table.setupMetaData(getDatabaseMetaData());
            }
            for (int j = 0; j < table.getColumnSize(); j++) {
                DataColumn column = table.getColumn(j);
                if (column.isPrimaryKey()) {
                    newDataSet.addTable(reload(table));
                    continue outer;
                }
            }
            newDataSet.addTable(readDbByTable(table.getTableName()));
        }
        return newDataSet;
    }

    /**
     * データベースからデータを削除します。
     * 
     * @param dataSet
     *            データセット
     */
    public void deleteDb(DataSet dataSet) {
        SqlDeleteTableWriter writer = new SqlDeleteTableWriter(dataSource);
        for (int i = dataSet.getTableSize() - 1; i >= 0; --i) {
            writer.write(dataSet.getTable(i));
        }
    }

    /**
     * テーブルのデータを削除します。
     * 
     * @param tableName
     *            テーブル名
     */
    public void deleteTable(String tableName) {
        UpdateHandler handler = new BasicUpdateHandler(dataSource,
                "DELETE FROM " + tableName);
        handler.execute(null);
    }

    public <T> T createFromRow(Class<T> entityClass, DataRow row) {
        T entity = null;
        try {
            entity = entityClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
        setFromRow(row, entity);
        return entity;
    }

    public void setFromRow(DataRow row, Object entity) {
        BeanDesc desc = BeanDescFactory.getBeanDesc(entity.getClass());
        DataTable table = row.getTable();
        for (int i = 0; i < table.getColumnSize(); i++) {
            DataColumn col = table.getColumn(i);
            ColumnType ct = col.getColumnType();
            Object value = row.getValue(i);
            PersistenceConvention pc = jdbcManager.getPersistenceConvention();
            String propName = pc.fromColumnNameToPropertyName(col.getColumnName());
            PropertyDesc propDesc = desc.getPropertyDesc(propName);
            value = ct.convert(propDesc.getPropertyType(), value);
            propDesc.setValue(entity, value);
        }
    }
    
    public <T> List<T> createFromTable(Class<T> clazz, DataTable table) {
        List<T> objs = new ArrayList<>();
        for (int i = 0; i < table.getRowSize(); i++)
            objs.add(createFromRow(clazz, table.getRow(i)));
        return objs;
    }

}