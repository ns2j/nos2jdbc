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
package org.seasar.extension.jdbc.gen.sqltype;

import java.sql.Connection;
import java.sql.PreparedStatement;
import org.junit.jupiter.api.Test;
import org.seasar.extension.jdbc.gen.sqltype.BinaryType;
import org.seasar.framework.mock.sql.MockConnection;

/**
 * @author taedium
 * 
 */
class BinaryTypeTest {

    /**
     * 
     * @throws Exception
     */
    @Test
    void testBindValue_empty() throws Exception {
        Connection conn = new MockConnection();
        PreparedStatement ps = conn.prepareStatement("select * from a where b = ?");
        BinaryType binaryType = new BinaryType();
        binaryType.bindValue(ps, 1, "");
    }
}
