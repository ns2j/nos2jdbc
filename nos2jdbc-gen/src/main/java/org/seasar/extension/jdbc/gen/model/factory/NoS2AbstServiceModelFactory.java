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

import org.seasar.extension.jdbc.gen.model.NoS2AbstServiceModel;

/**
 * {@link NoS2AbstServiceModelFactory}の実装クラスです。
 * 
 * @author taedium
 */
public class NoS2AbstServiceModelFactory {

    /** パッケージ名 */
    protected String packageName;

    /** サービスクラス名のサフィックス */
    protected String serviceClassNameSuffix;

    //i
    protected String componentType;

    /** クラスモデルのサポート */
    protected ClassModelSupport classModelSupport = new ClassModelSupport();

    /** 生成モデルのサポート */
    protected GeneratedModelSupport generatedModelSupport = new GeneratedModelSupport();

    /**
     * インスタンスを構築します。
     * 
     * @param packageName
     *            パッケージ名
     * @param serviceClassNameSuffix
     *            サービスクラス名のサフィックス
     * @param componentType String
     */
    public NoS2AbstServiceModelFactory(String packageName,
            String serviceClassNameSuffix, String componentType) {
        if (serviceClassNameSuffix == null) {
            throw new NullPointerException("serviceClassNameSuffix");
        }
        this.packageName = packageName;
        this.serviceClassNameSuffix = serviceClassNameSuffix;
        this.componentType = componentType;

    }

    public NoS2AbstServiceModel getAbstServiceModel() {
        NoS2AbstServiceModel noS2AbstServiceModel = new NoS2AbstServiceModel();
        noS2AbstServiceModel.setPackageName(packageName);
//i        abstServiceModel.setShortClassName("Abstract" + serviceClassNameSuffix);
        noS2AbstServiceModel.setShortClassName("NoS2Abstract" + serviceClassNameSuffix);
      //i
        noS2AbstServiceModel.setComponentType(componentType);
        doImportName(noS2AbstServiceModel);
        doGeneratedInfo(noS2AbstServiceModel);
        return noS2AbstServiceModel;
    }

    /**
     * インポート名を処理します。
     * 
     * @param noS2AbstServiceModel
     *            抽象サービスモデル
     */
    protected void doImportName(NoS2AbstServiceModel noS2AbstServiceModel) {
//i        classModelSupport.addImportName(abstServiceModel,
//i                S2AbstractService.class);
        classModelSupport.addImportName(noS2AbstServiceModel, Generated.class);
    }

    /**
     * 生成情報を処理します。
     * 
     * @param noS2AbstServiceModel
     *            抽象サービスモデル
     */
    protected void doGeneratedInfo(NoS2AbstServiceModel noS2AbstServiceModel) {
        generatedModelSupport.fillGeneratedInfo(this, noS2AbstServiceModel);
    }
    
}
