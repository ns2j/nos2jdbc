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

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * {@link Connection}用のモッククラスです。
 * 
 * @author higa
 * 
 */
public class MockConnection implements Connection {

    private boolean closed = false;

    private boolean committed = false;

    private boolean rolledback = false;

    private boolean autoCommit = true;

    @Override
    public void clearWarnings() throws SQLException {
    }

    @Override
    public void close() throws SQLException {
        closed = true;
    }

    @Override
    public void commit() throws SQLException {
        committed = true;
    }

    /**
     * コミットしているかどうかを返します。
     * 
     * @return コミットしているかどうか
     */
    public boolean isCommitted() {
        return committed;
    }

    /**
     * コミットしているかどうかを設定します。
     * 
     * @param committed
     *            コミットしているかどうか
     */
    public void setCommitted(boolean committed) {
        this.committed = committed;
    }

    @Override
    public Statement createStatement() throws SQLException {
        return createMockStatement();
    }

    /**
     * モック用のステートメントを作成します。
     * 
     * @return モック用のステートメント
     */
    public MockStatement createMockStatement() {
        return new MockStatement(this);
    }

    @Override
    public Statement createStatement(int resultSetType,
            int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
        return createMockStatement(resultSetType, resultSetConcurrency,
                resultSetHoldability);
    }

    /**
     * モック用のステートメントを作成します。
     * 
     * @param resultSetType
     *            結果セットタイプ
     * @param resultSetConcurrency
     *            結果セット同時並行性
     * @param resultSetHoldability
     *            結果セット保持力
     * @return モック用のステートメント
     */
    public MockStatement createMockStatement(int resultSetType,
            int resultSetConcurrency, int resultSetHoldability) {
        return new MockStatement(this, resultSetType, resultSetConcurrency,
                resultSetHoldability);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency)
            throws SQLException {
        return createMockStatement(resultSetType, resultSetConcurrency);
    }

    /**
     * モック用のステートメントを作成します。
     * 
     * @param resultSetType
     *            結果セットタイプ
     * @param resultSetConcurrency
     *            結果セット同時並行性
     * @return モック用のステートメント
     */
    public MockStatement createMockStatement(int resultSetType,
            int resultSetConcurrency) {
        return new MockStatement(this, resultSetType, resultSetConcurrency);
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return autoCommit;
    }

    @Override
    public String getCatalog() throws SQLException {
        return null;
    }

    @Override
    public int getHoldability() throws SQLException {
        return 0;
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return null;
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return TRANSACTION_READ_COMMITTED;
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return null;
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    @Override
    public boolean isClosed() throws SQLException {
        return closed;
    }

    /**
     * 閉じているかどうかを設定します。
     * 
     * @param closed
     *            閉じているかどうか
     */
    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return false;
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        return null;
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType,
            int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
        return prepareMockCall(sql, resultSetType, resultSetConcurrency,
                resultSetHoldability);
    }

    /**
     * モック用の呼び出し可能なステートメントを作成します。
     * 
     * @param sql
     *            SQL
     * @param resultSetType
     *            結果セットタイプ
     * @param resultSetConcurrency
     *            結果セット同時並行性
     * @param resultSetHoldability
     *            結果セット保持力
     * @return モック用の呼び出し可能なステートメント
     */
    public MockCallableStatement prepareMockCall(String sql, int resultSetType,
            int resultSetConcurrency, int resultSetHoldability) {
        return new MockCallableStatement(this, sql, resultSetType,
                resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType,
            int resultSetConcurrency) throws SQLException {
        return prepareMockCall(sql, resultSetType, resultSetConcurrency);
    }

    /**
     * モック用の呼び出し可能なステートメントを作成します。
     * 
     * @param sql
     *            SQL
     * @param resultSetType
     *            結果セットタイプ
     * @param resultSetConcurrency
     *            結果セット同時並行性
     * @return モック用の呼び出し可能なステートメント
     */
    public MockCallableStatement prepareMockCall(String sql, int resultSetType,
            int resultSetConcurrency) {
        return new MockCallableStatement(this, sql, resultSetType,
                resultSetConcurrency);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        return prepareMockCall(sql);
    }

    /**
     * モック用の呼び出し可能なステートメントを作成します。
     * 
     * @param sql
     *            SQL
     * @return モック用の呼び出し可能なステートメント
     */
    public MockCallableStatement prepareMockCall(String sql) {
        return new MockCallableStatement(this, sql);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType,
            int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
        return prepareMockStatement(sql, resultSetType, resultSetConcurrency,
                resultSetHoldability);
    }

    /**
     * モック用の準備されたステートメントを作成します。
     * 
     * @param sql
     *            SQL
     * @param resultSetType
     *            結果セットタイプ
     * @param resultSetConcurrency
     *            結果セット同時並行性
     * @param resultSetHoldability
     *            結果セット保持力
     * @return モック用の準備されたステートメント
     */
    public MockPreparedStatement prepareMockStatement(String sql,
            int resultSetType, int resultSetConcurrency,
            int resultSetHoldability) {
        return new MockPreparedStatement(this, sql, resultSetType,
                resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType,
            int resultSetConcurrency) throws SQLException {
        return prepareMockStatement(sql, resultSetType, resultSetConcurrency);
    }

    /**
     * モック用の準備されたステートメントを作成します。
     * 
     * @param sql
     *            SQL
     * @param resultSetType
     *            結果セットタイプ
     * @param resultSetConcurrency
     *            結果セット同時並行性
     * @return モック用の準備されたステートメント
     */
    public MockPreparedStatement prepareMockStatement(String sql,
            int resultSetType, int resultSetConcurrency) {
        return new MockPreparedStatement(this, sql, resultSetType,
                resultSetConcurrency);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
            throws SQLException {
        return prepareMockStatement(sql, autoGeneratedKeys);
    }

    /**
     * モック用の準備されたステートメントを返します。
     * 
     * @param sql
     *            SQL
     * @param autoGeneratedKeys
     *            自動生成されたキーを返すかどうかのフラグ
     * @return モック用の準備されたステートメント
     */
    public MockPreparedStatement prepareMockStatement(String sql,
            int autoGeneratedKeys) {
        return new MockPreparedStatement(this, sql, autoGeneratedKeys);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndices)
            throws SQLException {
        return prepareMockStatement(sql, columnIndices);
    }

    /**
     * モック用の準備されたステートメントを返します。
     * 
     * @param sql
     *            SQL
     * @param columnIndices
     *            自動生成された値を返して欲しいカラムの位置の配列
     * @return モック用の準備されたステートメント
     */
    public MockPreparedStatement prepareMockStatement(String sql,
            int[] columnIndices) {
        return new MockPreparedStatement(this, sql, columnIndices);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames)
            throws SQLException {
        return prepareMockStatement(sql, columnNames);
    }

    /**
     * モック用の準備されたステートメントを返します。
     * 
     * @param sql
     *            SQL
     * @param columnNames
     *            自動生成された値を返して欲しいカラムの名前の配列
     * @return モック用の準備されたステートメント
     */
    public MockPreparedStatement prepareMockStatement(String sql,
            String[] columnNames) {
        return new MockPreparedStatement(this, sql, columnNames);
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return prepareMockStatement(sql);
    }

    /**
     * モック用の準備されたステートメントを返します。
     * 
     * @param sql
     *            SQL
     * @return モック用の準備されたステートメント
     */
    public MockPreparedStatement prepareMockStatement(String sql) {
        return new MockPreparedStatement(this, sql);
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
    }

    @Override
    public void rollback() throws SQLException {
        rolledback = true;
    }

    /**
     * ロールバックしたかどうかを返します。
     * 
     * @return Returns ロールバックしたかどうか
     */
    public boolean isRolledback() {
        return rolledback;
    }

    /**
     * ロールバックしたかどうかを設定します。
     * 
     * @param rolledback
     *            ロールバックしたかどうか
     */
    public void setRolledback(boolean rolledback) {
        this.rolledback = rolledback;
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        this.autoCommit = autoCommit;
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return null;
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        return null;
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> arg0) throws SQLException {
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
    public Clob createClob() throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Blob createBlob() throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public NClob createNClob() throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public void setClientInfo(String name, String value)
	    throws SQLClientInfoException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void setClientInfo(Properties properties)
	    throws SQLClientInfoException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Properties getClientInfo() throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements)
	    throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes)
	    throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void setSchema(String schema) throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public String getSchema() throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void abort(Executor executor) throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds)
	    throws SQLException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
	// TODO Auto-generated method stub
	return 0;
    }
}