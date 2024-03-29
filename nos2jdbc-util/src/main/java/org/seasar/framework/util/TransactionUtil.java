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
package org.seasar.framework.util;

import javax.transaction.xa.XAResource;

import org.seasar.framework.exception.RollbackRuntimeException;
import org.seasar.framework.exception.SystemRuntimeException;

import jakarta.transaction.RollbackException;
import jakarta.transaction.Synchronization;
import jakarta.transaction.SystemException;
import jakarta.transaction.Transaction;

/**
 * {@link Transaction}用のユーティリティクラスです。
 * 
 * @author higa
 * 
 */
public class TransactionUtil {

    /**
     * インスタンスを構築します。
     */
    protected TransactionUtil() {
    }

    /**
     * トランザクションのステータスを返します。
     * 
     * @param tx
     *            トランザクション
     * @return トランザクションのステータス
     */
    public static int getStatus(final Transaction tx) {
        try {
            return tx.getStatus();
        } catch (final SystemException e) {
            throw new SystemRuntimeException(e);
        }
    }

    /**
     * トランザクションに参加します。
     * 
     * @param tx tx
     * @param xaResource xaResource
     */
    public static void enlistResource(Transaction tx, XAResource xaResource) {
        try {
            tx.enlistResource(xaResource);
        } catch (SystemException e) {
            throw new SystemRuntimeException(e);
        } catch (RollbackException e) {
            throw new RollbackRuntimeException(e);
        }
    }

    /**
     * {@link Synchronization}を登録します。
     * 
     * @param tx tx
     * @param sync sync
     */
    public static void registerSynchronization(Transaction tx,
            Synchronization sync) {

        try {
            tx.registerSynchronization(sync);
        } catch (SystemException e) {
            throw new SystemRuntimeException(e);
        } catch (RollbackException e) {
            throw new RollbackRuntimeException(e);
        }
    }
}
