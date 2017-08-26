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
package org.seasar.extension.jdbc.gen.internal.model;

import java.util.List;

import org.junit.Test;
import org.seasar.extension.jdbc.gen.model.GeneratedModel;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class GeneratedModelSupportTest {

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testFillGeneratedInfo() throws Exception {
        Factory factory = new Factory();
        Model model = new Model();
        GeneratedModelSupport support = new GeneratedModelSupport();
        support.fillGeneratedInfo(factory, model);
        List<String> infoList = model.getGeneratedInfoList();
        assertEquals(2, infoList.size());
        assertEquals("S2JDBC-Gen test-0.0.1", infoList.get(0));
        assertEquals(Factory.class.getName(), infoList.get(1));
    }

    private static class Factory {
    }

    private static class Model extends GeneratedModel {
    }
}
