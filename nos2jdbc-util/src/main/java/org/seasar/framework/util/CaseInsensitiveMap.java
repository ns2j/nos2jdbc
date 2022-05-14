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

import java.util.Iterator;
import java.util.Map;

/**
 * キーで大文字小文字を気にしない {@link ArrayMap}です。
 * 
 * @author higa
 * 
 */
public class CaseInsensitiveMap<V> extends ArrayMap<String, V> {

    private static final long serialVersionUID = 1L;

    /**
     * {@link CaseInsensitiveMap}を作成します。
     */
    public CaseInsensitiveMap() {
        super();
    }

    /**
     * {@link CaseInsensitiveMap}を作成します。
     * 
     * @param capacity capacity
     */
    public CaseInsensitiveMap(int capacity) {
        super(capacity);
    }

    /**
     * キーが含まれているかどうかを返します。
     * 
     * @param key key
     * @return キーが含まれているかどうか
     */
    public final boolean containsKey(String key) {
        return super.containsKey(convertKey(key));
    }

    @Override
    public final V get(Object key) {
        return super.get(convertKey(key));
    }

    @Override
    public final V put(String key, V value) {
        return super.put(convertKey(key), value);
    }

    @Override
    public final void putAll(Map<? extends String, ? extends V> map) {
        for (@SuppressWarnings("rawtypes")
        Iterator i = map.entrySet().iterator(); i.hasNext();) {
            @SuppressWarnings("unchecked")
            Map.Entry<String, V> entry = (Map.Entry<String, V>)i.next();
            put(convertKey(entry.getKey()), entry.getValue());
        }
    }

    @Override
    public final V remove(Object key) {
        return super.remove(convertKey(key));
    }
    @Override
    public boolean containsKey(Object key) {
        return super.containsKey(convertKey(key));
    }

    private static String convertKey(Object key) {
        return ((String) key).toLowerCase();
    }

}
