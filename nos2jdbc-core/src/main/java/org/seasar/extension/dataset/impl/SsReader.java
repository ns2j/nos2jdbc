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
package org.seasar.extension.dataset.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.seasar.extension.dataset.ColumnType;
import org.seasar.extension.dataset.DataReader;
import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataSetConstants;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.types.ColumnTypes;
import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.util.Base64Util;
import org.seasar.framework.util.FileInputStreamUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.TimestampConversionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Excel用の {@link DataReader}です。
 * 
 * @author higa
 * @author manhole
 * @author azusa
 */
public class SsReader implements DataReader, DataSetConstants {
    final private static Logger logger = LoggerFactory.getLogger(SsReader.class);

    /**
     * データセットです。
     */
    protected DataSet dataSet;

    /**
     * ワークブックです。
     */
    protected Workbook workbook;

    /**
     * データフォーマットです。
     */
    protected DataFormat dataFormat;

    /**
     * 文字列をトリミングするかどうか
     */
    protected boolean trimString = true;

    /**
     * {@link SsReader}を作成します。
     * 
     * @param path
     *            パス
     */
    public SsReader(String path) {
        this(path, true);
    }

    /**
     * {@link SsReader}を作成します。
     * 
     * @param path
     *            パス
     * @param trimString
     *            文字列をトリムするかどうか
     */
    public SsReader(String path, boolean trimString) {
        this(ResourceUtil.getResourceAsStream(path), trimString);
    }

    /**
     * {@link SsReader}を作成します。
     * 
     * @param dirName
     *            ディレクトリ名
     * @param fileName
     *            ファイル名
     */
    public SsReader(String dirName, String fileName) {
        this(dirName, fileName, true);
    }

    /**
     * {@link SsReader}を作成します。
     * 
     * @param dirName
     *            ディレクトリ名
     * @param fileName
     *            ファイル名
     * @param trimString
     *            文字列をトリムするかどうか
     */
    public SsReader(String dirName, String fileName, boolean trimString) {
        this(ResourceUtil.getResourceAsFile(dirName), fileName, trimString);
    }

    /**
     * {@link SsReader}を作成します。
     * 
     * @param dir
     *            ディレクトリ
     * @param fileName
     *            ファイル名
     */
    public SsReader(File dir, String fileName) {
        this(dir, fileName, true);
    }

    /**
     * {@link SsReader}を作成します。
     * 
     * @param dir
     *            ディレクトリ
     * @param fileName
     *            ファイル名
     * @param trimString
     *            文字列をトリムするかどうか
     */
    public SsReader(File dir, String fileName, boolean trimString) {
        this(new File(dir, fileName), trimString);
    }

    /**
     * {@link SsReader}を作成します。
     * 
     * @param file
     *            ファイル
     */
    public SsReader(File file) {
        this(file, true);
    }

    /**
     * {@link SsReader}を作成します。
     * 
     * @param file
     *            ファイル
     * @param trimString
     *            文字列をトリムするかどうか
     */
    public SsReader(File file, boolean trimString) {
        this(FileInputStreamUtil.create(file), trimString);
    }

    /**
     * {@link SsReader}を作成します。
     * 
     * @param in
     *            入力ストリーム
     */
    public SsReader(InputStream in) {
        this(in, true);
    }

    /**
     * {@link SsReader}を作成します。
     * 
     * @param in
     *            入力ストリーム
     * @param trimString
     *            文字列をトリムするかどうか
     */
    public SsReader(InputStream in, boolean trimString) {
        this.trimString = trimString;
        try {
            workbook = WorkbookFactory.create(in);
        } catch (IOException ex) {
            throw new IORuntimeException(ex);
        }
        dataFormat = workbook.createDataFormat();
        dataSet = new DataSetImpl();
        for (int i = 0; i < workbook.getNumberOfSheets(); ++i) {
            createTable(workbook.getSheetName(i), workbook.getSheetAt(i));
        }
    }

    @Override
    public DataSet read() {
        return dataSet;
    }

    /**
     * テーブルを作成します。
     * 
     * @param sheetName
     *            シート名
     * @param sheet
     *            シート
     * @return テーブル
     */
    protected DataTable createTable(String sheetName, Sheet sheet) {
        DataTable table = dataSet.addTable(sheetName);
        int rowCount = sheet.getLastRowNum();
        if (rowCount > 0) {
            setupColumns(table, sheet);
            setupRows(table, sheet);
        } else if (rowCount == 0) {
            setupColumns(table, sheet);
        }
        return table;
    }

    /**
     * カラムの情報をセットアップします。
     * 
     * @param table
     *            テーブル
     * @param sheet
     *            シート
     */
    protected void setupColumns(DataTable table, Sheet sheet) {
        Row nameRow = sheet.getRow(0);
        Row valueRow = sheet.getRow(1);
        for (int i = 0; i <= Short.MAX_VALUE; ++i) {
            Cell nameCell = nameRow.getCell((short) i);
            if (nameCell == null) {
                break;
            }
            String columnName = nameCell.getRichStringCellValue().getString();
            if (columnName.length() == 0) {
                break;
            }
            Cell valueCell = null;
            if (valueRow != null) {
                for (int j = 1; j <= sheet.getLastRowNum(); j++) {
                    if (sheet.getRow(j) == null)
                        //throw new RuntimeException("tableName: " + table.getTableName() + ", columnName: " + columnName + ", row: " + j + ", LastRowNum: " + sheet.getLastRowNum());
                        break;
                    valueCell = sheet.getRow(j).getCell((short) i);
                    if (valueCell != null
                            && !StringUtil.isEmpty(valueCell.toString())) {
                        break;
                    }
                }
            }
            if (valueCell != null) {
                table.addColumn(columnName, getColumnType(valueCell));
            } else {
                table.addColumn(columnName);
            }
        }
    }

    /**
     * シートの行をセットアップします。
     * 
     * @param table
     *            テーブル
     * @param sheet
     *            シート
     */
    protected void setupRows(DataTable table, Sheet sheet) {
        for (int i = 1; i <= SpreadsheetVersion.EXCEL97.getMaxRows(); ++i) {
            Row row = sheet.getRow(i);
            if (row == null) {
                break;
            }
            setupRow(table, row);
        }
    }

    /**
     * 行をセットアップします。
     * 
     * @param table
     *            テーブル
     * @param row
     *            行
     */
    protected void setupRow(DataTable table, Row row) {
        DataRow dataRow = table.addRow();
        for (int i = 0; i < table.getColumnSize(); ++i) {
            Cell cell = row.getCell((short) i);
            Object value = getValue(cell);
            dataRow.setValue(i, value);
        }
    }

    /**
     * セルがBase64でフォーマットされているかどうかを返します。
     * 
     * @param cell
     *            セル
     * @return セルがBase64でフォーマットされているかどうか
     */
    public boolean isCellBase64Formatted(Cell cell) {
        CellStyle cs = cell.getCellStyle();
        short dfNum = cs.getDataFormat();
        String df = dataFormat.getFormat(dfNum);
        return BASE64_FORMAT.equals(df) || BASE64_FORMAT_LIBREOFFICE.equals(df);
    }

    /**
     * セルが日付のフォーマットかどうかを返します。
     * 
     * @param cell
     *            セル
     * @return セルが日付のフォーマットかどうか
     */
    public boolean isCellDateFormatted(Cell cell) {
        CellStyle cs = cell.getCellStyle();
        short dfNum = cs.getDataFormat();
        String format = dataFormat.getFormat(dfNum);
        if (StringUtil.isEmpty(format)) {
            return false;
        }
        if (format.indexOf('/') > 0 || format.indexOf('y') > 0
                || format.indexOf('m') > 0 || format.indexOf('d') > 0) {
            return true;
        }
        return false;
    }

    /**
     * セルの値を返します。
     * 
     * @param cell
     *            セル
     * @return セルの値
     */
    public Object getValue(Cell cell) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
        case NUMERIC:
            if (isCellDateFormatted(cell)) {
                return TimestampConversionUtil.toTimestamp(cell
                        .getDateCellValue());
            }
            final double numericCellValue = cell.getNumericCellValue();
            if (isInt(numericCellValue)) {
                return new BigDecimal((int) numericCellValue);
            }
            return new BigDecimal(Double.toString(numericCellValue));
        case STRING:
            String s = cell.getRichStringCellValue().getString();
            if (s != null) {
                s = StringUtil.rtrim(s);
                if (!trimString && s.length() > 1 && s.startsWith("\"")
                        && s.endsWith("\"")) {
                    s = s.substring(1, s.length() - 1);
                }
            }
            if ("".equals(s)) {
                s = null;
            }
            if (isCellBase64Formatted(cell)) {
                return Base64Util.decode(s);
            }
            return s;
        case BOOLEAN:
            boolean b = cell.getBooleanCellValue();
            return Boolean.valueOf(b);
        case FORMULA:
            String f = cell.getCellFormula();
            if (f.equalsIgnoreCase("TRUE()"))
                return Boolean.valueOf(true);
            else
                return Boolean.valueOf(false);
        default:
            return null;
        }
    }

    /**
     * カラムの型を返します。
     * 
     * @param cell
     *            セル
     * @return カラムの型
     */
    protected ColumnType getColumnType(Cell cell) {
        switch (cell.getCellType()) {
        case NUMERIC:
            if (isCellDateFormatted(cell)) {
                return ColumnTypes.TIMESTAMP;
            }
            return ColumnTypes.BIGDECIMAL;
        case BOOLEAN:
            return ColumnTypes.BOOLEAN;
        case STRING:
            if (isCellBase64Formatted(cell)) {
                return ColumnTypes.BINARY;
            } else if (trimString) {
                return ColumnTypes.STRING;
            } else {
                return ColumnTypes.NOT_TRIM_STRING;
            }
        case FORMULA:
            return ColumnTypes.BOOLEAN;
        default:
            return ColumnTypes.STRING;
        }
    }

    /**
     * 整数かどうかを返します。
     * 
     * @param numericCellValue
     *            numericな値
     * @return 整数かどうか
     */
    protected boolean isInt(final double numericCellValue) {
        return ((int) numericCellValue) == numericCellValue;
    }

}
