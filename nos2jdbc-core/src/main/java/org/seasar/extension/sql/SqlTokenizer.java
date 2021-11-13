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
package org.seasar.extension.sql;

/**
 * SQLをトークンに分解するクラスです。
 * 
 * @author higa
 * 
 */
public interface SqlTokenizer {

    /**
     * SQL
     */
    int SQL = 1;

    /**
     * COMMENT
     */
    int COMMENT = 2;

    /**
     * ELSE
     */
    int ELSE = 3;

    /**
     * BIND_VARIABLE
     */
    int BIND_VARIABLE = 4;

    /**
     * EOF
     */
    int EOF = 99;

    /**
     * トークンを返します。
     * 
     * @return String
     */
    String getToken();

    /**
     * SQLを返します。
     * 
     * @return String
     */
    String getSql();

    /**
     * 現在解析しているポジションより前のSQLを返します。
     * 
     * @return String
     */
    String getBefore();

    /**
     * 現在解析しているポジションより後ろのSQLを返します。
     * 
     * @return String
     */
    String getAfter();

    /**
     * 現在解析しているポジションを返します。
     * 
     * @return String
     */
    int getPosition();

    /**
     * 現在のトークン種別を返します。
     * 
     * @return int
     */
    int getTokenType();

    /**
     * 次のトークン種別を返します。
     * 
     * @return int
     */
    int getNextTokenType();

    /**
     * 次のトークンに進みます。
     * 
     * @return int
     */
    int next();

    /**
     * トークンをスキップします。
     * 
     * @return スキップしたトークン
     */
    String skipToken();

    /**
     * ホワイトスペースをスキップします。
     * 
     * @return String
     */
    String skipWhitespace();
}