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
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seasar.extension.jdbc.gen.model.SqlFileTestModel;
import org.seasar.extension.jdbc.gen.model.factory.SqlFileTestModelFactory;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.TextUtil;

/**
 * @author taedium
 * 
 */
class GenerateSqlFileTestTest {

    private GeneratorImplStub generator;

    /**
     * 
     * @throws Exception
     */
    @BeforeEach
    public void setUp() throws Exception {
        generator = new GeneratorImplStub();
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    void testSqlFileSet() throws Exception {
        File classpathDir = ResourceUtil.getBuildDir(getClass());
        String basePath = getClass().getPackage().getName().replace(".", "/");
        Set<File> sqlFileSet = new HashSet<File>();
        sqlFileSet.add(ResourceUtil.getResourceAsFile(basePath + "/aaa.sql"));
        sqlFileSet.add(ResourceUtil.getResourceAsFile(basePath + "/bbb.sql"));
        SqlFileTestModelFactory sqlFileTestModelFactory = new SqlFileTestModelFactory(classpathDir, sqlFileSet, "jdbcManager", "hoge", "SqlFileTest", "rootpackagename", "none");
        SqlFileTestModel model = sqlFileTestModelFactory.getSqlFileTestModel();
        GenerationContext context = new GenerationContext(model, new File("file"), "java/sqlfiletest.ftl", "UTF-8", false);
        generator.generate(context);
        String path = getClass().getName().replace(".", "/") + "_SqlFileSet.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

    /**
     * 
     * @throws Exception
     */
    /*i    
    @Test
    void testSqlFileSet_s2junit4() throws Exception {
        File classpathDir = ResourceUtil.getBuildDir(getClass());
        String basePath = getClass().getPackage().getName().replace(".", "/");
        Set<File> sqlFileSet = new HashSet<File>();
        sqlFileSet.add(ResourceUtil.getResourceAsFile(basePath + "/aaa.sql"));
        sqlFileSet.add(ResourceUtil.getResourceAsFile(basePath + "/bbb.sql"));
        SqlFileTestModelFactoryImpl sqlFileTestModelFactory = new SqlFileTestModelFactoryImpl(
                classpathDir, sqlFileSet, "jdbcManager",
                "hoge", "SqlFileTest", "rootpackagename", "none", "");
        SqlFileTestModel model = sqlFileTestModelFactory.getSqlFileTestModel();

        GenerationContextImpl context = new GenerationContextImpl(model, new File(
                "file"), "java/sqlfiletest.ftl", "UTF-8", false);
        generator.generate(context);

        String path = getClass().getName().replace(".", "/")
                + "_SqlFileSet_s2junit4.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }
*/
    /**
     * 
     * @throws Exception
     */
    @Test
    void testNoSqlFile() throws Exception {
        File classpathDir = ResourceUtil.getBuildDir(getClass());
        SqlFileTestModelFactory sqlFileTestModelFactory = new SqlFileTestModelFactory(classpathDir, Collections.<File>emptySet(), "jdbcManager", "hoge", "SqlFileTest", "rootpackagename", "none");
        SqlFileTestModel model = sqlFileTestModelFactory.getSqlFileTestModel();
        GenerationContext context = new GenerationContext(model, new File("file"), "java/sqlfiletest.ftl", "UTF-8", false);
        generator.generate(context);
        String path = getClass().getName().replace(".", "/") + "_NoSqlFile.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }
}
