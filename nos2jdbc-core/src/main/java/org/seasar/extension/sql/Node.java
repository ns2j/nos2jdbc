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
 * <code>SQL</code>を構成する個々の要素をあらわします。
 * 
 * @author higa
 * 
 */
public interface Node {

    /**
     * 子供の要素数を返します。
     * 
     * @return 要素数
     */
    int getChildSize();

    /**
     * 指定されたインデックス番号<code>index</code>の<code>Node</code>を返します。
     * 
     * @param index int
     * @return Node
     */
    Node getChild(int index);

    /**
     * 子供の<code>Node</code>を追加します。
     * 
     * @param node Node
     */
    void addChild(Node node);

    /**
     * <code>CommandContext</code>をこの<code>Node</code>に、 適用します。
     * 
     * @param ctx SqlContext
     */
    void accept(SqlContext ctx);
}