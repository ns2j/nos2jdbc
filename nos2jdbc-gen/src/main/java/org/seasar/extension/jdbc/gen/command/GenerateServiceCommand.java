/*
v * Copyright 2004-2015 the Seasar Foundation and the Others.
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

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.gen.exception.RequiredPropertyNullRuntimeException;
import org.seasar.extension.jdbc.gen.generator.GenerationContext;
import org.seasar.extension.jdbc.gen.generator.Generator;
import org.seasar.extension.jdbc.gen.meta.EntityMetaReader;
import org.seasar.extension.jdbc.gen.meta.EntityMetaReaderImpl;
import org.seasar.extension.jdbc.gen.model.ClassModel;
import org.seasar.extension.jdbc.gen.model.NoS2AbstServiceModel;
import org.seasar.extension.jdbc.gen.model.ServiceBaseQualifierModel;
import org.seasar.extension.jdbc.gen.model.ServiceModel;
import org.seasar.extension.jdbc.gen.model.factory.NamesModelFactory;
import org.seasar.extension.jdbc.gen.model.factory.NoS2AbstServiceModelFactory;
import org.seasar.extension.jdbc.gen.model.factory.ServiceBaseQualifierModelFactory;
import org.seasar.extension.jdbc.gen.model.factory.ServiceModelFactory;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.FileUtil;

/**
 * サービスクラスのJavaファイルを生成する{@link Command}の実装です。
 * <p>
 * このコマンドは、エンティティクラスのメタデータからサービスクラスのJavaファイルを生成します。 そのため、
 * コマンドを実行するにはエンティティクラスを参照できるようにエンティティクラスが格納されたディレクトリをあらかじめクラスパスに設定しておく必要があります 。
 * また、そのディレクトリは、プロパティ{@link #classpathDir}に設定しておく必要があります。
 * </p>
 * このコマンドは、次のクラスの2種類のjavaコードを生成します。
 * <ul>
 * <li>エンティティクラスに対応するサービスクラス</li>
 * <li>上記サービスクラスの親クラスとなる抽象サービスクラス</li>
 * </ul>
 * 
 * @author taedium
 */
public class GenerateServiceCommand extends AbstractCommand {

    /** ロガー */
    protected static Logger logger = Logger
            .getLogger(GenerateServiceCommand.class);

    /** クラスパスのディレクトリ */
    protected File classpathDir;

    /** ルートパッケージ名 */
    protected String rootPackageName = "";

    /** エンティティクラスのパッケージ名 */
    protected String entityPackageName = "entity";

    /** 対象とするエンティティクラス名の正規表現 */
    protected String entityClassNamePattern = ".*";

    /** 対象としないエンティティクラス名の正規表現 */
    protected String ignoreEntityClassNamePattern = "";

    /** サービスクラスのパッケージ名 */
    protected String servicePackageName = "service";

    /** サービスクラス名のサフィックス */
    protected String serviceClassNameSuffix = "Service";

    /** 抽象サービスクラスのテンプレート名 */
    //i protected String abstractServiceTemplateFileName = "java/abstract-service.ftl";
    protected String abstractServiceTemplateFileName = "/nos2-abstract-service.ftl";
//i
    protected String serviceBaseQuailfierTemplateFileName = "/service-base-qualifier.ftl";
    
    
    /** サービスクラスのテンプレート名 */
    protected String serviceTemplateFileName = "/service.ftl";

    /** 名前クラスを使用する場合{@code true} */
    protected boolean useNamesClass = true;

    /** 名前クラス名のサフィックス */
    protected String namesClassNameSuffix = "Names";

    /** 名前クラスのパッケージ名 */
    protected String namesPackageName = "entity";

    protected String jvmLang = "java";
    
    /** 生成するJavaファイルの出力先ディレクトリ */
    protected File javaFileDestDir = new File(new File("src", "main"), "java");

    /** Javaファイルのエンコーディング */
    protected String javaFileEncoding = "UTF-8";

    /** サービスクラスを上書きをする場合{@code true}、しない場合{@code false} */
    protected boolean overwrite = true;

    /** 抽象サービスクラスを上書きをする場合{@code true}、しない場合{@code false} */
    protected boolean overwriteAbstractService = true;

    /** テンプレートファイルのエンコーディング */
    protected String templateFileEncoding = "UTF-8";

    /** テンプレートファイルを格納したプライマリディレクトリ */
    protected File templateFilePrimaryDir = null;
    
//i
    protected String componentType = "none";

    /** エンティティメタデータのリーダ */
    protected EntityMetaReader entityMetaReader;

    /** サービスモデルのファクトリ */
    protected ServiceModelFactory serviceModelFactory;

    /** 抽象サービスモデルのファクトリ *///i
    protected NoS2AbstServiceModelFactory noS2AbstServiceModelFactory;

    protected ServiceBaseQualifierModelFactory serviceBaseQualifierModelFactory;

    /** 名前モデルのファクトリ */
    protected NamesModelFactory namesModelFactory;

    /** ジェネレータ */
    protected Generator generator;

    /**
     * インスタンスを構築します。
     */
    public GenerateServiceCommand() {
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
     * サービスクラス名のサフィックスを返します。
     * 
     * @return サービスクラス名のサフィックス
     */
    public String getServiceClassNameSuffix() {
        return serviceClassNameSuffix;
    }

    /**
     * サービスクラス名のサフィックスを設定します。
     * 
     * @param serviceClassNameSuffix
     *            サービスクラス名のサフィックス
     */
    public void setServiceClassNameSuffix(String serviceClassNameSuffix) {
        this.serviceClassNameSuffix = serviceClassNameSuffix;
    }

    /**
     * サービスクラスのパッケージ名を返します。
     * 
     * @return サービスクラスのパッケージ名
     */
    public String getServicePackageName() {
        return servicePackageName;
    }

    /**
     * サービスクラスのパッケージ名を設定します。
     * 
     * @param servicePackageName
     *            サービスクラスのパッケージ名
     */
    public void setServicePackageName(String servicePackageName) {
        this.servicePackageName = servicePackageName;
    }

    /**
     * サービスクラスのテンプレート名を返します。
     * 
     * @return サービスクラスのテンプレート名
     */
    public String getServiceTemplateFileName() {
        return serviceTemplateFileName;
    }

    /**
     * サービスクラスのテンプレート名を設定します。
     * 
     * @param serviceTemplateFileName
     *            サービスクラスのテンプレート名
     */
    public void setServiceTemplateFileName(String serviceTemplateFileName) {
        this.serviceTemplateFileName = serviceTemplateFileName;
    }

    /**
     * 抽象サービスクラスのテンプレート名を返します。
     * 
     * @return 抽象サービスクラスのテンプレート名
     */
    public String getAbstractServiceTemplateFileName() {
        return abstractServiceTemplateFileName;
    }

    /**
     * 抽象サービスクラスのテンプレート名を設定します。
     * 
     * @param abstractServiceTemplateFileName
     *            抽象サービスクラスのテンプレート名
     */
    public void setAbstractServiceTemplateFileName(
            String abstractServiceTemplateFileName) {
        this.abstractServiceTemplateFileName = abstractServiceTemplateFileName;
    }

    /**
     * 名前クラス名のサフィックスを返します。
     * 
     * @return 名前クラス名のサフィックス
     */
    public String getNamesClassNameSuffix() {
        return namesClassNameSuffix;
    }

    /**
     * 名前クラス名のサフィックスを設定します。
     * 
     * @param namesClassNameSuffix
     *            名前クラス名のサフィックス
     */
    public void setNamesClassNameSuffix(String namesClassNameSuffix) {
        this.namesClassNameSuffix = namesClassNameSuffix;
    }

    /**
     * 名前クラスのパッケージ名を返します。
     * 
     * @return 名前クラスのパッケージ名
     */
    public String getNamesPackageName() {
        return namesPackageName;
    }

    /**
     * 名前クラスのパッケージ名を設定します。
     * 
     * @param namesPackageName
     *            名前クラスのパッケージ名
     */
    public void setNamesPackageName(String namesPackageName) {
        this.namesPackageName = namesPackageName;
    }

    /**
     * エンティティクラスのパッケージ名を返します。
     * 
     * @return エンティティクラスのパッケージ名
     */
    public String getEntityPackageName() {
        return entityPackageName;
    }

    /**
     * エンティティクラスのパッケージ名を設定します。
     * 
     * @param entityPackageName
     *            エンティティクラスのパッケージ名
     */
    public void setEntityPackageName(String entityPackageName) {
        this.entityPackageName = entityPackageName;
    }

    /**
     * 対象とするエンティティクラス名の正規表現を返します。
     * 
     * @return 対象とするエンティティクラス名の正規表現
     */
    public String getEntityClassNamePattern() {
        return entityClassNamePattern;
    }

    /**
     * 対象とするエンティティクラス名の正規表現を設定します。
     * 
     * @param entityClassNamePattern
     *            対象とするエンティティクラス名の正規表現
     */
    public void setEntityClassNamePattern(String entityClassNamePattern) {
        this.entityClassNamePattern = entityClassNamePattern;
    }

    /**
     * 対象としないエンティティクラス名の正規表現を返します。
     * 
     * @return 対象としないエンティティクラス名の正規表現
     */
    public String getIgnoreEntityClassNamePattern() {
        return ignoreEntityClassNamePattern;
    }

    /**
     * 対象としないエンティティクラス名の正規表現を設定します。
     * 
     * @param ignoreEntityClassNamePattern
     *            対象としないエンティティクラス名の正規表現
     */
    public void setIgnoreEntityClassNamePattern(
            String ignoreEntityClassNamePattern) {
        this.ignoreEntityClassNamePattern = ignoreEntityClassNamePattern;
    }

    public String getJvmLang() {
        return jvmLang;
    }
    public void setJvmLang(String lang) {
        jvmLang = lang;
    }
    

    /**
     * 生成するJavaファイルの出力先ディレクトリを返します。
     * 
     * @return 生成するJavaファイルの出力先ディレクトリ
     */
    public File getJavaFileDestDir() {
        return new File(new File("src", "main"), jvmLang);
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
     * サービスクラスを上書きをする場合{@code true}、しない場合{@code false}を返します。
     * 
     * @return 上書きをする場合{@code true}、しない場合{@code false}
     */
    public boolean isOverwrite() {
        return overwrite;
    }

    /**
     * サービスクラスを上書きをする場合{@code true}、しない場合{@code false}を設定します。
     * 
     * @param overwrite
     *            上書きをする場合{@code true}、しない場合{@code false}
     */
    public void setOverwrite(boolean overwrite) {
        this.overwrite = overwrite;
    }

    /**
     * 抽象サービスクラスを上書きをする場合{@code true}、しない場合{@code false}を返します。
     * 
     * @return 上書きをする場合{@code true}、しない場合{@code false}
     */
    public boolean isOverwriteAbstractService() {
        return overwriteAbstractService;
    }

    /**
     * 抽象サービスクラスを上書きをする場合{@code true}、しない場合{@code false}を設定します。
     * 
     * @param overwriteAbstractService
     *            上書きをする場合{@code true}、しない場合{@code false}
     */
    public void setOverwriteAbstractService(boolean overwriteAbstractService) {
        this.overwriteAbstractService = overwriteAbstractService;
    }

    /**
     * ルートパッケージ名を返します。
     * 
     * @return ルートパッケージ名
     */
    public String getRootPackageName() {
        return rootPackageName;
    }

    /**
     * ルートパッケージ名を設定します。
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
     * テンプレートファイルを格納したプライマリディレクトリを返します。
     * 
     * @return テンプレートファイルを格納したプライマリディレクトリ
     */
    public File getTemplateFilePrimaryDir() {
        return templateFilePrimaryDir;
    }

    /**
     * テンプレートファイルを格納したプライマリディレクトリを設定します。
     * 
     * @param templateFilePrimaryDir
     *            テンプレートファイルを格納したプライマリディレクトリ
     */
    public void setTemplateFilePrimaryDir(File templateFilePrimaryDir) {
        this.templateFilePrimaryDir = templateFilePrimaryDir;
    }

    /**
     * 名前クラスを使用する場合{@code true}、しない場合{@code false}を返します。
     * 
     * @return 名前クラスを使用する場合{@code true}、しない場合{@code false}
     */
    public boolean isUseNamesClass() {
        return useNamesClass;
    }

    /**
     * 名前クラスを使用する場合{@code true}、しない場合{@code false}を設定します。
     * 
     * @param useNamesClass
     *            名前クラスを使用する場合{@code true}、しない場合{@code false}
     */
    public void setUseNamesClass(boolean useNamesClass) {
        this.useNamesClass = useNamesClass;
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

    @Override
    protected void doInit() {
        entityMetaReader = createEntityMetaReader();
        namesModelFactory = createNamesModelFactory();
        serviceModelFactory = createServiceModelFactory();
        noS2AbstServiceModelFactory = createAbstServiceModelFactory();
        serviceBaseQualifierModelFactory = createServiceBaseQualifierModelFactory();
        generator = createGenerator();
    }

    @Override
    protected void doExecute() {
        generateAbstractService();
        if ("cdi".equals(componentType))
            generateServiceBaseQualifier();
        for (EntityMeta entityMeta : entityMetaReader.read()) {
            generateService(entityMeta);
        }
    }

    @Override
    protected void doDestroy() {
    }

    /**
     * 抽象サービスクラスのJavaファイルを生成します。
     */
    protected void generateAbstractService() {
        NoS2AbstServiceModel model = noS2AbstServiceModelFactory.getAbstServiceModel();
        GenerationContext context = createGenerationContext(model,
                jvmLang + abstractServiceTemplateFileName, overwriteAbstractService);
        generator.generate(context);
    }
    
    protected void generateServiceBaseQualifier() {
        ServiceBaseQualifierModel model = serviceBaseQualifierModelFactory.getServiceBaseQualifierModel();
        GenerationContext context = createGenerationContext(model,
                jvmLang + serviceBaseQuailfierTemplateFileName, overwriteAbstractService);
        generator.generate(context);
    }

    /**
     * サービスクラスのJavaファイルを生成します。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     */
    protected void generateService(EntityMeta entityMeta) {
        ServiceModel model = serviceModelFactory.getServiceModel(entityMeta);
        GenerationContext context = createGenerationContext(model,
                jvmLang + serviceTemplateFileName, overwrite);
        generator.generate(context);
    }

    /**
     * {@link GenerationContext}の実装を作成します。
     * 
     * @param model
     *            クラスモデル
     * @param templateName
     *            テンプレート名
     * @param overwrite
     *            上書きする場合{@code true}
     * @return {@link GenerationContext}の実装
     */
    protected GenerationContext createGenerationContext(ClassModel model,
            String templateName, boolean overwrite) {
        File file = FileUtil.createJavaFile(getJavaFileDestDir(), model
                    .getPackageName(), model.getShortClassName(), isKotlin());
        return new GenerationContext(model, file, templateName,
                javaFileEncoding, overwrite);
    }

    /**
     * {@link EntityMetaReader}の実装を作成します。
     * 
     * @return {@link EntityMetaReader}の実装
     */
    protected EntityMetaReader createEntityMetaReader() {
        return new EntityMetaReaderImpl(classpathDir, ClassUtil
                .concatName(rootPackageName, entityPackageName), jdbcManager
                .getEntityMetaFactory(), entityClassNamePattern,
                ignoreEntityClassNamePattern, false, null, null);
    }

    /**
     * {@link ServiceModelFactory}の実装を作成します。
     * 
     * @return {@link ServiceModelFactory}の実装
     */
    protected ServiceModelFactory createServiceModelFactory() {
        return new ServiceModelFactory(ClassUtil.concatName(
                rootPackageName, servicePackageName), serviceClassNameSuffix,
                namesModelFactory, useNamesClass, jdbcManagerName, componentType, isKotlin());
    }

    /**
     * {@link NoS2AbstServiceModelFactory}の実装を作成します。
     * 
     * @return {@link NoS2AbstServiceModelFactory}の実装
     */
    protected NoS2AbstServiceModelFactory createAbstServiceModelFactory() {
        return new NoS2AbstServiceModelFactory(ClassUtil
                .concatName(rootPackageName, servicePackageName),
                serviceClassNameSuffix, componentType);
    }

    protected ServiceBaseQualifierModelFactory createServiceBaseQualifierModelFactory() {
        return new ServiceBaseQualifierModelFactory(ClassUtil
                .concatName(rootPackageName, servicePackageName),
                serviceClassNameSuffix, componentType);
    }

    /**
     * {@link NamesModelFactory}の実装を作成します。
     * 
     * @return {@link NamesModelFactory}の実装
     */
    protected NamesModelFactory createNamesModelFactory() {
        return new NamesModelFactory(ClassUtil.concatName(
                rootPackageName, namesPackageName), namesClassNameSuffix);
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

    @Override
    protected Logger getLogger() {
        return logger;
    }

}
