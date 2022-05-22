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
package org.seasar.extension.jdbc.gen.internal.version;

import java.io.File;

import org.seasar.extension.jdbc.gen.internal.util.VersionUtil;
import org.seasar.extension.jdbc.gen.version.DdlInfoFile;
import org.seasar.framework.log.Logger;

/**
 * {@link DdlInfoFile}の実装クラスです。
 * 
 * @author taedium
 */
public class DdlInfoFileImpl implements DdlInfoFile{

    /** ロガー */
    protected static Logger logger = Logger.getLogger(DdlInfoFileImpl.class);

    /** エンコーディング */
    protected static final String ENCODING = "UTF-8";

    /** DDLファイル */
    protected File file;

    /** バージョン番号 */
    protected Integer versionNo;

    /**
     * インスタンスを構築します。
     * 
     * @param file
     *            ファイル
     */
    public DdlInfoFileImpl(File file) {
        if (file == null) {
            throw new NullPointerException("file");
        }
        this.file = file;
    }

    @Override
    public int getCurrentVersionNo() {
        versionNo = VersionUtil.getVersionNo(file);
        return versionNo;
    }

    @Override
    public int getNextVersionNo() {
        return VersionUtil.getNextVersionNo(file);
    }

    @Override
    public void applyNextVersionNo(String comment) {
        VersionUtil.applyNextVersionNo(file, comment);
    }
}
