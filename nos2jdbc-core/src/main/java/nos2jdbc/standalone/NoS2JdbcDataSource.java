
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