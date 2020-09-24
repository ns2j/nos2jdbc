/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.v
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
package nos2jdbc.unit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.opentest4j.AssertionFailedError;

import org.seasar.extension.dataset.ColumnType;
import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.unit.BeanListReader;
import org.seasar.extension.unit.BeanReader;
import org.seasar.extension.unit.MapListReader;
import org.seasar.extension.unit.MapReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

public abstract class DataSetAssertions {
    final static private Logger logger = LoggerFactory.getLogger(DataSetAssertions.class);
    
    /**
     * 等しいことを表明します。
     * 
     * @param expected
     *            期待値
     * @param actual
     *            実際の値
     */
    static public void assertDsEquals(DataSet expected, DataSet actual) {
        assertDsEquals(expected, actual, null);
    }

    /**
     * 等しいことを表明します。
     * 
     * @param expected
     *            期待値
     * @param actual
     *            実際の値
     * @param message
     *            メッセージ
     */
    static public void assertDsEquals(DataSet expected, DataSet actual, String message) {
        message = message == null ? "" : message;
        assertEquals(expected.getTableSize(), actual.getTableSize(), message + ":TableSize");
        for (int i = 0; i < expected.getTableSize(); ++i) {
            assertDsEquals(expected.getTable(i), actual.getTable(i), message);
        }
    }

    static public void assertDsContainsAll(DataSet expected, DataSet actual) {
        assertDsContainsAll(expected, actual, null);
    }
    static public void assertDsContainsAll(DataSet expected, DataSet actual, String message) {
        message = message == null ? "" : message;
        assertEquals(expected.getTableSize(), actual.getTableSize(), message + ":TableSize");

        for (int i = 0; i < expected.getTableSize(); ++i) {
            assertDsContainsAll(expected, expected.getTable(i).getTableName(), actual.getTable(i), message);
        }
    }

    static protected List<String> rowEquals(DataTable expected, int expectedRowNum, DataRow actualRow) {
        DataRow expectedRow = expected.getRow(expectedRowNum);
        List<String> errorMessages = new ArrayList<>();
        for (int i = 0; i < expected.getColumnSize(); ++i) {
            try {
                String columnName = expected.getColumnName(i);
                Object expectedValue = expectedRow.getValue(columnName);
                ColumnType ct = expected.getColumnType(i);
                Object actualValue = actualRow.getValue(columnName);
                if (!ct.equals(expectedValue, actualValue)) {
                    assertEquals(expectedValue, actualValue, 
                            ":columnName=" + columnName);
                }
            } catch (AssertionFailedError e) {
                errorMessages.add(e.getMessage());
            }
        }
        return errorMessages;
    }

    /**
     * 等しいことを表明します。
     * 
     * @param expected
     *            期待値
     * @param actual
     *            実際の値
     */
    static public void assertDsEquals(DataTable expected, DataTable actual) {
        assertDsEquals(expected, actual, null);
    }

    /**
     * 等しいことを表明します。
     * 
     * @param expected
     *            期待値
     * @param actual
     *            実際の値
     * @param message
     *            メッセージ
     */
    static public void assertDsEquals(DataTable expected,
            DataTable actual, String message) {
        message = message == null ? "" : message;
        message = message + ":TableName=" + expected.getTableName(); 

        assertEquals(expected.getRowSize(), actual.getRowSize(), message + ":RowSize");
        
        Map<Integer, List<String>> errorMessageMap = new TreeMap<>();
        for (int i = 0; i < expected.getRowSize(); ++i) {
            List<String> errorMessages = rowEquals(expected, i, actual.getRow(i));
            if (!errorMessages.isEmpty())
                errorMessageMap.put(i, errorMessages); 
        }
        StringBuilder sb = new StringBuilder();
        if (!errorMessageMap.isEmpty()) {
            errorMessageMap.forEach((r, m) -> sb.append(":Row=" + r + m)); 
            fail(message + sb);
        }
    }

    static public void assertDsEquals(DataSet expected,
            String dataTableName, Object actual) {
        assertDsEquals(expected, dataTableName, actual, null);
    }

    static public void assertDsEquals(DataSet expected, String dataTableName,
            Object obj, String message) {
        message = message == null ? "" : message;
        message = message + ":TableName=" + dataTableName;

        if (expected.hasTable(dataTableName) && obj == null)
            fail("expected data table exists, but actual is null. data table name=" + dataTableName);
        else if (!expected.hasTable(dataTableName) && obj != null)
            fail("expected data table does not exist, but actual is not null. data table name=" + dataTableName);
        else if (!expected.hasTable(dataTableName) && obj == null)
            return;

        BeanReader reader = new BeanReader(obj);
        DataTable actual = reader.readTable();

        DataTable expectedTable = expected.getTable(dataTableName);
        assertEquals(expectedTable.getRowSize(), actual
                .getRowSize(), message + ":RowSize");

        List<String> errorMessages
        = rowEquals(expectedTable, 0, actual.getRow(0));
        if (!errorMessages.isEmpty()) {
            fail(message + errorMessages);
        }
    }

    static public void assertDsEquals(DataSet expected,
            String dataTableName, List<?> actual) {
        assertDsEquals(expected, dataTableName, actual, null);
    }

    static public void assertDsEquals(DataSet expected, String dataTableName,
            List<?> list, String message) {
        message = message == null ? "" : message;

        if (expected.hasTable(dataTableName) && (list == null || list.isEmpty()))
            fail("expected data table exists, but actual is empty. data table name=" + dataTableName);
        else if (!expected.hasTable(dataTableName) && (list != null && !list.isEmpty()))
            fail("expected data table does not exist, but actual is not empty. data table name=" + dataTableName);
        else if (!expected.hasTable(dataTableName) && (list == null || list.isEmpty()))
            return;

        BeanListReader reader = new BeanListReader(list);
        DataTable actual = reader.readTable();
        DataTable expectedTable = expected.getTable(dataTableName);
        assertEquals(expectedTable.getRowSize(), actual.getRowSize(),
                message + ":TableName=" + dataTableName + ":RowSize");
        assertDsEquals(expectedTable, actual, message);
    }

    static protected boolean contains(DataTable expected, int expectedRowNum, DataTable actual) {
        for (int i = 0; i < actual.getRowSize(); ++i) {
            if (rowEquals(expected, expectedRowNum, actual.getRow(i)).isEmpty())
                return true;
        }
        return false;
    }
    static public void assertDsContainsAll(DataSet expected, String dataTableName, DataTable actual) {
        assertDsContainsAll(expected, dataTableName, actual, null);
    }

    static public void assertDsContainsAll(DataSet expected, String dataTableName, DataTable actual, String message) {
        message = message == null ? "" : message;
        message = message + ":TableName=" + dataTableName;

        DataTable expectedTable = expected.getTable(dataTableName);
        assertEquals(expectedTable.getRowSize(), actual.getRowSize(), message + ":RowSize");

        List<String> errorMessages = new ArrayList<>();
        for (int i = 0; i < expectedTable.getRowSize(); ++i) {
            DataRow expectedRow = expectedTable.getRow(i);
            if (!contains(expectedTable, i, actual)) {
                errorMessages.add(":Row=" + i + expectedRow);
            }
        }
        if (!errorMessages.isEmpty()) {
            fail("not contains " + message + errorMessages);
        }
    }

    static public void assertDsContainsAll(DataSet expected, String dataTableName, List<?> list) {
        assertDsContainsAll(expected, dataTableName, list, null);
    }

    static public void assertDsContainsAll(DataSet expected, String dataTableName, List<?> list, String message) {
        if (expected.hasTable(dataTableName) && (list == null || list.isEmpty()))
            fail("expected data table exists, but actual is empty. data table name=" + dataTableName);
        else if (!expected.hasTable(dataTableName) && (list != null && !list.isEmpty()))
            fail("expected data table does not exist, but actual is not empty. data table name=" + dataTableName);
        else if (!expected.hasTable(dataTableName) && (list == null || list.isEmpty()))
            return;

        BeanListReader reader = new BeanListReader(list);
        DataTable actual = reader.readTable();
        assertDsContainsAll(expected, dataTableName, actual, message);
    }


    /**
     * 等しいことを表明します。
     * 
     * @param expected
     *            期待値
     * @param actual
     *            実際の値
     */
    static public void assertDsEquals(DataSet expected, Object actual) {
        assertDsEquals(expected, actual, null);
    }

    /**
     * 等しいことを表明します。
     * 
     * @param expected
     *            期待値
     * @param actual
     *            実際の値
     * @param message
     *            メッセージ
     */
    static public void assertDsEquals(DataSet expected, Object actual, String message) {
        if (expected == null || actual == null) {
            assertEquals(expected, actual, message);
            return;
        }
        if (actual instanceof List<?>) {
            List<?> actualList = (List<?>) actual;
            assertFalse(actualList.isEmpty());
            Object actualItem = actualList.get(0);
            if (actualItem instanceof Map<?, ?>) {
                assertMapListEquals(expected, (List<Map<String, Object>>)actualList, message);
            } else {
                assertBeanListEquals(expected, actualList, message);
            }
        } else if (actual instanceof Object[]) {
            assertDsEquals(expected, Arrays.asList((Object[]) actual), message);
        } else {
            if (actual instanceof Map) {
                assertMapEquals(expected, (Map<String, Object>) actual, message);
            } else {
                assertBeanEquals(expected, actual, message);
            }
        }
    }

    /**
     * 等しいことを表明します。
     * 
     * @param expected
     *            期待値
     * @param map
     *            実際の値
     * @param message
     *            メッセージ
     */
    static protected void assertMapEquals(DataSet expected, Map<String, Object> map, String message) {

        MapReader reader = new MapReader(map);
        assertDsEquals(expected, reader.read(), message);
    }

    /**
     * 等しいことを表明します。
     * 
     * @param expected
     *            期待値
     * @param list
     *            実際の値
     * @param message
     *            メッセージ
     */
    static protected void assertMapListEquals(DataSet expected,
            List<Map<String, Object>> list, String message) {

        MapListReader reader = new MapListReader(list);
        assertDsEquals(expected, reader.read(), message);
    }

    /**
     * 等しいことを表明します。
     * 
     * @param expected
     *            期待値
     * @param bean
     *            実際の値
     * @param message
     *            メッセージ
     */
    static protected void assertBeanEquals(DataSet expected,
            Object bean, String message) {

        BeanReader reader = new BeanReader(bean);
        assertDsEquals(expected, reader.read(), message);
    }

    /**
     * 等しいことを表明します。
     * 
     * @param expected
     *            期待値
     * @param list
     *            実際の値
     * @param message
     *            メッセージ
     */
    static protected void assertBeanListEquals(DataSet expected,
            List<?> list, String message) {

        BeanListReader reader = new BeanListReader(list);
        assertDsEquals(expected, reader.read(), message);
    }


}