package nos2jdbc;

import org.seasar.extension.jta.TransactionManagerImpl;

import jakarta.transaction.TransactionManager;

public class TransactionManagerRegistry {
    static TransactionManager transactionManager;

    static public synchronized void register(TransactionManager tm) {
	transactionManager = tm;
    }

    static public synchronized TransactionManager get() {
	if (transactionManager == null)
	    transactionManager = new TransactionManagerImpl();
	return transactionManager;
    }

}
