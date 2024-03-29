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
import jakarta.transaction.Status;
import jakarta.transaction.SystemException;
import jakarta.transaction.Transaction;
import jakarta.transaction.TransactionManager;
import jakarta.transaction.TransactionSynchronizationRegistry;
import jakarta.transaction.UserTransaction;

/**
 * 機能が限定された{@link TransactionManager}の実装クラスです。
 * <p>
 * このトランザクションマネージャは、{@link UserTransaction}と{@link TransactionSynchronizationRegistry}を利用して実装しています。
 * そのため、以下の機能がサポートされません。
 * </p>
 * <ul>
 * <li>{@link TransactionManager#resume(Transaction)}</li>
 * <li>{@link TransactionManager#suspend()}</li>
 * </ul>
 * 
 * @author koichik
 */
public class RestrictedTransactionManagerImpl implements TransactionManager {

    /** ユーザトランザクション */
    protected UserTransaction userTransaction;

    /** トランザクションシンクロナイゼーションレジストリ */
    protected TransactionSynchronizationRegistry synchronizationRegistry;

    /**
     * インスタンスを構築します。
     */
    public RestrictedTransactionManagerImpl() {
    }

    /**
     * インスタンスを構築します。
     * 
     * @param userTransaction
     *            ユーザトランザクション
     * @param synchronizationRegistry
     *            トランザクションシンクロナイゼーションレジストリ
     */
    public RestrictedTransactionManagerImpl(
            final UserTransaction userTransaction,
            final TransactionSynchronizationRegistry synchronizationRegistry) {
        this.userTransaction = userTransaction;
        this.synchronizationRegistry = synchronizationRegistry;
    }

    /**
     * ユーザトランザクションを設定します。
     * 
     * @param userTransaction
     *            ユーザトランザクション
     */
    public void setUserTransaction(final UserTransaction userTransaction) {
        this.userTransaction = userTransaction;
    }

    /**
     * トランザクションシンクロナイゼーションレジストリを設定します。
     * 
     * @param synchronizationRegistry
     *            トランザクションシンクロナイゼーションレジストリ
     */
    public void setSynchronizationRegistry(
            final TransactionSynchronizationRegistry synchronizationRegistry) {
        this.synchronizationRegistry = synchronizationRegistry;
    }

    @Override
    public void begin() throws NotSupportedException, SystemException {
        userTransaction.begin();
    }

    @Override
    public void commit() throws HeuristicMixedException,
            HeuristicRollbackException, IllegalStateException,
            RollbackException, SecurityException, SystemException {
        userTransaction.commit();
    }

    @Override
    public int getStatus() throws SystemException {
        return userTransaction.getStatus();
    }

    @Override
    public Transaction getTransaction() throws SystemException {
        final int status = getStatus();
        if (status == Status.STATUS_NO_TRANSACTION
                || status == Status.STATUS_UNKNOWN) {
            return null;
        }
        RestrictedTransactionImpl tx = (RestrictedTransactionImpl) synchronizationRegistry
                .getResource(this);
        if (tx == null) {
            tx = new RestrictedTransactionImpl(userTransaction,
                    synchronizationRegistry);
            synchronizationRegistry.putResource(this, tx);
        }
        return tx;
    }

    @Override
    public void resume(final Transaction tx) throws IllegalStateException,
            InvalidTransactionException, SystemException {
        throw new UnsupportedOperationException("resume");
    }

    @Override
    public void rollback() throws IllegalStateException, SecurityException,
            SystemException {
        userTransaction.rollback();
    }

    @Override
    public void setRollbackOnly() throws IllegalStateException, SystemException {
        userTransaction.setRollbackOnly();
    }

    @Override
    public void setTransactionTimeout(final int seconds) throws SystemException {
        userTransaction.setTransactionTimeout(seconds);
    }

    @Override
    public Transaction suspend() throws SystemException {
        throw new UnsupportedOperationException("suspend");
    }

}
