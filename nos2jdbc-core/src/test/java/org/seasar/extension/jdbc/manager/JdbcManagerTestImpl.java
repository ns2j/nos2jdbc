package org.seasar.extension.jdbc.manager;

import org.seasar.extension.jdbc.dialect.StandardDialect;
import org.seasar.extension.jdbc.meta.ColumnMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.EntityMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.PropertyMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.TableMetaFactoryImpl;
import org.seasar.extension.jta.TransactionManagerImpl;
import org.seasar.extension.jta.TransactionSynchronizationRegistryImpl;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;
import org.seasar.framework.mock.sql.MockDataSource;

import jakarta.transaction.TransactionManager;

public class JdbcManagerTestImpl extends JdbcManagerImpl {
    private TransactionManager transactionManager;
    
    public JdbcManagerTestImpl() {
	super();
	setDialect(new StandardDialect());
	transactionManager = new TransactionManagerImpl();
	setSyncRegistry(new TransactionSynchronizationRegistryImpl(
		transactionManager));
	setDataSource(new MockDataSource());

	PersistenceConventionImpl convention = new PersistenceConventionImpl();
	EntityMetaFactoryImpl emFactory = new EntityMetaFactoryImpl();
	emFactory.setPersistenceConvention(convention);
	TableMetaFactoryImpl tableMetaFactory = new TableMetaFactoryImpl();
	tableMetaFactory.setPersistenceConvention(convention);
	emFactory.setTableMetaFactory(tableMetaFactory);

	PropertyMetaFactoryImpl pFactory = new PropertyMetaFactoryImpl();
	pFactory.setPersistenceConvention(convention);
	ColumnMetaFactoryImpl cmFactory = new ColumnMetaFactoryImpl();
	cmFactory.setPersistenceConvention(convention);
	pFactory.setColumnMetaFactory(cmFactory);
	emFactory.setPropertyMetaFactory(pFactory);
	emFactory.initialize();
	setEntityMetaFactory(emFactory);
    }
    
    public TransactionManager getTransactionManager() {
	return transactionManager;
    }
}
