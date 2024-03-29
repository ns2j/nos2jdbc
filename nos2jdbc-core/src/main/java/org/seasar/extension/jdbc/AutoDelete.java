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
package org.seasar.extension.jdbc;

import jakarta.persistence.OptimisticLockException;

/**
 * SQLを自動生成する削除です。
 * 
 * @author koichik
 * @param <T>
 *            エンティティの型です。
 */
public interface AutoDelete<T> extends Update<AutoDelete<T>> {

    /**
     * バージョンプロパティを無視して削除します。
     * 
     * @return このインスタンス自身
     */
    AutoDelete<T> ignoreVersion();

    /**
     * バージョンチェックを行った場合に、 更新行数が0行でも{@link OptimisticLockException}をスローしないようにします。
     * 
     * @return このインスタンス自身
     */
    AutoDelete<T> suppresOptimisticLockException();

}
