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
 * ダンプファイルが空の場合にスローされます。
 * 
 * @author taedium
 */
public class DumpFileEmptyRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 1L;

    /** ダンプファイルのパス */
    protected String dumpFilePath;

    /**
     * インスタンスを構築します。
     * 
     * @param dumpFilePath
     *            ダンプファイルのパス
     */
    public DumpFileEmptyRuntimeException(String dumpFilePath) {
        super("ES2JDBCGen0016", new Object[] { dumpFilePath, });
        this.dumpFilePath = dumpFilePath;
    }

    /**
     * ダンプファイルのパスを返します。
     * 
     * @return ダンプファイルのパス
     */
    public String getDumpFilePath() {
        return dumpFilePath;
    }

}
