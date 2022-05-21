package org.seasar.extension.jdbc.gen.internal.version;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.seasar.extension.jdbc.gen.internal.exception.IllegalDdlInfoVersionRuntimeException;
import org.seasar.extension.jdbc.gen.internal.exception.NextVersionExceededRuntimeException;
import org.seasar.extension.jdbc.gen.internal.util.CloseableUtil;
import org.seasar.extension.jdbc.gen.internal.util.FileUtil;
import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.log.Logger;

public class VersionUtil {
    /** ロガー */
    protected static Logger logger = Logger.getLogger(VersionUtil.class);

    /** エンコーディング */
    protected static final String ENCODING = "UTF-8";

    /** DDLのバージョンファイル */
    protected static File file = new File("db", "ddl-info.txt");

    public static int getVersionNo() {
        Integer versionNo;
        String line = readLine();
        if (line == null) {
            logger.log("IS2JDBCGen0007", new Object[] { file.getPath() });
            versionNo = 0;
            return versionNo;
        }
        int pos = line.indexOf("=");
        String value = pos > -1 ? line.substring(0, pos) : line;
        versionNo = convertToInt(value.trim());
        return versionNo;
    }

    @SuppressWarnings("resource")
    protected static String readLine() {
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
        File temp = null;
        if (file.exists()) {
            temp = FileUtil.createTempFile("ddl-info", null);
            temp.deleteOnExit();
            FileUtil.copy(file, temp);
        }

        writeLine(getNextVersionNoInternal() + "=" + comment);

        if (temp != null) {
            FileUtil.append(temp, file);
        }
    }

    protected static int getNextVersionNoInternal() {
        long nextVersionNo = (long) getVersionNo() + 1;
        if (nextVersionNo > Integer.MAX_VALUE) {
            throw new NextVersionExceededRuntimeException(file.getPath());
        }
        return (int) nextVersionNo;
    }

    protected static void writeLine(String line) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file), ENCODING));
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            CloseableUtil.close(writer);
        }
    }
}
