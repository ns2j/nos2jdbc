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
package org.seasar.extension.jdbc.gen.generator;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seasar.extension.jdbc.gen.desc.TableDesc;
import org.seasar.extension.jdbc.gen.desc.UniqueKeyDesc;
import org.seasar.extension.jdbc.gen.dialect.StandardGenDialect;
import org.seasar.extension.jdbc.gen.model.SqlIdentifierCaseType;
import org.seasar.extension.jdbc.gen.model.SqlKeywordCaseType;
import org.seasar.extension.jdbc.gen.model.TableModel;
import org.seasar.extension.jdbc.gen.model.factory.TableModelFactory;
import org.seasar.framework.mock.sql.MockConnection;
import org.seasar.framework.mock.sql.MockDataSource;
import org.seasar.framework.mock.sql.MockPreparedStatement;
import org.seasar.framework.mock.sql.MockResultSet;
import org.seasar.framework.util.ArrayMap;
import org.seasar.framework.util.TextUtil;

/**
 * @author taedium
 * 
 */
class GenerateUniqueKeyTest {

    private GeneratorImplStub generator;

    private DataSource dataSource;

    private TableModel model;

    /**
     * 
     * @throws Exception
     */
    @BeforeEach
    public void setUp() throws Exception {
        generator = new GeneratorImplStub();
        dataSource = new MockDataSource() {

            @Override
            public Connection getConnection() throws SQLException {
                return new MockConnection() {

                    @Override
                    public MockPreparedStatement prepareMockStatement(String sql) {
                        return new MockPreparedStatement(this, sql) {

                            @Override
                            public ResultSet executeQuery() throws SQLException {
                                MockResultSet resultSet = new MockResultSet();
                                ArrayMap map = new ArrayMap();
                                map.put(null, 200);
                                resultSet.addRowData(map);
                                return resultSet;
                            }
                        };
                    }
                };
            }
        };
        UniqueKeyDesc uniqueKeyDesc = new UniqueKeyDesc();
        uniqueKeyDesc.addColumnName("UK1-1");
        uniqueKeyDesc.addColumnName("UK1-2");
        UniqueKeyDesc uniqueKeyDesc2 = new UniqueKeyDesc();
        uniqueKeyDesc2.addColumnName("UK2-1");
        uniqueKeyDesc2.addColumnName("UK2-2");
        TableDesc tableDesc = new TableDesc();
        tableDesc.setCatalogName("AAA");
        tableDesc.setSchemaName("BBB");
        tableDesc.setName("HOGE");
        tableDesc.setCanonicalName("aaa.bbb.hoge");
        tableDesc.addUniqueKeyDesc(uniqueKeyDesc);
        tableDesc.addUniqueKeyDesc(uniqueKeyDesc2);
        TableModelFactory factory = new TableModelFactory(new StandardGenDialect(), dataSource, SqlIdentifierCaseType.ORIGINALCASE, SqlKeywordCaseType.ORIGINALCASE, ';', null, false) {

            @Override
            protected Long getNextValue(String sequenceName, int allocationSize) {
                return null;
            }
        };
        model = factory.getTableModel(tableDesc);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    void testCreate() throws Exception {
        GenerationContext context = new GenerationContext(model, new File("file"), "sql/create-uniquekey.ftl", "UTF-8", false);
        generator.generate(context);
        String path = getClass().getName().replace(".", "/") + "_Create.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    void testDrop() throws Exception {
        GenerationContext context = new GenerationContext(model, new File("file"), "sql/drop-uniquekey.ftl", "UTF-8", false);
        generator.generate(context);
        String path = getClass().getName().replace(".", "/") + "_Drop.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }
}
