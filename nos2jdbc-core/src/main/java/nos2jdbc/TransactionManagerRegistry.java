package nos2jdbc;

import javax.transaction.TransactionManager;

public class TransactionManagerRegistry {
    static TransactionManager transactionManager;

    static public synchronized void register(TransactionManager tm) {
	transactionManager = tm;
    }

    static public synchronized TransactionManager get() {
	return transactionManager;
    }

}
