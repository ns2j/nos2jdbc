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
package org.seasar.extension.jdbc.gen.model.factory;

import javax.annotation.Generated;

import org.seasar.extension.jdbc.gen.model.ArchiveTestUtilModel;

/**
 * {@link ArchiveTestUtilModelFactory}の実装クラスです。
 * 
 * @author gari
 */
public class ArchiveTestUtilModelFactory {

    /** パッケージ名 */
    protected String packageName;

    /** クラスモデルのサポート */
    protected ClassModelSupport classModelSupport = new ClassModelSupport();

    /** 生成モデルのサポート */
    protected GeneratedModelSupport generatedModelSupport = new GeneratedModelSupport();

    /**
     * インスタンスを構築します。
     * 
     * @param packageName
     *            パッケージ名
     */
    public ArchiveTestUtilModelFactory(String packageName) {
        this.packageName = packageName;

    }

    public ArchiveTestUtilModel getArchiveTestUtilModel() {
        ArchiveTestUtilModel archiveTestUtilModel = new ArchiveTestUtilModel();
        archiveTestUtilModel.setPackageName(packageName);
        archiveTestUtilModel.setShortClassName("ArchiveTestUtil");
        doImportName(archiveTestUtilModel);
        doGeneratedInfo(archiveTestUtilModel);
        return archiveTestUtilModel;
    }

    /**
     * インポート名を処理します。
     * 
     * @param archiveTestUtilModel
     *            抽象サービスモデル
     */
    protected void doImportName(ArchiveTestUtilModel archiveTestUtilModel) {
        classModelSupport.addImportName(archiveTestUtilModel, Generated.class);
    }

    /**
     * 生成情報を処理します。
     * 
     * @param archiveTestUtilModel
     *            抽象サービスモデル
     */
    protected void doGeneratedInfo(ArchiveTestUtilModel archiveTestUtilModel) {
        generatedModelSupport.fillGeneratedInfo(this, archiveTestUtilModel);
    }
    
}
