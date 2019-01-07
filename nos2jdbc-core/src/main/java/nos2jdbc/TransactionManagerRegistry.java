package nos2jdbc;

import javax.transaction.TransactionManager;

import org.seasar.extension.jta.TransactionManagerImpl;

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
