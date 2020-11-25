/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * and/or LICENSE file distributed with this work for additional
 * information regarding copyright ownership.  The ASF licenses
 * this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.seasar.framework.util;

import java.lang.reflect.AccessibleObject;

import ognl.AccessibleObjectHandler;

//FROM ognl package
/**
 * Utilizes a standard pre-JDK 9 reflection mechanism for changing the accessibility level of
 *   a given AccessibleObject.
 *
 * @since 3.1.24
 */
class AccessibleObjectHandlerPreJDK9 implements AccessibleObjectHandler
{

    /**
     * Private constructor
     */
    private AccessibleObjectHandlerPreJDK9() {}

    /**
     * Package-level generator of an AccessibleObjectHandlerJDK9Plus instance.
     *
     * Not intended for use outside of the package.
     *
     * @return an AccessibleObjectHandler instance
     *
     * @since 3.1.24
     */
    static AccessibleObjectHandler createHandler() {
        return new AccessibleObjectHandlerPreJDK9();
    }

    /**
     * Utilize accessibility modification mechanism for JDK 8 (Java Major Version 8) and earlier.
     *   It is also the default modification mechanism for JDK 9+.
     *
     * @param accessibleObject the AccessibleObject upon which to apply the flag.
     * @param flag the new accessible flag value.
     */
    @Override
    public void setAccessible(AccessibleObject accessibleObject, boolean flag) {
        accessibleObject.setAccessible(flag);
    }

}
