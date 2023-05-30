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
package org.seasar.extension.jta;

import jakarta.transaction.HeuristicMixedException;
import jakarta.transaction.HeuristicRollbackException;
import jakarta.transaction.InvalidTransactionException;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.Transaction;
import jakarta.transaction.TransactionManager;

//i import org.seasar.framework.container.factory.SingletonS2ContainerFactory;

/**
 * @author koichik
 */
public class SingletonTransactionManagerProxy implements TransactionManager {

    @Override
    public void begin() throws NotSupportedException, SystemException {
        getTransactionManager().begin();
    }

    @Override
    public void commit() throws HeuristicMixedException,
            HeuristicRollbackException, IllegalStateException,
            RollbackException, SecurityException, SystemException {
        getTransactionManager().commit();
    }

    @Override
    public int getStatus() throws SystemException {
        return getTransactionManager().getStatus();
    }

    @Override
    public Transaction getTransaction() throws SystemException {
        return getTransactionManager().getTransaction();
    }

    @Override
    public void resume(final Transaction tx) throws IllegalStateException,
            InvalidTransactionException, SystemException {
        getTransactionManager().resume(tx);
    }

    @Override
    public void rollback() throws IllegalStateException, SecurityException,
            SystemException {
        getTransactionManager().rollback();
    }

    @Override
    public void setRollbackOnly() throws IllegalStateException, SystemException {
        getTransactionManager().setRollbackOnly();
    }

    @Override
    public void setTransactionTimeout(final int timeout) throws SystemException {
        getTransactionManager().setTransactionTimeout(timeout);
    }

    @Override
    public Transaction suspend() throws SystemException {
        return getTransactionManager().suspend();
    }

    /**
     * トランザクションマネージャを返します。
     * 
     * @return トランザクションマネージャ
     */
    protected TransactionManager getTransactionManager() {
//i        return (TransactionManager) SingletonS2ContainerFactory.getContainer()
//i                .getComponent(TransactionManager.class);
    	return null;
    }

}
