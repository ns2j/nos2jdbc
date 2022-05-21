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
package org.seasar.framework.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.file.CopyOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;

import org.seasar.framework.exception.IORuntimeException;

/**
 * {@link File}を扱うユーティリティ・クラスです。
 * 
 * @author higa
 */
public class FileUtil {

    /**
     * インスタンスを構築します。
     */
    protected FileUtil() {
    }

    /**
     * この抽象パス名の正規の形式を返します。
     * 
     * @param file
     *            ファイル
     * @return この抽象パス名と同じファイルまたはディレクトリを示す正規パス名文字列
     */
    public static String getCanonicalPath(File file) {
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * この抽象パス名を<code>file:</code> URLに変換します。
     * 
     * @param file
     *            ファイル
     * @return ファイルURLを表すURLオブジェクト
     */
    public static URL toURL(final File file) {
        try {
            return file.toURI().toURL();
        } catch (final IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * ファイルの内容をバイト配列に読み込んで返します。
     * 
     * @param file
     *            ファイル
     * @return ファイルの内容を読み込んだバイト配列
     */
    public static byte[] getBytes(File file) {
        return InputStreamUtil.getBytes(FileInputStreamUtil.create(file));
    }

    /**
     * ファイルをコピーします。
     * 
     * @param src
     *            コピー元ファイル
     * @param dest
     *            コピー先ファイル
     */
    public static void copy(File src, File dest) {
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(src);
            out = new FileOutputStream(dest);
            copyInternal(in, out);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            CloseableUtil.close(in);
            CloseableUtil.close(out);
        }
    }

    /**
     * ファイルをコピーし追加します。
     * 
     * @param src
     *            コピー元ファイル
     * @param dest
     *            コピー先ファイル
     */
    public static void append(File src, File dest) {
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(src);
            out = new FileOutputStream(dest, true);
            copyInternal(in, out);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            CloseableUtil.close(in);
            CloseableUtil.close(out);
        }
    }

    /**
     * 内部的にコピーします。
     * 
     * @param in
     *            コピー元
     * @param out
     *            コピー先
     * @throws IOException
     *             IO例外が発生した場合
     */
    protected static void copyInternal(FileInputStream in, FileOutputStream out)
            throws IOException {
        FileChannel src = in.getChannel();
        FileChannel dest = out.getChannel();
        src.transferTo(0, src.size(), dest);
    }
    /**
     * バイトの配列をファイルに書き出します。
     * 
     * @param path
     *            ファイルのパス
     * @param data
     *            バイトの配列
     * @throws NullPointerException
     *             pathやdataがnullの場合。
     */
    public static void write(String path, byte[] data) {
        if (path == null) {
            throw new NullPointerException("path");
        }
        if (data == null) {
            throw new NullPointerException("data");
        }
        write(path, data, 0, data.length);
    }

    /**
     * バイトの配列をファイルに書き出します。
     * 
     * @param path
     *            ファイルのパス
     * @param data
     *            バイトの配列
     * @param offset
     *            オフセット
     * @param length
     *            配列の長さ
     * @throws NullPointerException
     *             pathやdataがnullの場合。
     */
    public static void write(String path, byte[] data, int offset, int length) {
        if (path == null) {
            throw new NullPointerException("path");
        }
        if (data == null) {
            throw new NullPointerException("data");
        }
        try {
            OutputStream out = new BufferedOutputStream(new FileOutputStream(
                    path));
            try {
                out.write(data, offset, length);
            } finally {
                out.close();
            }
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }
    /**
     * 新しいファイルを不可分 (atomic) に生成します。
     * 
     * @param file
     *            ファイル
     * @return 指定されたファイルが存在せず、ファイルの生成に成功した場合は{@code true}、示されたファイルがすでに存在する場合は
     *         {@code false}
     */
    public static boolean createNewFile(File file) {
        try {
            return file.createNewFile();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 一時ファイルを作成します。
     * 
     * @param prefix
     *            接頭辞文字列。3 文字以上の長さが必要である
     * @param suffix
     *            接尾辞文字列。null も指定でき、その場合は、接尾辞 ".tmp" が使用される
     * @return File
     */
    public static File createTempFile(String prefix, String suffix) {
        try {
            return File.createTempFile(prefix, suffix);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * ファイルの正規の形式を返します。
     * 
     * @param file
     *            ファイル
     * @return 正規の形式
     */
    public static File getCanonicalFile(File file) {
        try {
            return file.getCanonicalFile();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * ディレクトリを横断します。
     * 
     * @param dir
     *            ディレクトリ
     * @param filter
     *            フィルタ
     * @param comparator
     *            コンパレータ
     * @param handler
     *            ハンドラ
     */
    public static void traverseDirectory(File dir, FilenameFilter filter,
            Comparator<File> comparator, FileHandler handler) {
        if (!dir.exists()) {
            return;
        }
        File[] files = dir.listFiles(filter);
        if (files == null) {
            return;
        }
        Arrays.sort(files, comparator);
        for (File file : files) {
            if (file.isDirectory()) {
                traverseDirectory(file, filter, comparator, handler);
            }
            handler.handle(file);
        }
    }

    /**
     * Javaファイルを作成します。
     * 
     * @param baseDir
     *            ベースディレクトリ
     * @param packageName
     *            パッケージ名
     * @param shortClassName
     *            クラスの単純名
     * @return Javaファイル
     */
    public static File createJavaFile(File baseDir, String packageName,
            String shortClassName) {
        File packageDir;
        if (packageName == null) {
            packageDir = baseDir;
        } else {
            packageDir = new File(baseDir, packageName.replace('.',
                    File.separatorChar));
        }
        return new File(packageDir, shortClassName + ".java");
    }

    /**
     * ファイルを扱うインタフェースです・
     * 
     * @author taedium
     */
    public interface FileHandler {

        /**
         * 処理します。
         * 
         * @param file File
         */
        void handle(File file);
    }

    public static void copyFolder(Path source, Path target, CopyOption... options)
            throws IOException {
        Files.walkFileTree(source, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                    throws IOException {
                Files.createDirectories(target.resolve(source.relativize(dir)));
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                Files.copy(file, target.resolve(source.relativize(file)), options);
                return FileVisitResult.CONTINUE;
            }
        });
    }
    
    public static void rmdir(Path rootPath) {
        try (Stream<Path> walk = Files.walk(rootPath)) {
            walk.sorted(Comparator.reverseOrder())
            .map(Path::toFile)
            //.peek(System.out::println)
            .forEach(File::delete);
        } catch (IOException e) {
        }
    }
}
