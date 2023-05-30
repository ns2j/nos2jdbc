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
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.TransactionManager;
import jakarta.transaction.UserTransaction;

/**
 * {@link UserTransaction}の実装クラスです。
 * 
 * @author higa
 * 
 */
public class UserTransactionImpl implements UserTransaction {

    /**
     * トランザクションマネージャです。
     */
    protected TransactionManager tm;

    /**
     * {@link UserTransactionImpl}を作成します。
     * 
     * @param tm TransactionManager
     */
    public UserTransactionImpl(final TransactionManager tm) {
        this.tm = tm;
    }

    @Override
    public void begin() throws NotSupportedException, SystemException {
        tm.begin();
    }

    @Override
    public void commit() throws HeuristicMixedException,
            HeuristicRollbackException, IllegalStateException,
            RollbackException, SecurityException, SystemException {
        tm.commit();
    }

    @Override
    public int getStatus() throws SystemException {
        return tm.getStatus();
    }

    @Override
    public void rollback() throws IllegalStateException, SecurityException,
            SystemException {
        tm.rollback();
    }

    @Override
    public void setRollbackOnly() throws IllegalStateException, SystemException {
        tm.setRollbackOnly();
    }

    @Override
    public void setTransactionTimeout(final int timeout) throws SystemException {
        tm.setTransactionTimeout(timeout);
    }
}
