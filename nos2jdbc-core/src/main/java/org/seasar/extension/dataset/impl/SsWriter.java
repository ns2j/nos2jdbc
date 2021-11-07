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
import java.io.OutputStream;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataSetConstants;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.DataWriter;
import org.seasar.framework.conversion.StringConversionUtil;
import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.util.Base64Util;
import org.seasar.framework.util.FileOutputStreamUtil;
import org.seasar.framework.util.ResourceUtil;

/**
 * Excel用の {@link DataWriter}です。
 * 
 * @author higa
 * @author azusa
 * 
 */
public class SsWriter implements DataWriter, DataSetConstants {

    /**
     * 出力ストリームです。
     */
    protected OutputStream out;

    /**
     * ワークブックです。
     */
    protected Workbook workbook;

    /**
     * 日付用のスタイルです。
     */
    protected CellStyle dateStyle;

    /**
     * Base64用のスタイルです。
     */
    protected CellStyle base64Style;

    /**
     * {@link SsWriter}を作成します。
     * 
     * @param path
     *            パス
     */
    public SsWriter(String path) {
        this(new File(ResourceUtil.getResourceAsFile("."), path));
    }

    /**
     * {@link SsWriter}を作成します。
     * 
     * @param dirName
     *            ディレクトリ名
     * @param fileName
     *            ファイル名
     */
    public SsWriter(String dirName, String fileName) {
        this(ResourceUtil.getResourceAsFile(dirName), fileName);
    }

    /**
     * {@link SsWriter}を作成します。
     * 
     * @param dir
     *            ディレクトリ
     * @param fileName
     *            ファイル名
     */
    public SsWriter(File dir, String fileName) {
        this(new File(dir, fileName));
    }

    /**
     * {@link SsWriter}を作成します。
     * 
     * @param file
     *            ファイル
     */
    public SsWriter(File file) {
        this(FileOutputStreamUtil.create(file));
    }

    /**
     * {@link SsWriter}を作成します。
     * 
     * @param out
     *            出力ストリーム
     */
    public SsWriter(OutputStream out) {
        setOutputStream(out);
    }

    /**
     * 出力ストリームを設定します。
     * 
     * @param out
     *            出力ストリーム
     */
    public void setOutputStream(OutputStream out) {
        this.out = out;
        try {
            workbook = WorkbookFactory.create(true);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
        DataFormat df = workbook.createDataFormat();
        dateStyle = workbook.createCellStyle();
        dateStyle.setDataFormat(df.getFormat(DATE_FORMAT));
        base64Style = workbook.createCellStyle();
        base64Style.setDataFormat(df.getFormat(BASE64_FORMAT));
    }

    @Override
    public void write(DataSet dataSet) {
        for (int i = 0; i < dataSet.getTableSize(); ++i) {
            DataTable table = dataSet.getTable(i);
            Sheet sheet = workbook.createSheet();
            workbook.setSheetName(i, table.getTableName());
            Row headerRow = sheet.createRow(0);
            for (int j = 0; j < table.getColumnSize(); ++j) {
                Cell cell = headerRow.createCell((short) j);
                cell.setCellValue(workbook.getCreationHelper().createRichTextString(table
                                .getColumnName(j)));
            }
            for (int j = 0; j < table.getRowSize(); ++j) {
                Row row = sheet.createRow(j + 1);
                for (int k = 0; k < table.getColumnSize(); ++k) {
                    DataRow dataRow = table.getRow(j);
                    Object value = dataRow.getValue(k);
                    if (value != null) {
                        Cell cell = row.createCell((short) k);
                        setValue(cell, value);
                    }
                }
            }
        }
        try {
            workbook.write(out);
            out.flush();
            out.close();
        } catch (IOException ex) {
            throw new IORuntimeException(ex);
        }
    }

    /**
     * セルに値を設定します。
     * 
     * @param cell
     *            セル
     * @param value
     *            値
     */
    protected void setValue(Cell cell, Object value) {
        CreationHelper helper = workbook.getCreationHelper();
        if (value instanceof Number) {
            cell.setCellValue(helper.createRichTextString(value.toString()));
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
            cell.setCellStyle(dateStyle);
        } else if (value instanceof byte[]) {
            cell.setCellValue(helper.createRichTextString(Base64Util
                    .encode((byte[]) value)));
            cell.setCellStyle(base64Style);
        } else if (value instanceof Boolean) {
            cell.setCellValue(((Boolean) value).booleanValue());
        } else {
            cell.setCellValue(helper.createRichTextString(StringConversionUtil
                    .toString(value, null)));
        }
    }
}
