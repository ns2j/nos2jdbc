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

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.seasar.framework.conversion.BigDecimalConversionUtil;
import org.seasar.framework.conversion.BooleanConversionUtil;
import org.seasar.framework.conversion.ByteConversionUtil;
import org.seasar.framework.conversion.DoubleConversionUtil;
import org.seasar.framework.conversion.FloatConversionUtil;
import org.seasar.framework.conversion.IntegerConversionUtil;
import org.seasar.framework.conversion.LongConversionUtil;
import org.seasar.framework.conversion.ShortConversionUtil;
import org.seasar.framework.conversion.SqlDateConversionUtil;
import org.seasar.framework.conversion.StringConversionUtil;
import org.seasar.framework.conversion.TimeConversionUtil;
import org.seasar.framework.conversion.TimestampConversionUtil;
import org.seasar.framework.util.ArrayMap;

/**
 * {@link ResultSet}用のモッククラスです。
 * 
 * @author higa
 * 
 */
public class MockResultSet implements ResultSet {

    private MockResultSetMetaData metaData;

    private List<ArrayMap> rowDataList = new ArrayList<>();

    private int row = 0;

    private int type;

    private int concurrency;

    private boolean closed = false;

    private int fetchSize;

    private MockStatement statement;

    /**
     * {@link MockResultSet}を作成します。
     * 
     */
    public MockResultSet() {
        this(null);
    }

    /**
     * {@link MockResultSet}を作成します。
     * 
     * @param metaData
     *            結果セットメタデータ
     */
    public MockResultSet(MockResultSetMetaData metaData) {
        this(metaData, TYPE_FORWARD_ONLY, CONCUR_READ_ONLY);
    }

    /**
     * {@link MockResultSet}を作成します。
     * 
     * @param metaData
     *            結果セットメタデータ
     * @param type
     *            結果セットタイプ
     * @param concurrency
     *            結果セット同時並行性
     */
    public MockResultSet(MockResultSetMetaData metaData, int type,
            int concurrency) {
        setMockMetaData(metaData);
        setType(type);
        setConcurrency(concurrency);
    }

    /**
     * 現在の行データを返します。
     * 
     * @return 現在の行データ
     */
    public ArrayMap getRowData() {
        return getRowData(row);
    }

    /**
     * <p>
     * 特定の行のデータを返します。
     * </p>
     * <p>
     * 行番号は1からはじまります。
     * </p>
     * 
     * @param index
     *            行番号
     * @return 特定の行のデータ
     */
    public ArrayMap getRowData(int index) {
        return rowDataList.get(index - 1);
    }

    /**
     * <p>
     * 特定のカラムのデータを返します。
     * </p>
     * <p>
     * カラム番号は1からはじまります。
     * </p>
     * 
     * @param index
     *            カラム番号
     * @return 特定のカラムのデータ
     */
    public Object getColumnData(int index) {
        return getRowData().get(index - 1);
    }

    /**
     * <p>
     * 特定のカラムのデータを返します。
     * </p>
     * 
     * @param columnName
     *            カラム名
     * @return 特定のカラムのデータ
     */
    public Object getColumnData(String columnName) {
        return getRowData().get(columnName);
    }

    /**
     * 行データを追加します。
     * 
     * @param rowData
     *            行データ
     */
    public void addRowData(ArrayMap rowData) {
        rowDataList.add(rowData);
    }

    @Override
    public boolean absolute(int row) throws SQLException {
        assertCursor();
        if (row > 0 && row <= rowDataList.size()) {
            this.row = row;
            return true;
        }
        return false;
    }

    /**
     * カーソルが使えることを表明します。
     * 
     * @throws SQLException
     *             カーソルが使えない場合。
     */
    protected void assertCursor() throws SQLException {
        if (type == TYPE_FORWARD_ONLY) {
            throw new SQLException("cursor not supported");
        }
    }

    @Override
    public void afterLast() throws SQLException {
        assertCursor();
        row = rowDataList.size() + 1;
    }

    @Override
    public void beforeFirst() throws SQLException {
        assertCursor();
        row = 0;
    }

    @Override
    public void cancelRowUpdates() throws SQLException {
        assertUpdatable();
    }

    /**
     * 更新可能であることを表明します。
     * 
     * @throws SQLException
     *             更新可能でない場合。
     */
    protected void assertUpdatable() throws SQLException {
        if (concurrency == CONCUR_READ_ONLY) {
            throw new SQLException("update not supported");
        }
    }

    @Override
    public void clearWarnings() throws SQLException {
    }

    @Override
    public void close() throws SQLException {
        closed = true;
    }

    /**
     * 閉じているかどうかを返します。
     * 
     * @return 閉じているかどうか
     */
    @Override
    public boolean isClosed() {
        return closed;
    }

    @Override
    public void deleteRow() throws SQLException {
        assertUpdatable();
    }

    @Override
    public int findColumn(String columnName) throws SQLException {
        return metaData.findColumn(columnName);
    }

    @Override
    public boolean first() throws SQLException {
        return absolute(1);
    }

    @Override
    public Array getArray(int columnIndex) throws SQLException {
        return (Array) getColumnData(columnIndex);
    }

    @Override
    public Array getArray(String columnName) throws SQLException {
        return (Array) getColumnData(columnName);
    }

    @Override
    public InputStream getAsciiStream(int columnIndex) throws SQLException {
        return (InputStream) getColumnData(columnIndex);
    }

    @Override
    public InputStream getAsciiStream(String columnName) throws SQLException {
        return (InputStream) getColumnData(columnName);
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        return BigDecimalConversionUtil
                .toBigDecimal(getColumnData(columnIndex));
    }

    @Override
    public BigDecimal getBigDecimal(String columnName) throws SQLException {
        return BigDecimalConversionUtil.toBigDecimal(getColumnData(columnName));
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex, int scale)
            throws SQLException {
        BigDecimal value = getBigDecimal(columnIndex);
        if (value == null) {
            return null;
        }
        value = value.setScale(scale);
        return value;
    }

    @Override
    public BigDecimal getBigDecimal(String columnName, int scale)
            throws SQLException {
        BigDecimal value = getBigDecimal(columnName);
        if (value == null) {
            return null;
        }
        value = value.setScale(scale);
        return value;
    }

    @Override
    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        return (InputStream) getColumnData(columnIndex);
    }

    @Override
    public InputStream getBinaryStream(String columnName) throws SQLException {
        return (InputStream) getColumnData(columnName);
    }

    @Override
    public Blob getBlob(int columnIndex) throws SQLException {
        return (Blob) getColumnData(columnIndex);
    }

    @Override
    public Blob getBlob(String columnName) throws SQLException {
        return (Blob) getColumnData(columnName);
    }

    @Override
    public boolean getBoolean(int columnIndex) throws SQLException {
        return BooleanConversionUtil
                .toPrimitiveBoolean(getColumnData(columnIndex));
    }

    @Override
    public boolean getBoolean(String columnName) throws SQLException {
        return BooleanConversionUtil
                .toPrimitiveBoolean(getColumnData(columnName));
    }

    @Override
    public byte getByte(int columnIndex) throws SQLException {
        return ByteConversionUtil.toPrimitiveByte(getColumnData(columnIndex));
    }

    @Override
    public byte getByte(String columnName) throws SQLException {
        return ByteConversionUtil.toPrimitiveByte(getColumnData(columnName));
    }

    @Override
    public byte[] getBytes(int columnIndex) throws SQLException {
        return (byte[]) getColumnData(columnIndex);
    }

    @Override
    public byte[] getBytes(String columnName) throws SQLException {
        return (byte[]) getColumnData(columnName);
    }

    @Override
    public Reader getCharacterStream(int columnIndex) throws SQLException {
        return (Reader) getColumnData(columnIndex);
    }

    @Override
    public Reader getCharacterStream(String columnName) throws SQLException {
        return (Reader) getColumnData(columnName);
    }

    @Override
    public Clob getClob(int columnIndex) throws SQLException {
        return (Clob) getColumnData(columnIndex);
    }

    @Override
    public Clob getClob(String columnName) throws SQLException {
        return (Clob) getColumnData(columnName);
    }

    @Override
    public int getConcurrency() throws SQLException {
        return concurrency;
    }

    /**
     * 結果セット同時並行性を設定します。
     * 
     * @param concurrency
     *            結果セット同時並行性
     */
    public void setConcurrency(int concurrency) {
        this.concurrency = concurrency;
    }

    @Override
    public String getCursorName() throws SQLException {
        return null;
    }

    @Override
    public Date getDate(int columnIndex) throws SQLException {
        return SqlDateConversionUtil.toDate(getColumnData(columnIndex));
    }

    @Override
    public Date getDate(String columnName) throws SQLException {
        return SqlDateConversionUtil.toDate(getColumnData(columnName));
    }

    @Override
    public Date getDate(int columnIndex, Calendar cal) throws SQLException {
        return getDate(columnIndex);
    }

    @Override
    public Date getDate(String columnName, Calendar cal) throws SQLException {
        return getDate(columnName);
    }

    @Override
    public double getDouble(int columnIndex) throws SQLException {
        return DoubleConversionUtil
                .toPrimitiveDouble(getColumnData(columnIndex));
    }

    @Override
    public double getDouble(String columnName) throws SQLException {
        return DoubleConversionUtil
                .toPrimitiveDouble(getColumnData(columnName));
    }

    @Override
    public int getFetchDirection() throws SQLException {
        return 0;
    }

    @Override
    public int getFetchSize() throws SQLException {
        return fetchSize;
    }

    @Override
    public float getFloat(int columnIndex) throws SQLException {
        return FloatConversionUtil.toPrimitiveFloat(getColumnData(columnIndex));
    }

    @Override
    public float getFloat(String columnName) throws SQLException {
        return FloatConversionUtil.toPrimitiveFloat(getColumnData(columnName));
    }

    @Override
    public int getInt(int columnIndex) throws SQLException {
        return IntegerConversionUtil.toPrimitiveInt(getColumnData(columnIndex));
    }

    @Override
    public int getInt(String columnName) throws SQLException {
        return IntegerConversionUtil.toPrimitiveInt(getColumnData(columnName));
    }

    @Override
    public long getLong(int columnIndex) throws SQLException {
        return LongConversionUtil.toPrimitiveLong(getColumnData(columnIndex));
    }

    @Override
    public long getLong(String columnName) throws SQLException {
        return LongConversionUtil.toPrimitiveLong(getColumnData(columnName));
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return getMockMetaData();
    }

    /**
     * {@link ResultSetMetaData}用のモックを返します。
     * 
     * @return {@link ResultSetMetaData}用のモック
     */
    public MockResultSetMetaData getMockMetaData() {
        return metaData;
    }

    /**
     * {@link ResultSetMetaData}用のモックを設定します。
     * 
     * @param metaData
     *            {@link ResultSetMetaData}用のモック
     */
    public void setMockMetaData(MockResultSetMetaData metaData) {
        this.metaData = metaData;
    }

    @Override
    public Object getObject(int columnIndex) throws SQLException {
        return getColumnData(columnIndex);
    }

    @Override
    public Object getObject(String columnName) throws SQLException {
        return getColumnData(columnName);
    }

    @Override
    public Object getObject(int columnIndex, Map<String, Class<?>> arg1) throws SQLException {
        return getObject(columnIndex);
    }

    @Override
    public Object getObject(String columnName, Map<String, Class<?>> arg1) throws SQLException {
        return getObject(columnName);
    }

    @Override
    public Ref getRef(int columnIndex) throws SQLException {
        return (Ref) getColumnData(columnIndex);
    }

    @Override
    public Ref getRef(String columnName) throws SQLException {
        return (Ref) getColumnData(columnName);
    }

    @Override
    public int getRow() throws SQLException {
        return row;
    }

    @Override
    public short getShort(int columnIndex) throws SQLException {
        return ShortConversionUtil.toPrimitiveShort(getColumnData(columnIndex));
    }

    @Override
    public short getShort(String columnName) throws SQLException {
        return ShortConversionUtil.toPrimitiveShort(getColumnData(columnName));
    }

    @Override
    public Statement getStatement() throws SQLException {
        return getMockStatement();
    }

    /**
     * ステートメント用のモックを返します。
     * 
     * @return ステートメント用のモック
     */
    public MockStatement getMockStatement() {
        return statement;
    }

    /**
     * ステートメント用のモックを設定します。
     * 
     * @param statement
     *            ステートメント用のモック
     */
    public void setMockStatement(MockStatement statement) {
        this.statement = statement;
    }

    @Override
    public String getString(int columnIndex) throws SQLException {
        return StringConversionUtil.toString(getColumnData(columnIndex));
    }

    @Override
    public String getString(String columnName) throws SQLException {
        return StringConversionUtil.toString(getColumnData(columnName));
    }

    @Override
    public Time getTime(int columnIndex) throws SQLException {
        return TimeConversionUtil.toTime(getColumnData(columnIndex));
    }

    @Override
    public Time getTime(String columnName) throws SQLException {
        return TimeConversionUtil.toTime(getColumnData(columnName));
    }

    @Override
    public Time getTime(int columnIndex, Calendar cal) throws SQLException {
        return getTime(columnIndex);
    }

    @Override
    public Time getTime(String columnName, Calendar cal) throws SQLException {
        return getTime(columnName);
    }

    @Override
    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        return TimestampConversionUtil.toTimestamp(getColumnData(columnIndex));
    }

    @Override
    public Timestamp getTimestamp(String columnName) throws SQLException {
        return TimestampConversionUtil.toTimestamp(getColumnData(columnName));
    }

    @Override
    public Timestamp getTimestamp(int columnIndex, Calendar cal)
            throws SQLException {
        return getTimestamp(columnIndex);
    }

    @Override
    public Timestamp getTimestamp(String columnName, Calendar cal)
            throws SQLException {
        return getTimestamp(columnName);
    }

    @Override
    public int getType() throws SQLException {
        return type;
    }

    /**
     * 結果セットタイプを設定します。
     * 
     * @param type
     *            結果セットタイプ
     */
    public void setType(int type) {
        this.type = type;
    }

    @Override
    public URL getURL(int columnIndex) throws SQLException {
        return (URL) getColumnData(columnIndex);
    }

    @Override
    public URL getURL(String columnName) throws SQLException {
        return (URL) getColumnData(columnName);
    }

    @Override
    public InputStream getUnicodeStream(int columnIndex) throws SQLException {
        return (InputStream) getColumnData(columnIndex);
    }

    @Override
    public InputStream getUnicodeStream(String columnName) throws SQLException {
        return (InputStream) getColumnData(columnName);
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    @Override
    public void insertRow() throws SQLException {
        assertUpdatable();
    }

    @Override
    public boolean isAfterLast() throws SQLException {
        return row > rowDataList.size();
    }

    @Override
    public boolean isBeforeFirst() throws SQLException {
        return row == 0;
    }

    @Override
    public boolean isFirst() throws SQLException {
        return row == 1;
    }

    @Override
    public boolean isLast() throws SQLException {
        return row == rowDataList.size();
    }

    @Override
    public boolean last() throws SQLException {
        return absolute(rowDataList.size());
    }

    @Override
    public void moveToCurrentRow() throws SQLException {
    }

    @Override
    public void moveToInsertRow() throws SQLException {
    }

    @Override
    public boolean next() throws SQLException {
        if (row < rowDataList.size()) {
            ++row;
            return true;
        } else if (row == rowDataList.size()) {
            ++row;
            return false;
        }
        return false;
    }

    @Override
    public boolean previous() throws SQLException {
        assertCursor();
        if (row > 1) {
            --row;
            return true;
        } else if (row == 1) {
            --row;
            return false;
        }
        return false;
    }

    @Override
    public void refreshRow() throws SQLException {
    }

    @Override
    public boolean relative(int rows) throws SQLException {
        return absolute(row + rows);
    }

    @Override
    public boolean rowDeleted() throws SQLException {
        return false;
    }

    @Override
    public boolean rowInserted() throws SQLException {
        return false;
    }

    @Override
    public boolean rowUpdated() throws SQLException {
        return false;
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {
    }

    @Override
    public void setFetchSize(int fetchSize) throws SQLException {
        this.fetchSize = fetchSize;
    }

    @Override
    public void updateArray(int columnIndex, Array x) throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateArray(String columnName, Array x) throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x, int length)
            throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateAsciiStream(String columnName, InputStream x, int length)
            throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateBigDecimal(int columnIndex, BigDecimal x)
            throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateBigDecimal(String columnName, BigDecimal x)
            throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x, int length)
            throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateBinaryStream(String columnName, InputStream x, int length)
            throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateBlob(int columnIndex, Blob x) throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateBlob(String columnName, Blob x) throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateBoolean(int columnIndex, boolean x) throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateBoolean(String columnName, boolean x) throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateByte(int columnIndex, byte x) throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateByte(String columnName, byte x) throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateBytes(int columnIndex, byte[] x) throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateBytes(String columnName, byte[] x) throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x, int length)
            throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateCharacterStream(String columnName, Reader reader,
            int length) throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateClob(int columnIndex, Clob x) throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateClob(String columnName, Clob x) throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateDate(int columnIndex, Date x) throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateDate(String columnName, Date x) throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateDouble(int columnIndex, double x) throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateDouble(String columnName, double x) throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateFloat(int columnIndex, float x) throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateFloat(String columnName, float x) throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateInt(int columnIndex, int x) throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateInt(String columnName, int x) throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateLong(int columnIndex, long x) throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateLong(String columnName, long x) throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateNull(int columnIndex) throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateNull(String columnName) throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateObject(int columnIndex, Object x) throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateObject(String columnName, Object x) throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateObject(int columnIndex, Object x, int scale)
            throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateObject(String columnName, Object x, int scale)
            throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateRef(int columnIndex, Ref x) throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateRef(String columnName, Ref x) throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateRow() throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateShort(int columnIndex, short x) throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateShort(String columnName, short x) throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateString(int columnIndex, String x) throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateString(String columnName, String x) throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateTime(int columnIndex, Time x) throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateTime(String columnName, Time x) throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateTimestamp(int columnIndex, Timestamp x)
            throws SQLException {
        assertUpdatable();
    }

    @Override
    public void updateTimestamp(String columnName, Timestamp x)
            throws SQLException {
        assertUpdatable();
    }

    @Override
    public boolean wasNull() throws SQLException {
        return false;
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

    @Override
    public RowId getRowId(int columnIndex) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public RowId getRowId(String columnLabel) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void updateRowId(int columnIndex, RowId x) throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void updateRowId(String columnLabel, RowId x) throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public int getHoldability() throws SQLException {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public void updateNString(int columnIndex, String nString)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void updateNString(String columnLabel, String nString)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void updateNClob(String columnLabel, NClob nClob)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public NClob getNClob(int columnIndex) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public NClob getNClob(String columnLabel) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public SQLXML getSQLXML(int columnIndex) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public SQLXML getSQLXML(String columnLabel) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void updateSQLXML(int columnIndex, SQLXML xmlObject)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void updateSQLXML(String columnLabel, SQLXML xmlObject)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public String getNString(int columnIndex) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String getNString(String columnLabel) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Reader getNCharacterStream(int columnIndex) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Reader getNCharacterStream(String columnLabel) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void updateNCharacterStream(int columnIndex, Reader x, long length)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader,
	    long length) throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x, long length)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x, long length)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x, long length)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x,
	    long length) throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x,
	    long length) throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader,
	    long length) throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void updateBlob(int columnIndex, InputStream inputStream,
	    long length) throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void updateBlob(String columnLabel, InputStream inputStream,
	    long length) throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void updateClob(int columnIndex, Reader reader, long length)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void updateClob(String columnLabel, Reader reader, long length)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void updateNClob(int columnIndex, Reader reader, long length)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void updateNClob(String columnLabel, Reader reader, long length)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void updateNCharacterStream(int columnIndex, Reader x)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void updateBlob(int columnIndex, InputStream inputStream)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void updateBlob(String columnLabel, InputStream inputStream)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void updateClob(int columnIndex, Reader reader) throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void updateClob(String columnLabel, Reader reader)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void updateNClob(int columnIndex, Reader reader)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void updateNClob(String columnLabel, Reader reader)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public <T> T getObject(String columnLabel, Class<T> type)
	    throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }
}