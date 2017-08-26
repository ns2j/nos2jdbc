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
package org.seasar.extension.jdbc.exception;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.entity.Aaa;

/**
 * @author higa
 * 
 */
public class MappedByPropertyNotFoundRuntimeExceptionTest extends TestCase {

    /**
     * 
     */
    public void testGetMessage() {
        MappedByPropertyNotFoundRuntimeException ex = new MappedByPropertyNotFoundRuntimeException(
                "Department", "employees", "department", Aaa.class);
        System.out.println(ex.getMessage());
        assertEquals("Department", ex.getEntityName());
        assertEquals("employees", ex.getPropertyName());
        assertEquals("department", ex.getMappedBy());
        assertEquals(Aaa.class, ex.getInverseRelationshipClass());
    }
}