/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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

package nos2jdbc.standalone;

import org.seasar.extension.dbcp.impl.ConnectionPoolImpl;
import org.seasar.extension.dbcp.impl.DataSourceImpl;
import org.seasar.extension.dbcp.impl.XADataSourceImpl;

import jakarta.transaction.TransactionManager;

public class NoS2JdbcDataSource extends DataSourceImpl implements AutoCloseable {
    private static final long serialVersionUID = 1L;

    public NoS2JdbcDataSource(
            TransactionManager tm,
            String driverName,
            String url,
            String user,
            String password,
            int poolSize) {
        super(createConnectionPool(
                tm, driverName, url, user, password, poolSize));
    }

    private static ConnectionPoolImpl createConnectionPool(
            TransactionManager tm,
            String driverName,
            String url,
            String user,
            String password,
            int poolSize) {
        XADataSourceImpl xaDataSource = new XADataSourceImpl();
        xaDataSource.setDriverClassName(driverName);
        xaDataSource.setURL(url);
        xaDataSource.setUser(user);
        xaDataSource.setPassword(password);

        ConnectionPoolImpl pool = new ConnectionPoolImpl();
        pool.setXADataSource(xaDataSource);
        pool.setTransactionManager(tm);
        pool.setTimeout(20);
        pool.setMaxPoolSize(poolSize);
        pool.setAllowLocalTx(true);
        return pool;
    }

    @Override
    public void close() {
        getConnectionPool().close();
    }
}