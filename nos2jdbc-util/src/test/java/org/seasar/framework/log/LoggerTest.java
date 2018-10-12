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
package org.seasar.framework.log;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author higa
 *
 */
class LoggerTest {

    private Logger _logger = Logger.getLogger(getClass());

    /**
     * @throws Exception
     */
    @Test
    void testGetLogger() throws Exception {
        assertSame(Logger.getLogger(getClass()), Logger.getLogger(getClass()), "1");
    }

    /**
     * @throws Exception
     */
    @Test
    void testDebug() throws Exception {
        _logger.debug("debug");
    }

    /**
     * @throws Exception
     */
    @Test
    void testInfo() throws Exception {
        _logger.info("info");
    }

    /**
     * @throws Exception
     */
    @Test
    void testWarn() throws Exception {
        _logger.warn("warn");
    }

    /**
     * @throws Exception
     */
    @Test
    void testError() throws Exception {
        _logger.error("error");
    }

    /**
     * @throws Exception
     */
    @Test
    void testFatal() throws Exception {
        _logger.fatal("fatal");
    }

    /**
     * @throws Exception
     */
    @Test
    void testLog() throws Exception {
        _logger.log("ISSR0001", null);
    }

    /**
     * @throws Exception
     */
    @Test
    void testPerformance() throws Exception {
        final int num = 100;
        long start = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            System.out.println("test" + i);
        }
        long sysout = System.currentTimeMillis() - start;
        start = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            _logger.fatal("test" + i);
        }
        long logger = System.currentTimeMillis() - start;
        System.out.println("System.out:" + sysout);
        System.out.println("logger:" + logger);
    }
}
