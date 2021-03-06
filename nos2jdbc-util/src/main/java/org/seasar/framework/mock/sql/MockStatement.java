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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

/**
 * {@link Statement}用のモッククラスです。
 * 
 * @author higa
 * 
 */
public class MockStatement implements Statement {

    private MockConnection connection;

    private boolean closed = false;

    private int maxRows;

    private int fetchSize;

    private int queryTimeout;

    private int resultSetType = ResultSet.TYPE_FORWARD_ONLY;

    private int resultSetConcurrency = ResultSet.CONCUR_READ_ONLY;

    private int resultSetHoldability = ResultSet.CLOSE_CURSORS_AT_COMMIT;

    /**
     * {@link MockStatement}を作成します。
     * 
     * @param connection
     *            コネクション
     */
    public MockStatement(MockConnection connection) {
        this(connection, ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY);
    }

    /**
     * {@link MockStatement}を作成します。
     * 
     * @param connection
     *            コネクション
     * @param resultSetType
     *            結果セットタイプ
     * @param resultSetConcurrency
     *            結果セット同時並行性
     */
    public MockStatement(MockConnection connection, int resultSetType,
            int resultSetConcurrency) {
        this(connection, resultSetType, resultSetConcurrency,
                ResultSet.CLOSE_CURSORS_AT_COMMIT);
    }

    /**
     * {@link MockStatement}を作成します。
     * 
     * @param connection
     *            コネクション
     * @param resultSetType
     *            結果セットタイプ
     * @param resultSetConcurrency
     *            結果セット同時並行性
     * @param resultSetHoldability
     *            結果セット保持力
     */
    public MockStatement(MockConnection connection, int resultSetType,
            int resultSetConcurrency, int resultSetHoldability) {
        this.connection = connection;
        this.resultSetType = resultSetType;
        this.resultSetConcurrency = resultSetConcurrency;
        this.resultSetHoldability = resultSetHoldability;
    }

    public void addBatch(String sql) throws SQLException {
    }

    public void cancel() throws SQLException {
    }

    public void clearBatch() throws SQLException {
    }

    public void clearWarnings() throws SQLException {
    }

    public void close() throws SQLException {
        closed = true;
    }

    /**
     * 閉じているかどうかを返します。
     * 
     * @return 閉じているかどうか
     */
    public boolean isClosed() {
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

    public boolean execute(String sql) throws SQLException {
        return false;
    }

    public boolean execute(String sql, int autoGeneratedKeys)
            throws SQLException {
        return false;
    }

    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        return false;
    }

    public boolean execute(String sql, String[] columnNames)
            throws SQLException {
        return false;
    }

    public int[] executeBatch() throws SQLException {
        return null;
    }

    public ResultSet executeQuery(String sql) throws SQLException {
        return null;
    }

    public int executeUpdate(String sql) throws SQLException {
        return 0;
    }

    public int executeUpdate(String sql, int autoGeneratedKeys)
            throws SQLException {
        return 0;
    }

    public int executeUpdate(String sql, int[] columnIndexes)
            throws SQLException {
        return 0;
    }

    public int executeUpdate(String sql, String[] columnNames)
            throws SQLException {
        return 0;
    }

    public Connection getConnection() throws SQLException {
        return connection;
    }

    public int getFetchDirection() throws SQLException {
        return 0;
    }

    public int getFetchSize() throws SQLException {
        return fetchSize;
    }

    public ResultSet getGeneratedKeys() throws SQLException {
        return null;
    }

    public int getMaxFieldSize() throws SQLException {
        return 0;
    }

    public int getMaxRows() throws SQLException {
        return maxRows;
    }

    public boolean getMoreResults() throws SQLException {
        return false;
    }

    public boolean getMoreResults(int current) throws SQLException {
        return false;
    }

    public int getQueryTimeout() throws SQLException {
        return queryTimeout;
    }

    public ResultSet getResultSet() throws SQLException {
        return new MockResultSet();
    }

    public int getResultSetConcurrency() throws SQLException {
        return resultSetConcurrency;
    }

    public int getResultSetHoldability() throws SQLException {
        return resultSetHoldability;
    }

    public int getResultSetType() throws SQLException {
        return resultSetType;
    }

    public int getUpdateCount() throws SQLException {
        return 0;
    }

    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    public void setCursorName(String name) throws SQLException {
    }

    public void setEscapeProcessing(boolean enable) throws SQLException {
    }

    public void setFetchDirection(int direction) throws SQLException {
    }

    public void setFetchSize(int fetchSize) throws SQLException {
        this.fetchSize = fetchSize;
    }

    public void setMaxFieldSize(int max) throws SQLException {
    }

    public void setMaxRows(int maxRows) throws SQLException {
        this.maxRows = maxRows;
    }

    public void setQueryTimeout(int queryTimeout) throws SQLException {
        this.queryTimeout = queryTimeout;
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
	// TODO Auto-generated method stub
	return null;
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
	// TODO Auto-generated method stub
	return false;
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

}
