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
package org.seasar.extension.jdbc.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.seasar.extension.jdbc.ResultSetHandler;

/**
 * Beanを返す {@link ResultSetHandler}です。
 * 
 * @author higa
 * 
 */
public class BeanResultSetHandler extends AbstractBeanResultSetHandler {

    /**
     * {@link BeanResultSetHandler}を作成します。
     * 
     * @param beanClass
     *            Beanクラス
     */
    public BeanResultSetHandler(Class<?> beanClass) {
        super(beanClass);
    }

    @Override
    public Object handle(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return createRow(rs, createPropertyTypes(rs.getMetaData()));
        }
        return null;
    }
}