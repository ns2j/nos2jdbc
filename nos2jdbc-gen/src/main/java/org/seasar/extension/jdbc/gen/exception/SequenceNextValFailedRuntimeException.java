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
package org.seasar.extension.jdbc.gen.exception;

import org.seasar.framework.exception.SRuntimeException;

/**
 * シーケンスの次の値の取得に失敗した場合にスローされる例外です。
 * 
 * @author taedium
 */
public class SequenceNextValFailedRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 1L;

    /** シーケンス名 */
    protected String sequenceName;

    /**
     * インスタンスを構築します。
     * 
     * @param sequenceName
     *            シーケンス名
     */
    public SequenceNextValFailedRuntimeException(String sequenceName) {
        super("ES2JDBCGen0026", new Object[] { sequenceName });
        this.sequenceName = sequenceName;
    }

    /**
     * インスタンスを構築します。
     * 
     * @param sequenceName
     *            シーケンス名
     * @param cause
     *            原因
     */
    public SequenceNextValFailedRuntimeException(String sequenceName,
            Throwable cause) {
        super("ES2JDBCGen0026", new Object[] { sequenceName }, cause);
        this.sequenceName = sequenceName;
    }

    /**
     * シーケンス名を返します。
     * 
     * @return シーケンス名
     */
    public String getSequenceName() {
        return sequenceName;
    }

}
