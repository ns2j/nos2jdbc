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
package org.seasar.extension.jdbc.exception;

import org.seasar.extension.jdbc.exception.ColumnDuplicatedRuntimeException;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author higa
 * 
 */
class ColumnDuplicatedRuntimeExceptionTest {

	/**
	 * 
	 */
	@Test
    void testAll() {
		ColumnDuplicatedRuntimeException ex = new ColumnDuplicatedRuntimeException(
				"Hoge", "aaaName", "aaaName2", "aaa_name");
		System.out.println(ex.getMessage());
		assertEquals("Hoge", ex.getEntityName());
		assertEquals("aaaName", ex.getPropertyName());
		assertEquals("aaaName2", ex.getPropertyName2());
		assertEquals("aaa_name", ex.getColumnName());
	}
}