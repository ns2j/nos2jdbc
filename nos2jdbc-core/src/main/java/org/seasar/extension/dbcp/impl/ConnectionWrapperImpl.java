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
package org.seasar.extension.dbcp.impl;

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

import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;
import javax.sql.XAConnection;
import javax.transaction.xa.XAResource;

import org.seasar.extension.dbcp.ConnectionPool;
import org.seasar.extension.dbcp.ConnectionWrapper;
import org.seasar.extension.jdbc.impl.PreparedStatementWrapper;
import org.seasar.framework.exception.SSQLException;
import org.seasar.framework.log.Logger;

import jakarta.transaction.Transaction;

/**
 * {@link ConnectionWrapper}の実装クラスです。
 * 
 * @author higa
 * 
 */
public class ConnectionWrapperImpl implements ConnectionWrapper,
        ConnectionEventListener {

    private static final Logger logger_ = Logger
            .getLogger(ConnectionWrapperImpl.class);

    private XAConnection xaConnection_;

    private Connection physicalConnection_;

    private XAResource xaResource_;

    private ConnectionPool connectionPool_;

    private boolean closed_ = false;

    private Transaction tx_;

    /**
     * {@link ConnectionWrapperImpl}を作成します。
     * 
     * @param xaConnection
     *            XAコネクション
     * @param physicalConnection
     *            物理コネクション
     * @param connectionPool
     *            コネクションプール
     * @param tx
     *            トランザクション
     * @throws SQLException
     *             SQL例外が発生した場合
     */
    public ConnectionWrapperImpl(final XAConnection xaConnection,
            final Connection physicalConnection,
            final ConnectionPool connectionPool, final Transaction tx)
            throws SQLException {
        xaConnection_ = xaConnection;
        physicalConnection_ = physicalConnection;
        xaResource_ = new XAResourceWrapperImpl(xaConnection.getXAResource(),
                this);
        connectionPool_ = connectionPool;
        tx_ = tx;
        xaConnection_.addConnectionEventListener(this);
    }

    @Override
    public Connection getPhysicalConnection() {
        return physicalConnection_;
    }

    @Override
    public XAResource getXAResource() {
        return xaResource_;
    }

    @Override
    public XAConnection getXAConnection() {
        return xaConnection_;
    }

    @Override
    public void init(final Transaction tx) {
        closed_ = false;
        tx_ = tx;
    }

    @Override
    public void cleanup() {
        xaConnection_.removeConnectionEventListener(this);
        closed_ = true;
        xaConnection_ = null;
        physicalConnection_ = null;
        tx_ = null;
    }

    @Override
    public void closeReally() {
        if (xaConnection_ == null) {
            return;
        }
        closed_ = true;
        try {
            if (!physicalConnection_.isClosed()) {
                if (!physicalConnection_.getAutoCommit()) {
                    try {
                        physicalConnection_.rollback();
                        physicalConnection_.setAutoCommit(true);
                    } catch (final SQLException ex) {
                        logger_.log(ex);
                    }
                }
                physicalConnection_.close();
            }
        } catch (final SQLException ex) {
            logger_.log(ex);
        } finally {
            physicalConnection_ = null;
        }

        try {
            xaConnection_.close();
            logger_.log("DSSR0001", null);
        } catch (final SQLException ex) {
            logger_.log(ex);
        } finally {
            xaConnection_ = null;
        }
    }

    private void assertOpened() throws SQLException {
        if (closed_) {
            throw new SSQLException("ESSR0062", null);
        }
    }

    private void assertLocalTx() throws SQLException {
        if (tx_ != null) {
            throw new SSQLException("ESSR0366", null);
        }
    }

    @Override
    public void release() throws SQLException {
        if (!closed_) {
            connectionPool_.release(this);
        }
    }

    @Override
    public void connectionClosed(final ConnectionEvent event) {
    }

    @Override
    public void connectionErrorOccurred(final ConnectionEvent event) {
        try {
            release();
        } catch (final SQLException ignore) {
        }
    }

    @Override
    public Statement createStatement() throws SQLException {
        assertOpened();
        try {
            return physicalConnection_.createStatement();
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    @Override
    public PreparedStatement prepareStatement(final String sql)
            throws SQLException {
        assertOpened();
        try {
            return new PreparedStatementWrapper(physicalConnection_
                    .prepareStatement(sql), sql);
        } catch (final SQLException ex) {
            release();
            throw wrapException(ex, sql);
        }
    }

    @Override
    public CallableStatement prepareCall(final String sql) throws SQLException {
        assertOpened();
        try {
            return physicalConnection_.prepareCall(sql);
        } catch (final SQLException ex) {
            release();
            throw wrapException(ex, sql);
        }
    }

    @Override
    public String nativeSQL(final String sql) throws SQLException {
        assertOpened();
        try {
            return physicalConnection_.nativeSQL(sql);
        } catch (final SQLException ex) {
            release();
            throw wrapException(ex, sql);
        }
    }

    @Override
    public boolean isClosed() throws SQLException {
        return closed_;
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        assertOpened();
        try {
            return physicalConnection_.getMetaData();
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    @Override
    public void setReadOnly(final boolean readOnly) throws SQLException {
        assertOpened();
        try {
            physicalConnection_.setReadOnly(readOnly);
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        assertOpened();
        try {
            return physicalConnection_.isReadOnly();
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    @Override
    public void setCatalog(final String catalog) throws SQLException {
        assertOpened();
        try {
            physicalConnection_.setCatalog(catalog);
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    @Override
    public String getCatalog() throws SQLException {
        assertOpened();
        try {
            return physicalConnection_.getCatalog();
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    @Override
    public void close() throws SQLException {
        if (closed_) {
            return;
        }
        if (logger_.isDebugEnabled()) {
            logger_.log("DSSR0002", new Object[] { tx_ });
        }
        if (tx_ == null) {
            connectionPool_.checkIn(this);
        } else {
            connectionPool_.checkInTx(tx_);
        }
    }

    @Override
    public void setTransactionIsolation(final int level) throws SQLException {
        assertOpened();
        try {
            physicalConnection_.setTransactionIsolation(level);
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        assertOpened();
        try {
            return physicalConnection_.getTransactionIsolation();
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        assertOpened();
        try {
            return physicalConnection_.getWarnings();
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    @Override
    public void clearWarnings() throws SQLException {
        assertOpened();
        try {
            physicalConnection_.clearWarnings();
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    @Override
    public void commit() throws SQLException {
        assertOpened();
        assertLocalTx();
        try {
            physicalConnection_.commit();
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    @Override
    public void rollback() throws SQLException {
        assertOpened();
        assertLocalTx();
        try {
            physicalConnection_.rollback();
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    @Override
    public void setAutoCommit(final boolean autoCommit) throws SQLException {
        assertOpened();
        if (autoCommit) {
            assertLocalTx();
        }
        try {
            physicalConnection_.setAutoCommit(autoCommit);
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        assertOpened();
        try {
            return physicalConnection_.getAutoCommit();
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    @Override
    public Statement createStatement(final int resultSetType,
            final int resultSetConcurrency) throws SQLException {

        assertOpened();
        try {
            return physicalConnection_.createStatement(resultSetType,
                    resultSetConcurrency);
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        assertOpened();
        try {
            return physicalConnection_.getTypeMap();
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    @Override
    public void setTypeMap(final Map<String, Class<?>> map) throws SQLException {
        assertOpened();
        try {
            physicalConnection_.setTypeMap(map);
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    @Override
    public PreparedStatement prepareStatement(final String sql,
            final int resultSetType, final int resultSetConcurrency)
            throws SQLException {

        assertOpened();
        try {
            return new PreparedStatementWrapper(
                    physicalConnection_.prepareStatement(sql, resultSetType,
                            resultSetConcurrency), sql);
        } catch (final SQLException ex) {
            release();
            throw wrapException(ex, sql);
        }
    }

    @Override
    public CallableStatement prepareCall(final String sql,
            final int resultSetType, final int resultSetConcurrency)
            throws SQLException {

        assertOpened();
        try {
            return physicalConnection_.prepareCall(sql, resultSetType,
                    resultSetConcurrency);
        } catch (final SQLException ex) {
            release();
            throw wrapException(ex, sql);
        }
    }

    @Override
    public void setHoldability(final int holdability) throws SQLException {
        assertOpened();
        try {
            physicalConnection_.setHoldability(holdability);
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    @Override
    public int getHoldability() throws SQLException {
        assertOpened();
        try {
            return physicalConnection_.getHoldability();
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        assertOpened();
        assertLocalTx();
        try {
            return physicalConnection_.setSavepoint();
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    @Override
    public Savepoint setSavepoint(final String name) throws SQLException {
        assertOpened();
        assertLocalTx();
        try {
            return physicalConnection_.setSavepoint(name);
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    @Override
    public void rollback(final Savepoint savepoint) throws SQLException {
        assertOpened();
        assertLocalTx();
        try {
            physicalConnection_.rollback(savepoint);
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    @Override
    public void releaseSavepoint(final Savepoint savepoint) throws SQLException {
        assertOpened();
        assertLocalTx();
        try {
            physicalConnection_.releaseSavepoint(savepoint);
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    @Override
    public Statement createStatement(final int resultSetType,
            final int resultSetConcurrency, final int resultSetHoldability)
            throws SQLException {

        assertOpened();
        try {
            return physicalConnection_.createStatement(resultSetType,
                    resultSetConcurrency, resultSetHoldability);
        } catch (final SQLException ex) {
            release();
            throw ex;
        }
    }

    @Override
    public PreparedStatement prepareStatement(final String sql,
            final int resultSetType, final int resultSetConcurrency,
            final int resultSetHoldability) throws SQLException {

        assertOpened();
        try {
            return new PreparedStatementWrapper(physicalConnection_
                    .prepareStatement(sql, resultSetType, resultSetConcurrency,
                            resultSetHoldability), sql);
        } catch (final SQLException ex) {
            release();
            throw wrapException(ex, sql);
        }
    }

    @Override
    public CallableStatement prepareCall(final String sql,
            final int resultSetType, final int resultSetConcurrency,
            final int resultSetHoldability) throws SQLException {

        assertOpened();
        try {
            return physicalConnection_.prepareCall(sql, resultSetType,
                    resultSetConcurrency, resultSetHoldability);
        } catch (final SQLException ex) {
            release();
            throw wrapException(ex, sql);
        }
    }

    @Override
    public PreparedStatement prepareStatement(final String sql,
            final int autoGeneratedKeys) throws SQLException {

        assertOpened();
        try {
            return new PreparedStatementWrapper(physicalConnection_
                    .prepareStatement(sql, autoGeneratedKeys), sql);
        } catch (final SQLException ex) {
            release();
            throw wrapException(ex, sql);
        }
    }

    @Override
    public PreparedStatement prepareStatement(final String sql,
            final int[] columnIndexes) throws SQLException {

        assertOpened();
        try {
            return new PreparedStatementWrapper(physicalConnection_
                    .prepareStatement(sql, columnIndexes), sql);
        } catch (final SQLException ex) {
            release();
            throw wrapException(ex, sql);
        }
    }

    @Override
    public PreparedStatement prepareStatement(final String sql,
            final String[] columnNames) throws SQLException {

        assertOpened();
        try {
            return new PreparedStatementWrapper(physicalConnection_
                    .prepareStatement(sql, columnNames), sql);
        } catch (final SQLException ex) {
            release();
            throw wrapException(ex, sql);
        }
    }

    private SQLException wrapException(final SQLException e, final String sql) {
        return new SSQLException("ESSR0072",
                new Object[] { sql, e.getMessage(),
                        Integer.valueOf(e.getErrorCode()), e.getSQLState() }, e
                        .getSQLState(), e.getErrorCode(), e, sql);
    }

    @Override
    public void abort(Executor arg0) throws SQLException {
	assertOpened();
	try {
	    physicalConnection_.abort(arg0);
	} catch (final SQLException ex) {
	    release();
	    throw ex;
	}
    }

    @Override
    public Clob createClob() throws SQLException {
	assertOpened();
	try {
	    return physicalConnection_.createClob();
	} catch (final SQLException ex) {
	    release();
	    throw ex;
	}
    }

    @Override
    public Blob createBlob() throws SQLException {
	assertOpened();
	try {
	    return physicalConnection_.createBlob();
	} catch (final SQLException ex) {
	    release();
	    throw ex;
	}
    }

    @Override
    public NClob createNClob() throws SQLException {
	assertOpened();
	try {
	    return physicalConnection_.createNClob();
	} catch (final SQLException ex) {
	    release();
	    throw ex;
	}
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
	assertOpened();
	try {
	    return physicalConnection_.createSQLXML();
	} catch (final SQLException ex) {
	    release();
	    throw ex;
	}
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
	assertOpened();
	try {
	    return physicalConnection_.isValid(timeout);
	} catch (final SQLException ex) {
	    release();
	    throw ex;
	}
    }

    @Override
    public void setClientInfo(String name, String value)
	    throws SQLClientInfoException {
	physicalConnection_.setClientInfo(name, value);
    }

    @Override
    public void setClientInfo(Properties properties)
	    throws SQLClientInfoException {
	physicalConnection_.setClientInfo(properties);
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
	assertOpened();
	try {
	    return physicalConnection_.getClientInfo(name);
	} catch (final SQLException ex) {
	    release();
	    throw ex;
	}
    }

    @Override
    public Properties getClientInfo() throws SQLException {
	assertOpened();
	try {
	    return physicalConnection_.getClientInfo();
	} catch (final SQLException ex) {
	    release();
	    throw ex;
	}
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements)
	    throws SQLException {
	assertOpened();
	try {
	    return physicalConnection_.createArrayOf(typeName, elements);
	} catch (final SQLException ex) {
	    release();
	    throw ex;
	}
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes)
	    throws SQLException {
	assertOpened();
	try {
	    return physicalConnection_.createStruct(typeName, attributes);
	} catch (final SQLException ex) {
	    release();
	    throw ex;
	}
    }

    @Override
    public void setSchema(String schema) throws SQLException {
	assertOpened();
	try {
	    physicalConnection_.setSchema(schema);
	} catch (final SQLException ex) {
	    release();
	    throw ex;
	}
    }

    @Override
    public String getSchema() throws SQLException {
	assertOpened();
	try {
	    return physicalConnection_.getSchema();
	} catch (final SQLException ex) {
	    release();
	    throw ex;
	}
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds)
	    throws SQLException {
	assertOpened();
	try {
	    physicalConnection_.setNetworkTimeout(executor, milliseconds);
	} catch (final SQLException ex) {
	    release();
	    throw ex;
	}
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
	assertOpened();
	try {
	    return physicalConnection_.getNetworkTimeout();
	} catch (final SQLException ex) {
	    release();
	    throw ex;
	}
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
	assertOpened();
	try {
	    return physicalConnection_.unwrap(iface);
	} catch (final SQLException ex) {
	    release();
	    throw ex;
	}
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
	assertOpened();
	try {
	    return physicalConnection_.isWrapperFor(iface);
	} catch (final SQLException ex) {
	    release();
	    throw ex;
	}
    }
}
