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
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seasar.extension.jdbc.gen.model.NamesModel;
import org.seasar.extension.jdbc.gen.model.SqlFileConstantsModel;
import org.seasar.extension.jdbc.gen.model.factory.SqlFileConstantNamingRule;
import org.seasar.extension.jdbc.gen.model.factory.SqlFileConstantsModelFactory;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.TextUtil;

/**
 * @author taedium
 * 
 */
class GenerateSqlFileConstantsTest {

    private SqlFileConstantsModelFactory factory;

    private GeneratorImplStub generator;

    /**
     * 
     * @throws Exception
     */
    @BeforeEach
    public void setUp() throws Exception {
        File classpathDir = ResourceUtil.getBuildDir(getClass());
        String basePath = getClass().getPackage().getName().replace(".", "/");
        Set<File> sqlFileSet = new HashSet<File>();
        sqlFileSet.add(ResourceUtil.getResourceAsFile(basePath + "/aaa.sql"));
        sqlFileSet.add(ResourceUtil.getResourceAsFile(basePath + "/bbb.sql"));
        factory = new SqlFileConstantsModelFactory(classpathDir, sqlFileSet, new SqlFileConstantNamingRule(), "hoge", "SqlFileTest");
        generator = new GeneratorImplStub();
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    void test() throws Exception {
        NamesModel namesModel = new NamesModel();
        namesModel.setEntityClassName("entity.Employee");
        namesModel.setShortEntityClassName("Employee");
        namesModel.setShortClassName("EmployeeNames");
        namesModel.setShortInnerClassName("_EmployeeNames");
        NamesModel namesModel2 = new NamesModel();
        namesModel2.setEntityClassName("entity.Department");
        namesModel2.setShortEntityClassName("Department");
        namesModel2.setShortClassName("DepartmentNames");
        namesModel2.setShortInnerClassName("_Department");
        SqlFileConstantsModel model = factory.getSqlFileConstantsModel();
        GenerationContext context = new GenerationContext(model, new File("file"), "java/sqlfileconstants.ftl", "UTF-8", false);
        generator.generate(context);
        String path = getClass().getName().replace(".", "/") + ".txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }
}
