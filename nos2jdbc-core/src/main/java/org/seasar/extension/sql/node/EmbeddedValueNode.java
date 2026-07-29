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
package org.seasar.extension.sql.node;

import nos2jdbc.NoS2JdbcConstants;
import org.seasar.extension.sql.Node;
import org.seasar.extension.sql.SemicolonNotAllowedRuntimeException;
import org.seasar.extension.sql.SqlContext;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;

/**
 * 値を埋め込む用の{@link Node}です。
 * 
 * @author higa
 * 
 */
public class EmbeddedValueNode extends AbstractNode {

    private String expression;

    private String baseName;

    private String propertyName;

    /**
     * <code>EmbeddedValueNode</code>を作成します。
     * 
     * @param expression String
     */
    public EmbeddedValueNode(String expression) {
        this.expression = expression;
        int index = expression.indexOf('.');
        if (index > 0) {
            this.baseName = expression.substring(0, index);
            this.propertyName = expression.substring(index + 1);
        } else {
            this.baseName = expression;
        }
    }

    /**
     * 式を返します。
     * 
     * @return String
     */
    public String getExpression() {
        return expression;
    }

    @Override
    public void accept(SqlContext ctx) {
        Object value = ctx.getArg(baseName);
        Class<?> clazz = ctx.getArgType(baseName);
        if (propertyName != null) {
            String[] props = propertyName.split("\\." + "|" + NoS2JdbcConstants.EMBEDDED_PROPERTY_NAME_SEPARATOR);
            for (String prop : props) {
                if (value == null) {
                    break;
                }
                BeanDesc beanDesc = BeanDescFactory.getBeanDesc(clazz);
                PropertyDesc pd = beanDesc.getPropertyDesc(prop);
                value = pd.getValue(value);
                clazz = pd.getPropertyType();
            }
        }
        if (value != null) {
            String sql = value.toString();
            if (sql.indexOf(';') >= 0) {
                throw new SemicolonNotAllowedRuntimeException();
            }
            ctx.addSql(sql);
        }
    }
}