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
package org.seasar.extension.jdbc.gen.internal.model;

import java.io.File;
import java.util.List;
import java.util.Set;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.gen.model.SqlFileTestModel;

/**
 * {@link SqlFileTestModelFactory}の実装クラスです。
 * 
 * @author taedium
 */
public class SqlFileTestModelFactory {

    /** 設定ファイルのパス */
//i    protected String configPath;

    /** {@link JdbcManager}のコンポーネント名 */
    protected String jdbcManagerName;

    /** パッケージ名、パッケージ名を指定しない場合は{@code null} */
    protected String packageName;

    /** テストクラスの単純名 */
    protected String shortClassName;

    /** S2JUnit4を使用する場合{@code true}、S2Unitを使用する場合{@code false} */
//i    protected boolean useS2junit4;

    /** SQLファイルのパスのリスト */
    protected List<String> sqlFilePathList;

    /** SQLファイルのサポート */
    protected SqlFileSupport sqlFileSupport;

    /** 生成モデルのサポート */
    protected GeneratedModelSupport generatedModelSupport = new GeneratedModelSupport();
//i
    protected String rootPackageName;
//i
    protected String componentType = "none";
    /**
     * インスタンスを構築します。
     * 
     * @param classpathDir
     *            クラスパスのディレクトリ
     * @param sqlFileSet
     *            SQLファイルのセット
     * @param jdbcManagerName
     *            {@link JdbcManager}のコンポーネント名
     * @param packageName
     *            パッケージ名
     * @param shortClassName
     *            テストクラスの単純名
     * @param rootPackageName String
     * @param componentType String
     */
    public SqlFileTestModelFactory(File classpathDir, Set<File> sqlFileSet,
            String jdbcManagerName, String packageName,
            String shortClassName, String rootPackageName, String componentType) {
        this(classpathDir, sqlFileSet, jdbcManagerName,
                packageName, shortClassName, new SqlFileSupport(),
                rootPackageName, componentType);
    }

    /**
     * インスタンスを構築します。
     * 
     * @param classpathDir
     *            クラスパスのディレクトリ
     * @param sqlFileSet
     *            SQLファイルのセット
     * @param jdbcManagerName
     *            {@link JdbcManager}のコンポーネント名
     * @param packageName
     *            パッケージ名
     * @param shortClassName
     *            テストクラスの単純名
     * @param sqlFileSupport
     *            SQLファイルのサポート
     * @param rootPackageName String
     * @param componentType String
     */
    protected SqlFileTestModelFactory(File classpathDir,
            Set<File> sqlFileSet, String jdbcManagerName,
            String packageName, String shortClassName,
            SqlFileSupport sqlFileSupport,
            String rootPackageName, String componentType) {
        if (classpathDir == null) {
            throw new NullPointerException("classpathDir");
        }
        if (sqlFileSet == null) {
            throw new NullPointerException("sqlFileSet");
        }
//i        if (configPath == null) {
//i            throw new NullPointerException("configPath");
//i        }
        if (jdbcManagerName == null) {
            throw new NullPointerException("jdbcManagerName");
        }
        if (shortClassName == null) {
            throw new NullPointerException("shortClassName");
        }
        if (sqlFileSupport == null) {
            throw new NullPointerException("sqlFileSupport");
        }
//i        
        if (rootPackageName == null) {
            throw new NullPointerException("rootPackageName");
        }
//i        this.configPath = configPath;
        this.jdbcManagerName = jdbcManagerName;
        this.packageName = packageName;
        this.shortClassName = shortClassName;
//i        this.useS2junit4 = useS2junit4;
        this.sqlFileSupport = sqlFileSupport;
        this.sqlFilePathList = createSqlFilePathList(classpathDir, sqlFileSet);
        this.rootPackageName = rootPackageName;
        this.componentType = componentType;
    }

    /**
     * SQLファイルのパスのリストを作成します。
     * 
     * @param classpathDir
     *            クラスパスのディレクトリ
     * @param sqlFileSet
     *            SQLファイルのセット
     * @return SQLファイルのパスのリスト
     */
    protected List<String> createSqlFilePathList(File classpathDir,
            Set<File> sqlFileSet) {
        return sqlFileSupport.createSqlFilePathList(classpathDir, sqlFileSet);
    }

    public SqlFileTestModel getSqlFileTestModel() {
        SqlFileTestModel model = new SqlFileTestModel();
//i        model.setConfigPath(configPath);
        model.setJdbcManagerName(jdbcManagerName);
        model.setPackageName(packageName);
        model.setShortClassName(shortClassName);
//i        model.setUseS2junit4(useS2junit4);
        for (String sqlFilePath : sqlFilePathList) {
            model.addSqlFilePath(sqlFilePath);
        }
//i
        model.setRootPackageName(rootPackageName);
        model.setComponentType(componentType);
        doGeneratedInfo(model);
        return model;
    }

    /**
     * 生成情報を処理します。
     * 
     * @param sqlFileTestModel
     *            SQLファイルテストモデル
     */
    protected void doGeneratedInfo(SqlFileTestModel sqlFileTestModel) {
        generatedModelSupport.fillGeneratedInfo(this, sqlFileTestModel);
    }
}
