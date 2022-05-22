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
package org.seasar.extension.jdbc.gen.internal.version;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.seasar.extension.jdbc.gen.internal.exception.IllegalDdlInfoVersionRuntimeException;
import org.seasar.extension.jdbc.gen.internal.exception.NextVersionExceededRuntimeException;
import org.seasar.framework.util.ResourceUtil;

/**
 * @author taedium
 * 
 */
class DdlInfoFileImplTest {

    /**
     * 
     */
    @Test
    void testGetVersionNo() {
        String path = getClass().getName().replace('.', '/') + "_version.txt";
        File file = ResourceUtil.getResourceAsFile(path);
        DdlInfoFileImpl ddlInfoFile = new DdlInfoFileImpl(file);
        assertEquals(10, ddlInfoFile.getCurrentVersionNo());
        assertEquals(10, ddlInfoFile.getCurrentVersionNo());
    }

    /**
     * 
     */
    @Test
    void testGetVersionNo_fileNotExistent() {
        DdlInfoFileImpl ddlInfoFile = new DdlInfoFileImpl(new File("notExistent"));
        assertEquals(0, ddlInfoFile.getCurrentVersionNo());
    }

    /**
     * 
     */
    @Test
    void testGetVersionNo_illegalVersionNoFormat() {
        String fileName = getClass().getName().replace('.', '/') + "_illegalVersion.txt";
        File file = ResourceUtil.getResourceAsFile(fileName);
        DdlInfoFileImpl ddlInfoFile = new DdlInfoFileImpl(file);
        try {
            ddlInfoFile.getCurrentVersionNo();
            fail();
        } catch (IllegalDdlInfoVersionRuntimeException expected) {
        }
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    void testGetNextVersion_maxVersionNo() throws Exception {
        String path = getClass().getName().replace('.', '/') + "_maxVersion.txt";
        File file = ResourceUtil.getResourceAsFile(path);
        DdlInfoFileImpl ddlInfoFile = new DdlInfoFileImpl(file);
        try {
            ddlInfoFile.getNextVersionNo();
            fail();
        } catch (NextVersionExceededRuntimeException expected) {
        }
    }
}
