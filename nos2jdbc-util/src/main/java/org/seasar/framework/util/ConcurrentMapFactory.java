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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.seasar.framework.util.MapUtil.MapFactory;

/**
 * {@link java.util.concurrent.ConcurrentHashMap}を作成するファクトリの実装です。
 * 
 * @author koichik
 */
public class ConcurrentMapFactory implements MapFactory {

    @Override
    public Map<Object, Object> create() {
        return new ConcurrentHashMap<Object, Object>();
    }

    @Override
    public Map<Object, Object> create(final int initialCapacity) {
        return new ConcurrentHashMap<Object, Object>(initialCapacity);
    }

    @Override
    public Map<Object, Object> create(final int initialCapacity, final float loadFactor) {
        return new ConcurrentHashMap<Object, Object>(initialCapacity, loadFactor,
                initialCapacity);
    }

}
