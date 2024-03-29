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
package org.seasar.extension.jdbc.gen.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seasar.extension.jdbc.gen.data.Dumper;
import org.seasar.extension.jdbc.gen.desc.PrimaryKeyDesc;
import org.seasar.extension.jdbc.gen.desc.TableDesc;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.dialect.StandardGenDialect;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author taedium
 * 
 */
class DumperImplTest {

    private GenDialect dialect;

    private Dumper dumper;

    /**
     * 
     */
    @BeforeEach
    public void setUp() {
        dialect = new StandardGenDialect();
        dumper = new Dumper(dialect, "UTF-8");
    }

    /**
     * 
     */
    @Test
    void testBuildSqlWithOrderbyId() {
        PrimaryKeyDesc primaryKeyDesc = new PrimaryKeyDesc();
        primaryKeyDesc.addColumnName("ID1");
        primaryKeyDesc.addColumnName("ID2");
        TableDesc tableDesc = new TableDesc();
        tableDesc.setCatalogName("AAA");
        tableDesc.setSchemaName("BBB");
        tableDesc.setName("HOGE");
        tableDesc.setPrimaryKeyDesc(primaryKeyDesc);
        assertEquals("select * from AAA.BBB.HOGE order by ID1, ID2", dumper.buildSqlWithSort(tableDesc));
    }
}
