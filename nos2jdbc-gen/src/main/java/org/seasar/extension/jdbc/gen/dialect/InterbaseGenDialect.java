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
package org.seasar.extension.jdbc.gen.dialect;

import jakarta.persistence.GenerationType;

/**
 * Interbaseの方言を扱うクラスです。
 * 
 * @author taedium
 */
public class InterbaseGenDialect extends StandardGenDialect {

    /**
     * インスタンスを構築します。
     */
    public InterbaseGenDialect() {
    }

    @Override
    public String getName() {
        return "interbase";
    }

    @Override
    public GenerationType getDefaultGenerationType() {
        return GenerationType.SEQUENCE;
    }

    @Override
    public String getSequenceNextValString(String sequenceName,
            int allocationSize) {
        return "select RDB$GENERATOR_NAME from RDB$GENERATORS";
    }

}
