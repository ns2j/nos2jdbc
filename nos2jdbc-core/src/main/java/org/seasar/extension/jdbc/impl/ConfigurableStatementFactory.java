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

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import org.seasar.extension.jdbc.StatementFactory;
import org.seasar.framework.util.StatementUtil;

/**
 * {@link Statement}をカスタマイズするためのファクトリです。
 * 
 * @author manhole
 * @author higa
 */
public class ConfigurableStatementFactory implements StatementFactory {

    /**
     * {@link StatementFactory}です。
     */
    protected StatementFactory statementFactory;

    /**
     * フェッチサイズです。
     */
    protected Integer fetchSize;

    /**
     * 最大行数です。
     */
    protected Integer maxRows;

    /**
     * クエリのタイムアウトです。
     */
    protected Integer queryTimeout;

    /**
     * {@link ConfigurableStatementFactory}を作成します。
     *
     * @param statementFactory StatementFactory
     */
    public ConfigurableStatementFactory(StatementFactory statementFactory) {
        if (statementFactory == null) {
            throw new NullPointerException("statementFactory");
        }
        this.statementFactory = statementFactory;
    }

    @Override
    public PreparedStatement createPreparedStatement(Connection con, String sql) {
        PreparedStatement ps = statementFactory.createPreparedStatement(con,
                sql);
        configurePreparedStatement(ps);
        return ps;
    }

    @Override
    public CallableStatement createCallableStatement(Connection con, String sql) {
        CallableStatement cs = statementFactory.createCallableStatement(con,
                sql);
        configurePreparedStatement(cs);
        return cs;
    }

    /**
     * {@link PreparedStatement}をカスタマイズします。
     * 
     * @param ps PreparedStatement
     */
    protected void configurePreparedStatement(PreparedStatement ps) {
        if (fetchSize != null) {
            StatementUtil.setFetchSize(ps, fetchSize.intValue());
        }
        if (maxRows != null) {
            StatementUtil.setMaxRows(ps, maxRows.intValue());
        }
        if (queryTimeout != null) {
            StatementUtil.setQueryTimeout(ps, queryTimeout.intValue());
        }
    }

    /**
     * フェッチサイズを設定します。
     * 
     * @param fetchSize Integer
     */
    public void setFetchSize(Integer fetchSize) {
        this.fetchSize = fetchSize;
    }

    /**
     * 最大行数を設定します。
     * 
     * @param maxRows Integer
     */
    public void setMaxRows(Integer maxRows) {
        this.maxRows = maxRows;
    }

    /**
     * クエリタイムアウトを設定します。
     * 
     * @param queryTimeout Integer
     */
    public void setQueryTimeout(Integer queryTimeout) {
        this.queryTimeout = queryTimeout;
    }

}
