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
package org.seasar.extension.jdbc.gen.sql;

import java.io.File;
import org.junit.jupiter.api.Test;
import org.seasar.extension.jdbc.gen.desc.DatabaseDesc;
import org.seasar.extension.jdbc.gen.desc.TableDesc;
import org.seasar.extension.jdbc.gen.dialect.StandardGenDialect;
import org.seasar.extension.jdbc.gen.sql.SqlFileExecutor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author taedium
 * 
 */
class SqlFileExecutorTest {

    /**
     * 
     * @throws Exception
     */
    @Test
    void testIsTarget() throws Exception {
        SqlFileExecutor executor = new SqlFileExecutor(new StandardGenDialect(), "UTF-8", ';', null);
        TableDesc tableDesc = new TableDesc();
        tableDesc.setCanonicalName("aaa.bbb.ccc");
        DatabaseDesc databaseDesc = new DatabaseDesc();
        databaseDesc.addTableDesc(tableDesc);
        File match = new File("aaa.bbb.ccc.sql");
        File unmatch = new File("xxx.sql");
        assertTrue(executor.isTarget(databaseDesc, match));
        assertTrue(executor.isTarget(databaseDesc, unmatch));
        databaseDesc.setFiltered(true);
        assertTrue(executor.isTarget(databaseDesc, match));
        assertFalse(executor.isTarget(databaseDesc, unmatch));
    }
}
