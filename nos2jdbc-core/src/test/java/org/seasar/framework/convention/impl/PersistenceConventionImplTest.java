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
package org.seasar.framework.convention.impl;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author higa
 * 
 */
class PersistenceConventionImplTest {

    private PersistenceConventionImpl convention = new PersistenceConventionImpl();

    @BeforeEach
    void setUp() throws Exception {
        ;
    }

    /**
     * Test method for
     * {@link org.seasar.framework.convention.impl.PersistenceConventionImpl#fromTableNameToEntityName(java.lang.String)}.
     */
    @Test
    void testFromTableNameToEntityName() {
        assertEquals("Emp", convention.fromTableNameToEntityName("EMP"));
        assertEquals("AaaBbb", convention.fromTableNameToEntityName("AAA_BBB"));
    }

    /**
     * Test method for
     * {@link org.seasar.framework.convention.impl.PersistenceConventionImpl#fromEntityNameToTableName(java.lang.String)}.
     */
    @Test
    void testFromEntityNameToTableName() {
        assertEquals("EMP", convention.fromEntityNameToTableName("Emp"));
        assertEquals("AAA_BBB", convention.fromEntityNameToTableName("AaaBbb"));
    }

    /**
     * Test method for
     * {@link org.seasar.framework.convention.impl.PersistenceConventionImpl#fromColumnNameToPropertyName(java.lang.String)}.
     */
    @Test
    void testFromColumnNameToPropertyName() {
        assertEquals("aaa", convention.fromColumnNameToPropertyName("AAA"));
        assertEquals("aaaBbb", convention
                .fromColumnNameToPropertyName("AAA_BBB"));
    }

    /**
     * Test method for
     * {@link org.seasar.framework.convention.impl.PersistenceConventionImpl#fromPropertyNameToColumnName(java.lang.String)}.
     */
    @Test
    void testFromPropertyNameToColumnName() {
        assertEquals("AAA", convention.fromPropertyNameToColumnName("aaa"));
        assertEquals("AAA_BBB", convention
                .fromPropertyNameToColumnName("aaaBbb"));
    }
}