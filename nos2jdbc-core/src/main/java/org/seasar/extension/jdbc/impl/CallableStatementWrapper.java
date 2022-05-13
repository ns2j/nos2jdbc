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
package org.seasar.extension.jdbc.impl;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

/**
 * {@link CallableStatement}のラッパです。
 * 
 * @author higa
 * 
 */
public class CallableStatementWrapper implements CallableStatement {

    private CallableStatement original;

    /**
     * {@link CallableStatementWrapper}を作成します。
     * 
     * @param original
     *            オリジナル
     * 
     */
    public CallableStatementWrapper(CallableStatement original) {
        this.original = original;
    }

    /**
     * @see java.sql.CallableStatement#wasNull()
     */
    @Override
    public boolean wasNull() throws SQLException {
        return original.wasNull();
    }

    /**
     * @see java.sql.CallableStatement#getByte(int)
     */
    @Override
    public byte getByte(int parameterIndex) throws SQLException {
        return original.getByte(parameterIndex);
    }

    /**
     * @see java.sql.CallableStatement#getDouble(int)
     */
    @Override
    public double getDouble(int parameterIndex) throws SQLException {
        return original.getDouble(parameterIndex);
    }

    /**
     * @see java.sql.CallableStatement#getFloat(int)
     */
    @Override
    public float getFloat(int parameterIndex) throws SQLException {
        return original.getFloat(parameterIndex);
    }

    /**
     * @see java.sql.CallableStatement#getInt(int)
     */
    @Override
    public int getInt(int parameterIndex) throws SQLException {
        return original.getInt(parameterIndex);
    }

    /**
     * @see java.sql.CallableStatement#getLong(int)
     */
    @Override
    public long getLong(int parameterIndex) throws SQLException {
        return original.getLong(parameterIndex);
    }

    /**
     * @see java.sql.CallableStatement#getShort(int)
     */
    @Override
    public short getShort(int parameterIndex) throws SQLException {
        return original.getShort(parameterIndex);
    }

    /**
     * @see java.sql.CallableStatement#getBoolean(int)
     */
    @Override
    public boolean getBoolean(int parameterIndex) throws SQLException {
        return original.getBoolean(parameterIndex);
    }

    /**
     * @see java.sql.CallableStatement#getBytes(int)
     */
    @Override
    public byte[] getBytes(int parameterIndex) throws SQLException {
        return original.getBytes(parameterIndex);
    }

    /**
     * @see java.sql.CallableStatement#registerOutParameter(int, int)
     */
    @Override
    public void registerOutParameter(int parameterIndex, int sqlType)
            throws SQLException {

        original.registerOutParameter(parameterIndex, sqlType);
    }

    /**
     * @see java.sql.CallableStatement#registerOutParameter(int, int, int)
     */
    @Override
    public void registerOutParameter(int parameterIndex, int sqlType, int scale)
            throws SQLException {

        original.registerOutParameter(parameterIndex, sqlType, scale);
    }

    /**
     * @see java.sql.CallableStatement#getObject(int)
     */
    @Override
    public Object getObject(int parameterIndex) throws SQLException {
        return original.getObject(parameterIndex);
    }

    /**
     * @see java.sql.CallableStatement#getString(int)
     */
    @Override
    public String getString(int parameterIndex) throws SQLException {
        return original.getString(parameterIndex);
    }

    /**
     * @see java.sql.CallableStatement#registerOutParameter(int, int,
     *      java.lang.String)
     */
    @Override
    public void registerOutParameter(int paramIndex, int sqlType,
            String typeName) throws SQLException {

        original.registerOutParameter(paramIndex, sqlType, typeName);
    }

    /**
     * @see java.sql.CallableStatement#getByte(java.lang.String)
     */
    @Override
    public byte getByte(String parameterName) throws SQLException {
        return original.getByte(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#getDouble(java.lang.String)
     */
    @Override
    public double getDouble(String parameterName) throws SQLException {
        return original.getDouble(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#getFloat(java.lang.String)
     */
    @Override
    public float getFloat(String parameterName) throws SQLException {
        return original.getFloat(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#getInt(java.lang.String)
     */
    @Override
    public int getInt(String parameterName) throws SQLException {
        return original.getInt(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#getLong(java.lang.String)
     */
    @Override
    public long getLong(String parameterName) throws SQLException {
        return original.getLong(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#getShort(java.lang.String)
     */
    @Override
    public short getShort(String parameterName) throws SQLException {
        return original.getShort(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#getBoolean(java.lang.String)
     */
    @Override
    public boolean getBoolean(String parameterName) throws SQLException {
        return original.getBoolean(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#getBytes(java.lang.String)
     */
    @Override
    public byte[] getBytes(String parameterName) throws SQLException {
        return original.getBytes(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#setByte(java.lang.String, byte)
     */
    @Override
    public void setByte(String parameterName, byte x) throws SQLException {
        original.setByte(parameterName, x);
    }

    /**
     * @see java.sql.CallableStatement#setDouble(java.lang.String, double)
     */
    @Override
    public void setDouble(String parameterName, double x) throws SQLException {
        original.setDouble(parameterName, x);
    }

    /**
     * @see java.sql.CallableStatement#setFloat(java.lang.String, float)
     */
    @Override
    public void setFloat(String parameterName, float x) throws SQLException {
        original.setFloat(parameterName, x);
    }

    /**
     * @see java.sql.CallableStatement#registerOutParameter(java.lang.String,
     *      int)
     */
    @Override
    public void registerOutParameter(String parameterName, int sqlType)
            throws SQLException {

        original.registerOutParameter(parameterName, sqlType);
    }

    /**
     * @see java.sql.CallableStatement#setInt(java.lang.String, int)
     */
    @Override
    public void setInt(String parameterName, int x) throws SQLException {
        original.setInt(parameterName, x);
    }

    /**
     * @see java.sql.CallableStatement#setNull(java.lang.String, int)
     */
    @Override
    public void setNull(String parameterName, int sqlType) throws SQLException {
        original.setNull(parameterName, sqlType);
    }

    /**
     * @see java.sql.CallableStatement#registerOutParameter(java.lang.String,
     *      int, int)
     */
    @Override
    public void registerOutParameter(String parameterName, int sqlType,
            int scale) throws SQLException {

        original.registerOutParameter(parameterName, sqlType, scale);
    }

    /**
     * @see java.sql.CallableStatement#setLong(java.lang.String, long)
     */
    @Override
    public void setLong(String parameterName, long x) throws SQLException {
        original.setLong(parameterName, x);
    }

    /**
     * @see java.sql.CallableStatement#setShort(java.lang.String, short)
     */
    @Override
    public void setShort(String parameterName, short x) throws SQLException {
        original.setShort(parameterName, x);
    }

    /**
     * @see java.sql.CallableStatement#setBoolean(java.lang.String, boolean)
     */
    @Override
    public void setBoolean(String parameterName, boolean x) throws SQLException {
        original.setBoolean(parameterName, x);
    }

    /**
     * @see java.sql.CallableStatement#setBytes(java.lang.String, byte[])
     */
    @Override
    public void setBytes(String parameterName, byte[] x) throws SQLException {
        original.setBytes(parameterName, x);
    }

    /**
     * @see java.sql.CallableStatement#getBigDecimal(int)
     */
    @Override
    public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
        return original.getBigDecimal(parameterIndex);
    }

    /**
     * @see java.sql.CallableStatement#getBigDecimal(int, int)
     * @deprecated
     */
    @Deprecated
    @Override
    public BigDecimal getBigDecimal(int parameterIndex, int scale)
            throws SQLException {

        return original.getBigDecimal(parameterIndex, scale);
    }

    /**
     * @see java.sql.CallableStatement#getURL(int)
     */
    @Override
    public URL getURL(int parameterIndex) throws SQLException {
        return original.getURL(parameterIndex);
    }

    /**
     * @see java.sql.CallableStatement#getArray(int)
     */
    @Override
    public Array getArray(int i) throws SQLException {
        return original.getArray(i);
    }

    /**
     * @see java.sql.CallableStatement#getBlob(int)
     */
    @Override
    public Blob getBlob(int i) throws SQLException {
        return original.getBlob(i);
    }

    /**
     * @see java.sql.CallableStatement#getClob(int)
     */
    @Override
    public Clob getClob(int i) throws SQLException {
        return original.getClob(i);
    }

    /**
     * @see java.sql.CallableStatement#getDate(int)
     */
    @Override
    public Date getDate(int parameterIndex) throws SQLException {
        return original.getDate(parameterIndex);
    }

    /**
     * @see java.sql.CallableStatement#getRef(int)
     */
    @Override
    public Ref getRef(int i) throws SQLException {
        return original.getRef(i);
    }

    /**
     * @see java.sql.CallableStatement#getTime(int)
     */
    @Override
    public Time getTime(int parameterIndex) throws SQLException {
        return original.getTime(parameterIndex);
    }

    /**
     * @see java.sql.CallableStatement#getTimestamp(int)
     */
    @Override
    public Timestamp getTimestamp(int parameterIndex) throws SQLException {
        return original.getTimestamp(parameterIndex);
    }

    /**
     * @see java.sql.CallableStatement#setAsciiStream(java.lang.String,
     *      java.io.InputStream, int)
     */
    @Override
    public void setAsciiStream(String parameterName, InputStream x, int length)
            throws SQLException {

        original.setAsciiStream(parameterName, x, length);
    }

    /**
     * @see java.sql.CallableStatement#setBinaryStream(java.lang.String,
     *      java.io.InputStream, int)
     */
    @Override
    public void setBinaryStream(String parameterName, InputStream x, int length)
            throws SQLException {

        original.setBinaryStream(parameterName, x, length);
    }

    /**
     * @see java.sql.CallableStatement#setCharacterStream(java.lang.String,
     *      java.io.Reader, int)
     */
    @Override
    public void setCharacterStream(String parameterName, Reader reader,
            int length) throws SQLException {

        original.setCharacterStream(parameterName, reader, length);
    }

    /**
     * @see java.sql.CallableStatement#getObject(java.lang.String)
     */
    @Override
    public Object getObject(String parameterName) throws SQLException {
        return original.getObject(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#setObject(java.lang.String,
     *      java.lang.Object)
     */
    @Override
    public void setObject(String parameterName, Object x) throws SQLException {
        original.setObject(parameterName, x);
    }

    /**
     * @see java.sql.CallableStatement#setObject(java.lang.String,
     *      java.lang.Object, int)
     */
    @Override
    public void setObject(String parameterName, Object x, int targetSqlType)
            throws SQLException {

        original.setObject(parameterName, x, targetSqlType);
    }

    /**
     * @see java.sql.CallableStatement#setObject(java.lang.String,
     *      java.lang.Object, int, int)
     */
    @Override
    public void setObject(String parameterName, Object x, int targetSqlType,
            int scale) throws SQLException {

        original.setObject(parameterName, x, targetSqlType, scale);
    }

    /**
     * @see java.sql.CallableStatement#getObject(int, java.util.Map)
     */
    @Override
    public Object getObject(int i, Map<String, Class<?>> map) throws SQLException {
        return original.getObject(i, map);
    }

    /**
     * @see java.sql.CallableStatement#getString(java.lang.String)
     */
    @Override
    public String getString(String parameterName) throws SQLException {
        return original.getString(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#registerOutParameter(java.lang.String,
     *      int, java.lang.String)
     */
    @Override
    public void registerOutParameter(String parameterName, int sqlType,
            String typeName) throws SQLException {

        original.registerOutParameter(parameterName, sqlType, typeName);
    }

    /**
     * @see java.sql.CallableStatement#setNull(java.lang.String, int,
     *      java.lang.String)
     */
    @Override
    public void setNull(String parameterName, int sqlType, String typeName)
            throws SQLException {

        original.setNull(parameterName, sqlType, typeName);
    }

    /**
     * @see java.sql.CallableStatement#setString(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public void setString(String parameterName, String x) throws SQLException {
        original.setString(parameterName, x);
    }

    /**
     * @see java.sql.CallableStatement#getBigDecimal(java.lang.String)
     */
    @Override
    public BigDecimal getBigDecimal(String parameterName) throws SQLException {
        return original.getBigDecimal(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#setBigDecimal(java.lang.String,
     *      java.math.BigDecimal)
     */
    @Override
    public void setBigDecimal(String parameterName, BigDecimal x)
            throws SQLException {

        original.setBigDecimal(parameterName, x);
    }

    /**
     * @see java.sql.CallableStatement#getURL(java.lang.String)
     */
    @Override
    public URL getURL(String parameterName) throws SQLException {
        return original.getURL(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#setURL(java.lang.String, java.net.URL)
     */
    @Override
    public void setURL(String parameterName, URL val) throws SQLException {
        original.setURL(parameterName, val);
    }

    /**
     * @see java.sql.CallableStatement#getArray(java.lang.String)
     */
    @Override
    public Array getArray(String parameterName) throws SQLException {
        return original.getArray(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#getBlob(java.lang.String)
     */
    @Override
    public Blob getBlob(String parameterName) throws SQLException {
        return original.getBlob(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#getClob(java.lang.String)
     */
    @Override
    public Clob getClob(String parameterName) throws SQLException {
        return original.getClob(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#getDate(java.lang.String)
     */
    @Override
    public Date getDate(String parameterName) throws SQLException {
        return original.getDate(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#setDate(java.lang.String, java.sql.Date)
     */
    @Override
    public void setDate(String parameterName, Date x) throws SQLException {
        original.setDate(parameterName, x);
    }

    /**
     * @see java.sql.CallableStatement#getDate(int, java.util.Calendar)
     */
    @Override
    public Date getDate(int parameterIndex, Calendar cal) throws SQLException {
        return original.getDate(parameterIndex, cal);
    }

    /**
     * @see java.sql.CallableStatement#getRef(java.lang.String)
     */
    @Override
    public Ref getRef(String parameterName) throws SQLException {
        return original.getRef(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#getTime(java.lang.String)
     */
    @Override
    public Time getTime(String parameterName) throws SQLException {
        return original.getTime(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#setTime(java.lang.String, java.sql.Time)
     */
    @Override
    public void setTime(String parameterName, Time x) throws SQLException {
        original.setTime(parameterName, x);
    }

    /**
     * @see java.sql.CallableStatement#getTime(int, java.util.Calendar)
     */
    @Override
    public Time getTime(int parameterIndex, Calendar cal) throws SQLException {
        return original.getTime(parameterIndex, cal);
    }

    /**
     * @see java.sql.CallableStatement#getTimestamp(java.lang.String)
     */
    @Override
    public Timestamp getTimestamp(String parameterName) throws SQLException {
        return original.getTimestamp(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#setTimestamp(java.lang.String,
     *      java.sql.Timestamp)
     */
    @Override
    public void setTimestamp(String parameterName, Timestamp x)
            throws SQLException {

        original.setTimestamp(parameterName, x);
    }

    /**
     * @see java.sql.CallableStatement#getTimestamp(int, java.util.Calendar)
     */
    @Override
    public Timestamp getTimestamp(int parameterIndex, Calendar cal)
            throws SQLException {

        return original.getTimestamp(parameterIndex, cal);
    }

    /**
     * @see java.sql.CallableStatement#getObject(java.lang.String,
     *      java.util.Map)
     */
    @Override
    public Object getObject(String parameterName, Map<String, Class<?>> map) throws SQLException {
        return original.getObject(parameterName, map);
    }

    /**
     * @see java.sql.CallableStatement#getDate(java.lang.String,
     *      java.util.Calendar)
     */
    @Override
    public Date getDate(String parameterName, Calendar cal) throws SQLException {
        return original.getDate(parameterName, cal);
    }

    /**
     * @see java.sql.CallableStatement#getTime(java.lang.String,
     *      java.util.Calendar)
     */
    @Override
    public Time getTime(String parameterName, Calendar cal) throws SQLException {
        return original.getTime(parameterName, cal);
    }

    /**
     * @see java.sql.CallableStatement#getTimestamp(java.lang.String,
     *      java.util.Calendar)
     */
    @Override
    public Timestamp getTimestamp(String parameterName, Calendar cal)
            throws SQLException {

        return original.getTimestamp(parameterName, cal);
    }

    /**
     * @see java.sql.CallableStatement#setDate(java.lang.String, java.sql.Date,
     *      java.util.Calendar)
     */
    @Override
    public void setDate(String parameterName, Date x, Calendar cal)
            throws SQLException {

        original.setDate(parameterName, x, cal);
    }

    /**
     * @see java.sql.CallableStatement#setTime(java.lang.String, java.sql.Time,
     *      java.util.Calendar)
     */
    @Override
    public void setTime(String parameterName, Time x, Calendar cal)
            throws SQLException {

        original.setTime(parameterName, x, cal);
    }

    /**
     * @see java.sql.CallableStatement#setTimestamp(java.lang.String,
     *      java.sql.Timestamp, java.util.Calendar)
     */
    @Override
    public void setTimestamp(String parameterName, Timestamp x, Calendar cal)
            throws SQLException {

        original.setTimestamp(parameterName, x, cal);
    }

    /**
     * @see java.sql.PreparedStatement#executeUpdate()
     */
    @Override
    public int executeUpdate() throws SQLException {
        return original.executeUpdate();
    }

    /**
     * @see java.sql.PreparedStatement#addBatch()
     */
    @Override
    public void addBatch() throws SQLException {
        original.addBatch();
    }

    /**
     * @see java.sql.PreparedStatement#clearParameters()
     */
    @Override
    public void clearParameters() throws SQLException {
        original.clearParameters();
    }

    /**
     * @see java.sql.PreparedStatement#execute()
     */
    @Override
    public boolean execute() throws SQLException {
        return original.execute();
    }

    /**
     * @see java.sql.PreparedStatement#setByte(int, byte)
     */
    @Override
    public void setByte(int parameterIndex, byte x) throws SQLException {
        original.setByte(parameterIndex, x);
    }

    /**
     * @see java.sql.PreparedStatement#setDouble(int, double)
     */
    @Override
    public void setDouble(int parameterIndex, double x) throws SQLException {
        original.setDouble(parameterIndex, x);
    }

    /**
     * @see java.sql.PreparedStatement#setFloat(int, float)
     */
    @Override
    public void setFloat(int parameterIndex, float x) throws SQLException {
        original.setFloat(parameterIndex, x);
    }

    /**
     * @see java.sql.PreparedStatement#setInt(int, int)
     */
    @Override
    public void setInt(int parameterIndex, int x) throws SQLException {
        original.setInt(parameterIndex, x);
    }

    /**
     * @see java.sql.PreparedStatement#setNull(int, int)
     */
    @Override
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        original.setNull(parameterIndex, sqlType);
    }

    /**
     * @see java.sql.PreparedStatement#setLong(int, long)
     */
    @Override
    public void setLong(int parameterIndex, long x) throws SQLException {
        original.setLong(parameterIndex, x);
    }

    /**
     * @see java.sql.PreparedStatement#setShort(int, short)
     */
    @Override
    public void setShort(int parameterIndex, short x) throws SQLException {
        original.setShort(parameterIndex, x);
    }

    /**
     * @see java.sql.PreparedStatement#setBoolean(int, boolean)
     */
    @Override
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        original.setBoolean(parameterIndex, x);
    }

    /**
     * @see java.sql.PreparedStatement#setBytes(int, byte[])
     */
    @Override
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        original.setBytes(parameterIndex, x);
    }

    /**
     * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream,
     *      int)
     */
    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length)
            throws SQLException {

        original.setAsciiStream(parameterIndex, x, length);
    }

    /**
     * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream,
     *      int)
     */
    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length)
            throws SQLException {

        original.setBinaryStream(parameterIndex, x, length);
    }

    /**
     * @see java.sql.PreparedStatement#setUnicodeStream(int,
     *      java.io.InputStream, int)
     * @deprecated
     */
    @Deprecated
    @Override
    public void setUnicodeStream(int parameterIndex, InputStream x, int length)
            throws SQLException {

        original.setUnicodeStream(parameterIndex, x, length);
    }

    /**
     * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader,
     *      int)
     */
    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, int length)
            throws SQLException {

        original.setCharacterStream(parameterIndex, reader, length);
    }

    /**
     * @see java.sql.PreparedStatement#setObject(int, java.lang.Object)
     */
    @Override
    public void setObject(int parameterIndex, Object x) throws SQLException {
        original.setObject(parameterIndex, x);
    }

    /**
     * @see java.sql.PreparedStatement#setObject(int, java.lang.Object, int)
     */
    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType)
            throws SQLException {

        original.setObject(parameterIndex, x, targetSqlType);
    }

    /**
     * @see java.sql.PreparedStatement#setObject(int, java.lang.Object, int,
     *      int)
     */
    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType,
            int scale) throws SQLException {

        original.setObject(parameterIndex, x, targetSqlType, scale);
    }

    /**
     * @see java.sql.PreparedStatement#setNull(int, int, java.lang.String)
     */
    @Override
    public void setNull(int paramIndex, int sqlType, String typeName)
            throws SQLException {

        original.setNull(paramIndex, sqlType, typeName);
    }

    /**
     * @see java.sql.PreparedStatement#setString(int, java.lang.String)
     */
    @Override
    public void setString(int parameterIndex, String x) throws SQLException {
        original.setString(parameterIndex, x);
    }

    /**
     * @see java.sql.PreparedStatement#setBigDecimal(int, java.math.BigDecimal)
     */
    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x)
            throws SQLException {

        original.setBigDecimal(parameterIndex, x);
    }

    /**
     * @see java.sql.PreparedStatement#setURL(int, java.net.URL)
     */
    @Override
    public void setURL(int parameterIndex, URL x) throws SQLException {
        original.setURL(parameterIndex, x);
    }

    /**
     * @see java.sql.PreparedStatement#setArray(int, java.sql.Array)
     */
    @Override
    public void setArray(int i, Array x) throws SQLException {
        original.setArray(i, x);
    }

    /**
     * @see java.sql.PreparedStatement#setBlob(int, java.sql.Blob)
     */
    @Override
    public void setBlob(int i, Blob x) throws SQLException {
        original.setBlob(i, x);
    }

    /**
     * @see java.sql.PreparedStatement#setClob(int, java.sql.Clob)
     */
    @Override
    public void setClob(int i, Clob x) throws SQLException {
        original.setClob(i, x);
    }

    /**
     * @see java.sql.PreparedStatement#setDate(int, java.sql.Date)
     */
    @Override
    public void setDate(int parameterIndex, Date x) throws SQLException {
        original.setDate(parameterIndex, x);
    }

    /**
     * @see java.sql.PreparedStatement#getParameterMetaData()
     */
    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        return original.getParameterMetaData();
    }

    /**
     * @see java.sql.PreparedStatement#setRef(int, java.sql.Ref)
     */
    @Override
    public void setRef(int i, Ref x) throws SQLException {
        original.setRef(i, x);
    }

    /**
     * @see java.sql.PreparedStatement#executeQuery()
     */
    @Override
    public ResultSet executeQuery() throws SQLException {
        return original.executeQuery();
    }

    /**
     * @see java.sql.PreparedStatement#getMetaData()
     */
    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return original.getMetaData();
    }

    /**
     * @see java.sql.PreparedStatement#setTime(int, java.sql.Time)
     */
    @Override
    public void setTime(int parameterIndex, Time x) throws SQLException {
        original.setTime(parameterIndex, x);
    }

    /**
     * @see java.sql.PreparedStatement#setTimestamp(int, java.sql.Timestamp)
     */
    @Override
    public void setTimestamp(int parameterIndex, Timestamp x)
            throws SQLException {

        original.setTimestamp(parameterIndex, x);
    }

    /**
     * @see java.sql.PreparedStatement#setDate(int, java.sql.Date,
     *      java.util.Calendar)
     */
    @Override
    public void setDate(int parameterIndex, Date x, Calendar cal)
            throws SQLException {

        original.setDate(parameterIndex, x, cal);
    }

    /**
     * @see java.sql.PreparedStatement#setTime(int, java.sql.Time,
     *      java.util.Calendar)
     */
    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal)
            throws SQLException {

        original.setTime(parameterIndex, x, cal);
    }

    /**
     * @see java.sql.PreparedStatement#setTimestamp(int, java.sql.Timestamp,
     *      java.util.Calendar)
     */
    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal)
            throws SQLException {

        original.setTimestamp(parameterIndex, x, cal);
    }

    /**
     * @see java.sql.Statement#getFetchDirection()
     */
    @Override
    public int getFetchDirection() throws SQLException {
        return original.getFetchDirection();
    }

    /**
     * @see java.sql.Statement#getFetchSize()
     */
    @Override
    public int getFetchSize() throws SQLException {
        return original.getFetchSize();
    }

    /**
     * @see java.sql.Statement#getMaxFieldSize()
     */
    @Override
    public int getMaxFieldSize() throws SQLException {
        return original.getMaxFieldSize();
    }

    /**
     * @see java.sql.Statement#getMaxRows()
     */
    @Override
    public int getMaxRows() throws SQLException {
        return original.getMaxRows();
    }

    /**
     * @see java.sql.Statement#getQueryTimeout()
     */
    @Override
    public int getQueryTimeout() throws SQLException {
        return original.getQueryTimeout();
    }

    /**
     * @see java.sql.Statement#getResultSetConcurrency()
     */
    @Override
    public int getResultSetConcurrency() throws SQLException {
        return original.getResultSetConcurrency();
    }

    /**
     * @see java.sql.Statement#getResultSetHoldability()
     */
    @Override
    public int getResultSetHoldability() throws SQLException {
        return original.getResultSetHoldability();
    }

    /**
     * @see java.sql.Statement#getResultSetType()
     */
    @Override
    public int getResultSetType() throws SQLException {
        return original.getResultSetType();
    }

    /**
     * @see java.sql.Statement#getUpdateCount()
     */
    @Override
    public int getUpdateCount() throws SQLException {
        return original.getUpdateCount();
    }

    /**
     * @see java.sql.Statement#cancel()
     */
    @Override
    public void cancel() throws SQLException {
        original.cancel();
    }

    /**
     * @see java.sql.Statement#clearBatch()
     */
    @Override
    public void clearBatch() throws SQLException {
        original.clearBatch();
    }

    /**
     * @see java.sql.Statement#clearWarnings()
     */
    @Override
    public void clearWarnings() throws SQLException {
        original.clearWarnings();
    }

    /**
     * @see java.sql.Statement#close()
     */
    @Override
    public void close() throws SQLException {
        original.close();
    }

    /**
     * @see java.sql.Statement#getMoreResults()
     */
    @Override
    public boolean getMoreResults() throws SQLException {
        return original.getMoreResults();
    }

    /**
     * @see java.sql.Statement#executeBatch()
     */
    @Override
    public int[] executeBatch() throws SQLException {
        return original.executeBatch();
    }

    /**
     * @see java.sql.Statement#setFetchDirection(int)
     */
    @Override
    public void setFetchDirection(int direction) throws SQLException {
        original.setFetchDirection(direction);
    }

    /**
     * @see java.sql.Statement#setFetchSize(int)
     */
    @Override
    public void setFetchSize(int rows) throws SQLException {
        original.setFetchSize(rows);
    }

    /**
     * @see java.sql.Statement#setMaxFieldSize(int)
     */
    @Override
    public void setMaxFieldSize(int max) throws SQLException {
        original.setMaxFieldSize(max);
    }

    /**
     * @see java.sql.Statement#setMaxRows(int)
     */
    @Override
    public void setMaxRows(int max) throws SQLException {
        original.setMaxRows(max);
    }

    /**
     * @see java.sql.Statement#setQueryTimeout(int)
     */
    @Override
    public void setQueryTimeout(int seconds) throws SQLException {
        original.setQueryTimeout(seconds);
    }

    /**
     * @see java.sql.Statement#getMoreResults(int)
     */
    @Override
    public boolean getMoreResults(int current) throws SQLException {
        return original.getMoreResults(current);
    }

    /**
     * @see java.sql.Statement#setEscapeProcessing(boolean)
     */
    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException {
        original.setEscapeProcessing(enable);
    }

    /**
     * @see java.sql.Statement#executeUpdate(java.lang.String)
     */
    @Override
    public int executeUpdate(String sql) throws SQLException {
        return original.executeUpdate(sql);
    }

    /**
     * @see java.sql.Statement#addBatch(java.lang.String)
     */
    @Override
    public void addBatch(String sql) throws SQLException {
        original.addBatch(sql);
    }

    /**
     * @see java.sql.Statement#setCursorName(java.lang.String)
     */
    @Override
    public void setCursorName(String name) throws SQLException {
        original.setCursorName(name);
    }

    /**
     * @see java.sql.Statement#execute(java.lang.String)
     */
    @Override
    public boolean execute(String sql) throws SQLException {
        return original.execute(sql);
    }

    /**
     * @see java.sql.Statement#executeUpdate(java.lang.String, int)
     */
    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys)
            throws SQLException {

        return original.executeUpdate(sql, autoGeneratedKeys);
    }

    /**
     * @see java.sql.Statement#execute(java.lang.String, int)
     */
    @Override
    public boolean execute(String sql, int autoGeneratedKeys)
            throws SQLException {

        return original.execute(sql, autoGeneratedKeys);
    }

    /**
     * @see java.sql.Statement#executeUpdate(java.lang.String, int[])
     */
    @Override
    public int executeUpdate(String sql, int[] columnIndexes)
            throws SQLException {

        return original.executeUpdate(sql, columnIndexes);
    }

    /**
     * @see java.sql.Statement#execute(java.lang.String, int[])
     */
    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        return original.execute(sql, columnIndexes);
    }

    /**
     * @see java.sql.Statement#getConnection()
     */
    @Override
    public Connection getConnection() throws SQLException {
        return original.getConnection();
    }

    /**
     * @see java.sql.Statement#getGeneratedKeys()
     */
    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        return original.getGeneratedKeys();
    }

    /**
     * @see java.sql.Statement#getResultSet()
     */
    @Override
    public ResultSet getResultSet() throws SQLException {
        return original.getResultSet();
    }

    /**
     * @see java.sql.Statement#getWarnings()
     */
    @Override
    public SQLWarning getWarnings() throws SQLException {
        return original.getWarnings();
    }

    /**
     * @see java.sql.Statement#executeUpdate(java.lang.String,
     *      java.lang.String[])
     */
    @Override
    public int executeUpdate(String sql, String[] columnNames)
            throws SQLException {

        return original.executeUpdate(sql, columnNames);
    }

    /**
     * @see java.sql.Statement#execute(java.lang.String, java.lang.String[])
     */
    @Override
    public boolean execute(String sql, String[] columnNames)
            throws SQLException {

        return original.execute(sql, columnNames);
    }

    /**
     * @see java.sql.Statement#executeQuery(java.lang.String)
     */
    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        return original.executeQuery(sql);
    }

    @Override
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
	original.setRowId(parameterIndex, x);;
    }

    @Override
    public void setNString(int parameterIndex, String value)
	    throws SQLException {
	original.setNString(parameterIndex, value);
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value,
	    long length) throws SQLException {
	original.setNCharacterStream(parameterIndex, value, length);
    }

    @Override
    public void setNClob(int parameterIndex, NClob value) throws SQLException {
	original.setNClob(parameterIndex, value);
    }

    @Override
    public void setClob(int parameterIndex, Reader reader, long length)
	    throws SQLException {
	original.setClob(parameterIndex, reader, length);
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream,
	    long length) throws SQLException {
	original.setBlob(parameterIndex, inputStream, length);
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader, long length)
	    throws SQLException {
	original.setNClob(parameterIndex, reader, length);
    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject)
	    throws SQLException {
	original.setSQLXML(parameterIndex, xmlObject);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length)
	    throws SQLException {
	original.setAsciiStream(parameterIndex, x, length);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length)
	    throws SQLException {
	original.setBinaryStream(parameterIndex, x, length);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader,
	    long length) throws SQLException {
	original.setCharacterStream(parameterIndex, reader, length);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x)
	    throws SQLException {
	original.setAsciiStream(parameterIndex, x);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x)
	    throws SQLException {
	original.setBinaryStream(parameterIndex, x);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader)
	    throws SQLException {
	original.setCharacterStream(parameterIndex, reader);
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value)
	    throws SQLException {
	original.setNCharacterStream(parameterIndex, value);
    }

    @Override
    public void setClob(int parameterIndex, Reader reader) throws SQLException {
	original.setClob(parameterIndex, reader);
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream)
	    throws SQLException {
	original.setBlob(parameterIndex, inputStream);
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader)
	    throws SQLException {
	original.setNClob(parameterIndex, reader);
    }

    @Override
    public boolean isClosed() throws SQLException {
	return original.isClosed();
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {
	original.setPoolable(poolable);
    }

    @Override
    public boolean isPoolable() throws SQLException {
	return original.isPoolable();
    }

    @Override
    public void closeOnCompletion() throws SQLException {
	original.closeOnCompletion();
    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
	return original.isCloseOnCompletion();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
	return original.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
	return original.isWrapperFor(iface);
    }

    @Override
    public RowId getRowId(int parameterIndex) throws SQLException {
	return original.getRowId(parameterIndex);
    }

    @Override
    public RowId getRowId(String parameterName) throws SQLException {
	return original.getRowId(parameterName);
    }

    @Override
    public void setRowId(String parameterName, RowId x) throws SQLException {
	original.setRowId(parameterName, x);
    }

    @Override
    public void setNString(String parameterName, String value)
	    throws SQLException {
	original.setNString(parameterName, value);
    }

    @Override
    public void setNCharacterStream(String parameterName, Reader value,
	    long length) throws SQLException {
	original.setNCharacterStream(parameterName, value, length);
    }

    @Override
    public void setNClob(String parameterName, NClob value)
	    throws SQLException {
	original.setNClob(parameterName, value);
    }

    @Override
    public void setClob(String parameterName, Reader reader, long length)
	    throws SQLException {
	original.setClob(parameterName, reader, length);
    }

    @Override
    public void setBlob(String parameterName, InputStream inputStream,
	    long length) throws SQLException {
	original.setBlob(parameterName, inputStream, length);
    }

    @Override
    public void setNClob(String parameterName, Reader reader, long length)
	    throws SQLException {
	original.setNClob(parameterName, reader, length);
    }

    @Override
    public NClob getNClob(int parameterIndex) throws SQLException {
	return original.getNClob(parameterIndex);
    }

    @Override
    public NClob getNClob(String parameterName) throws SQLException {
	return original.getNClob(parameterName);
    }

    @Override
    public void setSQLXML(String parameterName, SQLXML xmlObject)
	    throws SQLException {
	original.setSQLXML(parameterName, xmlObject);
    }

    @Override
    public SQLXML getSQLXML(int parameterIndex) throws SQLException {
	return original.getSQLXML(parameterIndex);
    }

    @Override
    public SQLXML getSQLXML(String parameterName) throws SQLException {
	return original.getSQLXML(parameterName);
    }

    @Override
    public String getNString(int parameterIndex) throws SQLException {
	return original.getNString(parameterIndex);
    }

    @Override
    public String getNString(String parameterName) throws SQLException {
	return original.getNString(parameterName);
    }

    @Override
    public Reader getNCharacterStream(int parameterIndex) throws SQLException {
	return original.getNCharacterStream(parameterIndex);
    }

    @Override
    public Reader getNCharacterStream(String parameterName)
	    throws SQLException {
	return original.getNCharacterStream(parameterName);
    }

    @Override
    public Reader getCharacterStream(int parameterIndex) throws SQLException {
	return original.getCharacterStream(parameterIndex);
    }

    @Override
    public Reader getCharacterStream(String parameterName) throws SQLException {
	return original.getCharacterStream(parameterName);
    }

    @Override
    public void setBlob(String parameterName, Blob x) throws SQLException {
	original.setBlob(parameterName, x);
    }

    @Override
    public void setClob(String parameterName, Clob x) throws SQLException {
	original.setClob(parameterName, x);
    }

    @Override
    public void setAsciiStream(String parameterName, InputStream x, long length)
	    throws SQLException {
	original.setAsciiStream(parameterName, x, length);
    }

    @Override
    public void setBinaryStream(String parameterName, InputStream x,
	    long length) throws SQLException {
	original.setBinaryStream(parameterName, x, length);
    }

    @Override
    public void setCharacterStream(String parameterName, Reader reader,
	    long length) throws SQLException {
	original.setCharacterStream(parameterName, reader, length);
    }

    @Override
    public void setAsciiStream(String parameterName, InputStream x)
	    throws SQLException {
	original.setAsciiStream(parameterName, x);
    }

    @Override
    public void setBinaryStream(String parameterName, InputStream x)
	    throws SQLException {
	original.setBinaryStream(parameterName, x);
    }

    @Override
    public void setCharacterStream(String parameterName, Reader reader)
	    throws SQLException {
	original.setCharacterStream(parameterName, reader);
    }

    @Override
    public void setNCharacterStream(String parameterName, Reader value)
	    throws SQLException {
	original.setNCharacterStream(parameterName, value);
    }

    @Override
    public void setClob(String parameterName, Reader reader)
	    throws SQLException {
	original.setClob(parameterName, reader);
    }

    @Override
    public void setBlob(String parameterName, InputStream inputStream)
	    throws SQLException {
	original.setBlob(parameterName, inputStream);
    }

    @Override
    public void setNClob(String parameterName, Reader reader)
	    throws SQLException {
	original.setNClob(parameterName, reader);
    }

    @Override
    public <T> T getObject(int parameterIndex, Class<T> type)
	    throws SQLException {
	return original.getObject(parameterIndex, type);
    }

    @Override
    public <T> T getObject(String parameterName, Class<T> type)
	    throws SQLException {
	return original.getObject(parameterName, type);
    }
}