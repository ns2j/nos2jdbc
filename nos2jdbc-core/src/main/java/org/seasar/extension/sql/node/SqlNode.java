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

import org.seasar.extension.sql.Node;
import org.seasar.extension.sql.SqlContext;

/**
 * SQL用の{@link Node}です。
 * 
 * @author higa
 * 
 */
public class SqlNode extends AbstractNode {

    private String sql;

    /**
     * <code>SqlNode</code>を作成します。
     * 
     * @param sql String
     */
    public SqlNode(String sql) {
        this.sql = sql;
    }

    /**
     * SQLを返します。
     * 
     * @return String
     */
    public String getSql() {
        return sql;
    }

    @Override
    public void accept(SqlContext ctx) {
        ctx.addSql(sql);
    }
}