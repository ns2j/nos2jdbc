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
package org.seasar.extension.jdbc.gen.util;

import org.seasar.extension.jdbc.EntityMeta;

/**
 * {@link EntityMeta}に関するユーティリティクラスです。
 * 
 * @author taedium
 */
public class EntityMetaUtil {

    /** コメントのキー */
    protected static String COMMENT_KEY = EntityMetaUtil.class.getName()
            + "_comment";

    /**
     * 
     */
    protected EntityMetaUtil() {
    }

    /**
     * コメントを設定します。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     * @param comment
     *            コメント
     */
    public static void setComment(EntityMeta entityMeta, String comment) {
        entityMeta.addAdditionalInfo(COMMENT_KEY, comment);
    }

    /**
     * コメントを返します。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     * @return コメント
     */
    public static String getComment(EntityMeta entityMeta) {
        return (String) entityMeta.getAdditionalInfo(COMMENT_KEY);
    }
}
