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
package org.seasar.framework.exception;

import jakarta.transaction.RollbackException;

/**
 * {@link RollbackException}をラップする例外です。
 * 
 * @author higa
 * 
 */
public class RollbackRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = -889986104662559877L;

    /**
     * {@link RollbackRuntimeException}を作成します。
     * 
     * @param cause cause of exception
     */
    public RollbackRuntimeException(RollbackException cause) {
        super("ESSR0063", new Object[] { cause }, cause);
    }
}
