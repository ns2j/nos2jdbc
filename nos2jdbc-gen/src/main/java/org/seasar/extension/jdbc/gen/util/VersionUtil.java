package org.seasar.extension.jdbc.gen.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.seasar.extension.jdbc.gen.exception.IllegalDdlInfoVersionRuntimeException;
import org.seasar.extension.jdbc.gen.exception.NextVersionExceededRuntimeException;
import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.FileUtil;

public class VersionUtil {
    /** ロガー */
    protected static Logger logger = Logger.getLogger(VersionUtil.class);

    /** エンコーディング */
    protected static final String ENCODING = "UTF-8";

    /** DDLのバージョンファイル */
    protected static File defaultFile = new File("db", "ddl-info.txt");

    public static int getVersionNo() {
        return getVersionNo(defaultFile);
    }
    public static int getVersionNo(File file) {
        Integer versionNo;
        if (!file.exists()) {
            logger.log("IS2JDBCGen0003", new Object[] { file.getPath() });
            versionNo = 0;
            return versionNo;
        }
        String line = readLine(file);
        if (line == null) {
            logger.log("IS2JDBCGen0007", new Object[] { file.getPath() });
            versionNo = 0;
            return versionNo;
        }
        int pos = line.indexOf("=");
        String value = pos > -1 ? line.substring(0, pos) : line;
        versionNo = convertToInt(file, value.trim());
        return versionNo;
    }

    @SuppressWarnings("resource")
    protected static String readLine(File file) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file), ENCODING));
            return reader.readLine();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }
    
    protected static int convertToInt(String value) {
        return convertToInt(defaultFile, value);
    }
    protected static int convertToInt(File file, String value) {
        int versionNo;
        try {
            versionNo = Integer.valueOf(value);
        } catch (NumberFormatException e) {
            throw new IllegalDdlInfoVersionRuntimeException(file.getPath(), value);
        }
        if (versionNo < 0) {
            throw new IllegalDdlInfoVersionRuntimeException(file.getPath(), value);
        }
        return versionNo;
    }

    public static void applyNextVersionNo(String comment) {
        applyNextVersionNo(defaultFile, comment);
    }

    public static void applyNextVersionNo(File file, String comment) {
        File temp = null;
        if (file.exists()) {
            temp = FileUtil.createTempFile("ddl-info", null);
            temp.deleteOnExit();
            FileUtil.copy(file, temp);
        }

        writeLine(getNextVersionNo() + "=" + comment);

        if (temp != null) {
            FileUtil.append(temp, file);
        }
    }

    public static int getNextVersionNo() {
        return getNextVersionNo(defaultFile);
    }
    
    public static int getNextVersionNo(File file) {
        long nextVersionNo = (long) getVersionNo(file) + 1;
        if (nextVersionNo > Integer.MAX_VALUE) {
            throw new NextVersionExceededRuntimeException(file.getPath());
        }
        return (int) nextVersionNo;
    }

    protected static void writeLine(String line) {
        writeLine(defaultFile, line);
    }
    protected static void writeLine(File file, String line) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file), ENCODING));
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            org.seasar.framework.util.CloseableUtil.close(writer);
        }
    }
}
