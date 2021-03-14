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
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

/**
 * {@link CallableStatement}用のモッククラスです。
 * 
 * @author higa
 * 
 */
public class MockCallableStatement extends MockPreparedStatement implements
        CallableStatement {

    /**
     * {@link MockCallableStatement}を作成します。
     * 
     * @param connection
     *            コネクション
     * @param sql
     *            SQL
     */
    public MockCallableStatement(MockConnection connection, String sql) {
        super(connection, sql);
    }

    /**
     * {@link MockCallableStatement}を作成します。
     * 
     * @param connection
     *            コネクション
     * @param sql
     *            SQL
     * @param resultSetType
     *            結果セットタイプ
     * @param resultSetConcurrency
     *            結果セット同時並行性
     */
    public MockCallableStatement(MockConnection connection, String sql,
            int resultSetType, int resultSetConcurrency) {
        super(connection, sql, resultSetType, resultSetConcurrency);
    }

    /**
     * {@link MockCallableStatement}を作成します。
     * 
     * @param connection
     *            コネクション
     * @param sql
     *            SQL
     * @param resultSetType
     *            結果セットタイプ
     * @param resultSetConcurrency
     *            結果セット同時並行性
     * @param resultSetHoldability
     *            結果セット保持力
     */
    public MockCallableStatement(MockConnection connection, String sql,
            int resultSetType, int resultSetConcurrency,
            int resultSetHoldability) {
        super(connection, sql, resultSetType, resultSetConcurrency,
                resultSetHoldability);
    }

    public Array getArray(int i) throws SQLException {
        return null;
    }

    public Array getArray(String parameterName) throws SQLException {
        return null;
    }

    public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
        return null;
    }

    public BigDecimal getBigDecimal(String parameterName) throws SQLException {
        return null;
    }

    public BigDecimal getBigDecimal(int parameterIndex, int scale)
            throws SQLException {
        return null;
    }

    public Blob getBlob(int i) throws SQLException {
        return null;
    }

    public Blob getBlob(String parameterName) throws SQLException {
        return null;
    }

    public boolean getBoolean(int parameterIndex) throws SQLException {
        return false;
    }

    public boolean getBoolean(String parameterName) throws SQLException {
        return false;
    }

    public byte getByte(int parameterIndex) throws SQLException {
        return 0;
    }

    public byte getByte(String parameterName) throws SQLException {
        return 0;
    }

    public byte[] getBytes(int parameterIndex) throws SQLException {
        return null;
    }

    public byte[] getBytes(String parameterName) throws SQLException {
        return null;
    }

    public Clob getClob(int i) throws SQLException {
        return null;
    }

    public Clob getClob(String parameterName) throws SQLException {
        return null;
    }

    public Date getDate(int parameterIndex) throws SQLException {
        return null;
    }

    public Date getDate(String parameterName) throws SQLException {
        return null;
    }

    public Date getDate(int parameterIndex, Calendar cal) throws SQLException {
        return null;
    }

    public Date getDate(String parameterName, Calendar cal) throws SQLException {
        return null;
    }

    public double getDouble(int parameterIndex) throws SQLException {
        return 0;
    }

    public double getDouble(String parameterName) throws SQLException {
        return 0;
    }

    public float getFloat(int parameterIndex) throws SQLException {
        return 0;
    }

    public float getFloat(String parameterName) throws SQLException {
        return 0;
    }

    public int getInt(int parameterIndex) throws SQLException {
        return 0;
    }

    public int getInt(String parameterName) throws SQLException {
        return 0;
    }

    public long getLong(int parameterIndex) throws SQLException {
        return 0;
    }

    public long getLong(String parameterName) throws SQLException {
        return 0;
    }

    public Object getObject(int parameterIndex) throws SQLException {
        return null;
    }

    public Object getObject(String parameterName) throws SQLException {
        return null;
    }

    public Object getObject(int arg0, Map arg1) throws SQLException {
        return null;
    }

    public Object getObject(String arg0, Map arg1) throws SQLException {
        return null;
    }

    public Ref getRef(int i) throws SQLException {
        return null;
    }

    public Ref getRef(String parameterName) throws SQLException {
        return null;
    }

    public short getShort(int parameterIndex) throws SQLException {
        return 0;
    }

    public short getShort(String parameterName) throws SQLException {
        return 0;
    }

    public String getString(int parameterIndex) throws SQLException {
        return null;
    }

    public String getString(String parameterName) throws SQLException {
        return null;
    }

    public Time getTime(int parameterIndex) throws SQLException {
        return null;
    }

    public Time getTime(String parameterName) throws SQLException {
        return null;
    }

    public Time getTime(int parameterIndex, Calendar cal) throws SQLException {
        return null;
    }

    public Time getTime(String parameterName, Calendar cal) throws SQLException {
        return null;
    }

    public Timestamp getTimestamp(int parameterIndex) throws SQLException {
        return null;
    }

    public Timestamp getTimestamp(String parameterName) throws SQLException {
        return null;
    }

    public Timestamp getTimestamp(int parameterIndex, Calendar cal)
            throws SQLException {
        return null;
    }

    public Timestamp getTimestamp(String parameterName, Calendar cal)
            throws SQLException {
        return null;
    }

    public URL getURL(int parameterIndex) throws SQLException {
        return null;
    }

    public URL getURL(String parameterName) throws SQLException {
        return null;
    }

    public void registerOutParameter(int parameterIndex, int sqlType)
            throws SQLException {
    }

    public void registerOutParameter(String parameterName, int sqlType)
            throws SQLException {
    }

    public void registerOutParameter(int parameterIndex, int sqlType, int scale)
            throws SQLException {
    }

    public void registerOutParameter(int paramIndex, int sqlType,
            String typeName) throws SQLException {
    }

    public void registerOutParameter(String parameterName, int sqlType,
            int scale) throws SQLException {
    }

    public void registerOutParameter(String parameterName, int sqlType,
            String typeName) throws SQLException {
    }

    public void setAsciiStream(String parameterName, InputStream x, int length)
            throws SQLException {
    }

    public void setBigDecimal(String parameterName, BigDecimal x)
            throws SQLException {
    }

    public void setBinaryStream(String parameterName, InputStream x, int length)
            throws SQLException {
    }

    public void setBoolean(String parameterName, boolean x) throws SQLException {
    }

    public void setByte(String parameterName, byte x) throws SQLException {
    }

    public void setBytes(String parameterName, byte[] x) throws SQLException {
    }

    public void setCharacterStream(String parameterName, Reader reader,
            int length) throws SQLException {
    }

    public void setDate(String parameterName, Date x) throws SQLException {
    }

    public void setDate(String parameterName, Date x, Calendar cal)
            throws SQLException {
    }

    public void setDouble(String parameterName, double x) throws SQLException {
    }

    public void setFloat(String parameterName, float x) throws SQLException {
    }

    public void setInt(String parameterName, int x) throws SQLException {
    }

    public void setLong(String parameterName, long x) throws SQLException {
    }

    public void setNull(String parameterName, int sqlType) throws SQLException {
    }

    public void setNull(String parameterName, int sqlType, String typeName)
            throws SQLException {
    }

    public void setObject(String parameterName, Object x) throws SQLException {
    }

    public void setObject(String parameterName, Object x, int targetSqlType)
            throws SQLException {
    }

    public void setObject(String parameterName, Object x, int targetSqlType,
            int scale) throws SQLException {
    }

    public void setShort(String parameterName, short x) throws SQLException {
    }

    public void setString(String parameterName, String x) throws SQLException {
    }

    public void setTime(String parameterName, Time x) throws SQLException {
    }

    public void setTime(String parameterName, Time x, Calendar cal)
            throws SQLException {
    }

    public void setTimestamp(String parameterName, Timestamp x)
            throws SQLException {
    }

    public void setTimestamp(String parameterName, Timestamp x, Calendar cal)
            throws SQLException {
    }

    public void setURL(String parameterName, URL val) throws SQLException {
    }

    public boolean wasNull() throws SQLException {
        return false;
    }

    public void setRowId(int parameterIndex, RowId x) throws SQLException {
	// TODO Auto-generated method stub
	
    }

    public void setNString(int parameterIndex, String value)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    public void setNCharacterStream(int parameterIndex, Reader value,
	    long length) throws SQLException {
	// TODO Auto-generated method stub
	
    }

    public void setNClob(int parameterIndex, NClob value) throws SQLException {
	// TODO Auto-generated method stub
	
    }

    public void setClob(int parameterIndex, Reader reader, long length)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    public void setBlob(int parameterIndex, InputStream inputStream,
	    long length) throws SQLException {
	// TODO Auto-generated method stub
	
    }

    public void setNClob(int parameterIndex, Reader reader, long length)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    public void setSQLXML(int parameterIndex, SQLXML xmlObject)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    public void setAsciiStream(int parameterIndex, InputStream x, long length)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    public void setBinaryStream(int parameterIndex, InputStream x, long length)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    public void setCharacterStream(int parameterIndex, Reader reader,
	    long length) throws SQLException {
	// TODO Auto-generated method stub
	
    }

    public void setAsciiStream(int parameterIndex, InputStream x)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    public void setBinaryStream(int parameterIndex, InputStream x)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    public void setCharacterStream(int parameterIndex, Reader reader)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    public void setNCharacterStream(int parameterIndex, Reader value)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    public void setClob(int parameterIndex, Reader reader) throws SQLException {
	// TODO Auto-generated method stub
	
    }

    public void setBlob(int parameterIndex, InputStream inputStream)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    public void setNClob(int parameterIndex, Reader reader)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    public void setPoolable(boolean poolable) throws SQLException {
	// TODO Auto-generated method stub
	
    }

    public boolean isPoolable() throws SQLException {
	// TODO Auto-generated method stub
	return false;
    }

    public void closeOnCompletion() throws SQLException {
	// TODO Auto-generated method stub
	
    }

    public boolean isCloseOnCompletion() throws SQLException {
	// TODO Auto-generated method stub
	return false;
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
	// TODO Auto-generated method stub
	return false;
    }

    public RowId getRowId(int parameterIndex) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    public RowId getRowId(String parameterName) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    public void setRowId(String parameterName, RowId x) throws SQLException {
	// TODO Auto-generated method stub
	
    }

    public void setNString(String parameterName, String value)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    public void setNCharacterStream(String parameterName, Reader value,
	    long length) throws SQLException {
	// TODO Auto-generated method stub
	
    }

    public void setNClob(String parameterName, NClob value)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    public void setClob(String parameterName, Reader reader, long length)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    public void setBlob(String parameterName, InputStream inputStream,
	    long length) throws SQLException {
	// TODO Auto-generated method stub
	
    }

    public void setNClob(String parameterName, Reader reader, long length)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    public NClob getNClob(int parameterIndex) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    public NClob getNClob(String parameterName) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    public void setSQLXML(String parameterName, SQLXML xmlObject)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    public SQLXML getSQLXML(int parameterIndex) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    public SQLXML getSQLXML(String parameterName) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    public String getNString(int parameterIndex) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    public String getNString(String parameterName) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    public Reader getNCharacterStream(int parameterIndex) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    public Reader getNCharacterStream(String parameterName)
	    throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    public Reader getCharacterStream(int parameterIndex) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    public Reader getCharacterStream(String parameterName) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    public void setBlob(String parameterName, Blob x) throws SQLException {
	// TODO Auto-generated method stub
	
    }

    public void setClob(String parameterName, Clob x) throws SQLException {
	// TODO Auto-generated method stub
	
    }

    public void setAsciiStream(String parameterName, InputStream x, long length)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    public void setBinaryStream(String parameterName, InputStream x,
	    long length) throws SQLException {
	// TODO Auto-generated method stub
	
    }

    public void setCharacterStream(String parameterName, Reader reader,
	    long length) throws SQLException {
	// TODO Auto-generated method stub
	
    }

    public void setAsciiStream(String parameterName, InputStream x)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    public void setBinaryStream(String parameterName, InputStream x)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    public void setCharacterStream(String parameterName, Reader reader)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    public void setNCharacterStream(String parameterName, Reader value)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    public void setClob(String parameterName, Reader reader)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    public void setBlob(String parameterName, InputStream inputStream)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    public void setNClob(String parameterName, Reader reader)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    public <T> T getObject(int parameterIndex, Class<T> type)
	    throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    public <T> T getObject(String parameterName, Class<T> type)
	    throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }
}