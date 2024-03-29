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
package org.seasar.extension.jdbc.gen.meta;

import java.util.List;

import org.seasar.extension.jdbc.EntityMeta;

/**
 * {@link CommentDoclet}のためのコンテキスト情報です。
 * 
 * @author taedium
 */
public class CommentDocletContext {

    /** エンティティメタリーダのリストを格納する{@link ThreadLocal} */
    protected static ThreadLocal<List<EntityMeta>> threadLocal = new ThreadLocal<List<EntityMeta>>();

    /**
     * エンティティメタデータのリストを設定します。
     * 
     * @param entityMetaList
     *            エンティティメタデータのリスト
     */
    public static void setEntityMetaList(List<EntityMeta> entityMetaList) {
        threadLocal.set(entityMetaList);
    }

    /**
     * エンティティメタデータのリストを返します。
     * 
     * @return エンティティメタデータのリストを設定します。
     */
    public static List<EntityMeta> getEntityMetaList() {
        return threadLocal.get();
    }

}
