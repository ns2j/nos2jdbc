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
package org.seasar.extension.jdbc.exception;

import org.seasar.framework.exception.SRuntimeException;

import jakarta.persistence.GenerationType;

/**
 * 識別子の自動生成に{@link GenerationType#IDENTITY}が指定されたがDBMSがサポートしていない場合にスローされる例外です。
 * 
 * @author koichik
 */
public class IdentityGeneratorNotSupportedRuntimeException extends
        SRuntimeException {

    private static final long serialVersionUID = 1L;

    /** エンティティ名 */
    protected String entityName;

    /** 識別子のプロパティ名 */
    protected String propertyName;

    /** DBMS名 */
    protected String dbmsName;

    /**
     * インスタンスを構築します。
     * 
     * @param entityName
     *            エンティティ名
     * @param propertyName
     *            プロパティ名
     * @param dbmsName
     *            DBMS名
     */
    public IdentityGeneratorNotSupportedRuntimeException(
            final String entityName, final String propertyName,
            final String dbmsName) {
        super("ESSR0741", new Object[] { entityName, propertyName, dbmsName });
        this.entityName = entityName;
        this.propertyName = propertyName;
        this.dbmsName = dbmsName;
    }

    /**
     * エンティティ名を返します。
     * 
     * @return エンティティ名
     */
    public String getEntityName() {
        return entityName;
    }

    /**
     * プロパティ名を返します。
     * 
     * @return プロパティ名
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * DBMS名を返します。
     * 
     * @return DBMS名
     */
    public String getDbmsName() {
        return dbmsName;
    }

}
