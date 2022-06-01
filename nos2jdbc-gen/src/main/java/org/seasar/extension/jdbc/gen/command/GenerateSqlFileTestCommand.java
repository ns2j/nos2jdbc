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
package org.seasar.extension.jdbc.gen.command;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.seasar.extension.jdbc.gen.exception.RequiredPropertyNullRuntimeException;
import org.seasar.extension.jdbc.gen.generator.GenerationContext;
import org.seasar.extension.jdbc.gen.generator.Generator;
import org.seasar.extension.jdbc.gen.model.ClassModel;
import org.seasar.extension.jdbc.gen.model.SqlFileTestModel;
import org.seasar.extension.jdbc.gen.model.factory.SqlFileTestModelFactory;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.FileUtil;

/**
 * SQLファイルに対するテストクラスのJavaファイルを生成する{@link Command}の実装です。
 * 
 * @author taedium
 */
public class GenerateSqlFileTestCommand extends AbstractCommand {

    /** ロガー */
    protected static Logger logger = Logger
            .getLogger(GenerateSqlFileTestCommand.class);

    /** クラスパスのディレクトリ */
    protected File classpathDir;

    /** SQLファイルのセット */
    protected Set<File> sqlFileSet = new HashSet<File>();

    /** ルートパッケージ名 */
    protected String rootPackageName = "";

    /** サブパッケージ名 */
    protected String subPackageName = "";

    /** 生成するテストクラスの単純名 */
    protected String shortClassName = "SqlFileTest";

    /** テストクラスのテンプレート名 */
    protected String templateFileName = "/sqlfiletest.ftl";

    /** テンプレートファイルのエンコーディング */
    protected String templateFileEncoding = "UTF-8";

    /** テンプレートファイルを格納するプライマリディレクトリ */
    protected File templateFilePrimaryDir = null;

    protected String jvmLang = "java";
    
    /** 生成するJavaファイルの出力先ディレクトリ */
    protected File javaFileDestDir = new File(new File("src", "test"), "java");

    /** Javaファイルのエンコーディング */
    protected String javaFileEncoding = "UTF-8";

    /** 上書きをする場合{@code true}、しない場合{@code false} */
    protected boolean overwrite = true;
//i
    protected String componentType = "none";
    
    /** SQLファイルテストのモデルのファクトリ */
    protected SqlFileTestModelFactory sqlFileTestModelFactory;

    /** ジェネレータ */
    protected Generator generator;

    /**
     * サブパッケージ名を返します。
     * 
     * @return サブパッケージ名
     */
    public String getSubPackageName() {
        return subPackageName;
    }

    /**
     * サブパッケージ名を設定します。
     * 
     * @param subPackageName
     *            サブパッケージ名
     */
    public void setSubPackageName(String subPackageName) {
        this.subPackageName = subPackageName;
    }

    public String getJvmLang() {
        return jvmLang;
    }
    public void setJvmLang(String lang) {
        jvmLang = lang;
    }
    
    /**
     * テストクラスのテンプレート名を返します。
     * 
     * @return テストクラスのテンプレート名
     */
    public String getTemplateFileName() {
        return templateFileName;
    }

    /**
     * テストクラスのテンプレート名を設定します。
     * 
     * @param templateFileName
     *            テストクラスのテンプレート名
     */
    public void setTemplateFileName(String templateFileName) {
        this.templateFileName = templateFileName;
    }

    /**
     * 生成するJavaファイルの出力先ディレクトリを返します。
     * 
     * @return 生成するJavaファイルの出力先ディレクトリ
     */
    public File getJavaFileDestDir() {
        return new File(new File("src", "test"), jvmLang);
    }

    /**
     * 生成するJavaファイルの出力先ディレクトリを設定します。
     * 
     * @param javaFileDestDir
     *            生成するJavaファイルの出力先ディレクトリ
     */
    public void setJavaFileDestDir(File javaFileDestDir) {
        this.javaFileDestDir = javaFileDestDir;
    }

    /**
     * Javaファイルのエンコーディングを返します。
     * 
     * @return Javaファイルのエンコーディング
     */
    public String getJavaFileEncoding() {
        return javaFileEncoding;
    }

    /**
     * Javaファイルのエンコーディングを設定します。
     * 
     * @param javaFileEncoding
     *            Javaファイルのエンコーディング
     */
    public void setJavaFileEncoding(String javaFileEncoding) {
        this.javaFileEncoding = javaFileEncoding;
    }

    /**
     * 上書きをする場合{@code true}、しない場合{@code false}を返します。
     * 
     * @return 上書きをする場合{@code true}、しない場合{@code false}
     */
    public boolean isOverwrite() {
        return overwrite;
    }

    /**
     * 上書きをする場合{@code true}、しない場合{@code false}を設定します。
     * 
     * @param overwrite
     *            上書きをする場合{@code true}、しない場合{@code false}
     */
    public void setOverwrite(boolean overwrite) {
        this.overwrite = overwrite;
    }

    /**
     * ルートパッケージ名を設定します。
     * 
     * @return ルートパッケージ名
     */
    public String getRootPackageName() {
        return rootPackageName;
    }

    /**
     * ルートパッケージ名を返します。
     * 
     * @param rootPackageName
     *            ルートパッケージ名
     */
    public void setRootPackageName(String rootPackageName) {
        this.rootPackageName = rootPackageName;
    }

    /**
     * テンプレートファイルのエンコーディングを返します。
     * 
     * @return テンプレートファイルのエンコーディング
     */
    public String getTemplateFileEncoding() {
        return templateFileEncoding;
    }

    /**
     * テンプレートファイルのエンコーディングを設定します。
     * 
     * @param templateFileEncoding
     *            テンプレートファイルのエンコーディング
     */
    public void setTemplateFileEncoding(String templateFileEncoding) {
        this.templateFileEncoding = templateFileEncoding;
    }

    /**
     * テンプレートファイルを格納するプライマリディレクトリを返します。
     * 
     * @return テンプレートファイルを格納するプライマリディレクトリ
     */
    public File getTemplateFilePrimaryDir() {
        return templateFilePrimaryDir;
    }

    /**
     * テンプレートファイルを格納するプライマリディレクトリを設定します。
     * 
     * @param templateFilePrimaryDir
     *            テンプレートファイルを格納するプライマリディレクトリ
     */
    public void setTemplateFilePrimaryDir(File templateFilePrimaryDir) {
        this.templateFilePrimaryDir = templateFilePrimaryDir;
    }

    /**
     * クラスパスのディレクトリを返します。
     * 
     * @return クラスパスのディレクトリ
     */
    public File getClasspathDir() {
        return classpathDir;
    }

    /**
     * クラスパスのディレクトリを設定します。
     * 
     * @param classpathDir
     *            クラスパスのディレクトリ
     */
    public void setClasspathDir(File classpathDir) {
        this.classpathDir = classpathDir;
    }

    /**
     * SQLファイルのセットを返します。
     * 
     * @return SQLファイルのセット
     */
    public Set<File> getSqlFileSet() {
        return sqlFileSet;
    }

    /**
     * SQLファイルのセットを設定します。
     * 
     * @param sqlFileSet
     *            SQLファイルのセット
     */
    public void setSqlFileSet(Set<File> sqlFileSet) {
        this.sqlFileSet = sqlFileSet;
    }

    /**
     * 生成するテストクラスの単純名を返します。
     * 
     * @return 生成するテストクラスの単純名
     */
    public String getShortClassName() {
        return shortClassName;
    }

    /**
     * 生成するテストクラスの単純名を設定します。
     * 
     * @param shortClassName
     *            生成するテストクラスの単純名
     */
    public void setShortClassName(String shortClassName) {
        this.shortClassName = shortClassName;
    }

    //i    
    public String getComponentType() {
        return componentType;
    }
//i
    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    protected boolean isKotlin() {
        return jvmLang.equals("kotlin");
    }

    @Override
    protected void doValidate() {
        if (classpathDir == null) {
            throw new RequiredPropertyNullRuntimeException("classpathDir");
        }
    }

    /**
     * 初期化します。
     */
    @Override
    protected void doInit() {
        sqlFileTestModelFactory = createSqlFileTestModelFactory();
        generator = createGenerator();
    }

    @Override
    protected void doExecute() {
        SqlFileTestModel model = sqlFileTestModelFactory.getSqlFileTestModel();
        GenerationContext context = createGenerationContext(model,
                templateFileName);
        generator.generate(context);
    }

    @Override
    protected void doDestroy() {
    }

    /**
     * {@link SqlFileTestModelFactory}の実装を作成します。
     * 
     * @return {@link SqlFileTestModelFactory}の実装
     */
    protected SqlFileTestModelFactory createSqlFileTestModelFactory() {
        return new SqlFileTestModelFactory(classpathDir,
                sqlFileSet, jdbcManagerName, ClassUtil.concatName(
                        rootPackageName, subPackageName), shortClassName,
                rootPackageName, componentType);
    }

    /**
     * {@link Generator}の実装を作成します。
     * 
     * @return {@link Generator}の実装
     */
    protected Generator createGenerator() {
        return new Generator(templateFileEncoding,
                templateFilePrimaryDir);
    }

    /**
     * {@link GenerationContext}の実装を作成します。
     * 
     * @param model
     *            モデル
     * @param templateName
     *            テンプレート名
     * @return {@link GenerationContext}の実装
     */
    protected GenerationContext createGenerationContext(ClassModel model,
            String templateName) {
        File file = FileUtil.createJavaFile(javaFileDestDir, model
                .getPackageName(), model.getShortClassName(), isKotlin());
        return new GenerationContext(model, file, jvmLang + templateName,
                javaFileEncoding, overwrite);
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }
}
