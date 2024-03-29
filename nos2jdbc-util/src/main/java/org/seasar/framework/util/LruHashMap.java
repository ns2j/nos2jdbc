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
package org.seasar.framework.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LRU用の {@link HashMap}です。
 * 
 * @author koichik
 * 
 */
public class LruHashMap extends LinkedHashMap<Object, Object> {

    private static final long serialVersionUID = 1L;

    /**
     * デフォルトの初期容量です。
     */
    protected static final int DEFAULT_INITIAL_CAPACITY = 16;

    /**
     * デフォルトのロードファクタです。
     */
    protected static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
     * 上限サイズです。
     */
    protected int limitSize;

    /**
     * {@link LruHashMap}を作成します。
     * 
     * @param limitSize limitSize
     */
    public LruHashMap(final int limitSize) {
        this(limitSize, DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    /**
     * {@link LruHashMap}を作成します。
     * 
     * @param limitSize limitSize
     * @param initialCapacity initialCapacity
     * @param loadFactor loadFactor
     */
    public LruHashMap(final int limitSize, final int initialCapacity,
            final float loadFactor) {
        super(initialCapacity, loadFactor, true);
        this.limitSize = limitSize;
    }

    /**
     * 上限サイズを返します。
     * 
     * @return 上限サイズ
     */
    public int getLimitSize() {
        return limitSize;
    }

    @Override
    protected boolean removeEldestEntry(final Map.Entry<Object, Object> entry) {
        return size() > limitSize;
    }
}