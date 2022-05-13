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

import java.util.LinkedList;

import org.seasar.extension.jdbc.SqlLog;
import org.seasar.extension.jdbc.SqlLogRegistry;

/**
 * {@link SqlLogRegistry}の実装クラスです。
 * 
 * @author taedium
 */
public class SqlLogRegistryImpl implements SqlLogRegistry {

    private static final int DEFAULT_LIMIT_SIZE = 3;

    private int limitSize;

    private ThreadLocal<LinkedList<SqlLog>> threadList = new ThreadLocal<>();

    /**
     * デフォルトの上限サイズを使用してインスタンスを構築します。
     * 
     */
    public SqlLogRegistryImpl() {
        this(DEFAULT_LIMIT_SIZE);
    }

    /**
     * 上限サイズを指定してインスタンスを構築します。
     * 
     * @param limitSize
     *            上限サイズ
     */
    public SqlLogRegistryImpl(int limitSize) {
        this.limitSize = limitSize;
    }

    @Override
    public int getLimitSize() {
        return limitSize;
    }

    @Override
    public int getSize() {
        return getSqlLogList().size();
    }

    @Override
    public boolean isEmpty() {
        return getSize() == 0;
    }

    @Override
    public SqlLog get(int index) {
        return getSqlLogList().get(index);
    }

    @Override
    public SqlLog getLast() {
        return isEmpty() ? null : (SqlLog) getSqlLogList().getLast();
    }

    @Override
    public void add(SqlLog sqlLog) {
        if (limitSize <= 0) {
            return;
        }
        LinkedList<SqlLog> list = getSqlLogList();
        list.add(sqlLog);
        if (list.size() > limitSize) {
            list.removeFirst();
        }
    }

    @Override
    public void clear() {
        getSqlLogList().clear();
    }

    private LinkedList<SqlLog> getSqlLogList() {
        LinkedList<SqlLog> list = threadList.get();
        if (list == null) {
            list = new LinkedList<>();
            threadList.set(list);
        }
        return list;
    }
}
