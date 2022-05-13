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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import javax.sql.ConnectionEventListener;
import javax.sql.StatementEventListener;
import javax.sql.XAConnection;
import javax.transaction.xa.XAResource;

/**
 * {@link XAConnection}の実装です。
 * 
 * @author higa
 * 
 */
public class XAConnectionImpl implements XAConnection {

    private Connection connection;

    private XAResource xaResource;

    private List<EventListener> listeners = new ArrayList<>();

    /**
     * {@link XAConnectionImpl}を作成します。
     * 
     * @param connection
     *            コネクション
     */
    public XAConnectionImpl(Connection connection) {
        this.connection = connection;
        this.xaResource = new DBXAResourceImpl(connection);
    }

    @Override
    public XAResource getXAResource() {
        return xaResource;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return connection;
    }

    @Override
    public void close() throws SQLException {
        if (connection == null) {
            return;
        }
        if (!connection.isClosed()) {
            connection.close();
        }
        connection = null;
    }

    @Override
    public synchronized void addConnectionEventListener(
            final ConnectionEventListener listener) {
        listeners.add(listener);
    }

    @Override
    public synchronized void removeConnectionEventListener(
            final ConnectionEventListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void addStatementEventListener(StatementEventListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeStatementEventListener(StatementEventListener listener) {
        listeners.remove(listener);
    }
}