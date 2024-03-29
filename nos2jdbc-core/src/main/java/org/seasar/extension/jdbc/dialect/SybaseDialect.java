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
package org.seasar.extension.jdbc.dialect;

import org.seasar.extension.jdbc.SelectForUpdateType;
import org.seasar.framework.util.Pair;

import jakarta.persistence.GenerationType;

/**
 * Sybase用の方言をあつかうクラスです。
 * 
 * @author higa
 * 
 */
public class SybaseDialect extends StandardDialect {

    @Override
    public String getName() {
        return "sybase";
    }

    @Override
    public GenerationType getDefaultGenerationType() {
        return GenerationType.IDENTITY;
    }

    @Override
    public boolean supportsIdentity() {
        return true;
    }

    @Override
    public boolean supportsGetGeneratedKeys() {
        return true;
    }

    @Override
    public String getIdentitySelectString(final String tableName,
            final String columnName) {
        return "select @@identity";
    }

    @Override
    public boolean supportsForUpdate(final SelectForUpdateType type,
            boolean withTarget) {
        return type == SelectForUpdateType.NORMAL;
    }

    @Override
    public String getForUpdateString(final SelectForUpdateType type,
            final int waitSeconds, @SuppressWarnings("unchecked") final Pair<String, String>... aliases) {
        return "";
    }

    @Override
    public boolean supportsLockHint() {
        return true;
    }

    @Override
    public String getLockHintString(final SelectForUpdateType type,
            final int waitSeconds) {
        return " holdlock";
    }

}
