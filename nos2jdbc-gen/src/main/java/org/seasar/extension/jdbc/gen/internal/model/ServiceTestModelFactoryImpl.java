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

import javax.annotation.Generated;

//i import org.junit.Assert;
//i import org.junit.runner.RunWith;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.gen.model.ServiceTestModel;
import org.seasar.extension.jdbc.gen.model.ServiceTestModelFactory;
//i import org.seasar.extension.unit.S2TestCase;
//i import org.seasar.framework.unit.Seasar2;

/**
 * {@link ServiceTestModelFactory}の実装クラスです。
 * 
 * @author taedium
 */
public class ServiceTestModelFactoryImpl implements ServiceTestModelFactory {
    /** パッケージ名、デフォルトパッケージの場合は{@code null} */
    protected String packageName;
  //i
    protected String rootPackageName;

    /** サービスクラス名のサフィックス */
    protected String serviceClassNameSuffix;

    /** テストクラス名のサフィックス */
    protected String testClassNameSuffix;

    /** 設定ファイルのパス */
    protected String configPath;

    /** S2JUnit4を使用する場合{@code true}、S2Unitを使用する場合{@code false} */
//i    protected boolean useS2junit4;
//i
    protected String componentType = "none";
    
    /** クラスモデルのサポート */
    protected ClassModelSupport classModelSupport = new ClassModelSupport();

    /** 生成モデルのサポート */
    protected GeneratedModelSupport generatedModelSupport = new GeneratedModelSupport();

    /**
     * インスタンスを構築します。
     * 
     * @param packageName
     *            パッケージ名、デフォルトパッケージの場合は{@code null}
     * @param serviceClassNameSuffix
     *            サービスクラス名のサフィックス
     * @param testClassNameSuffix
     *            テストクラス名のサフィックス
     * @param rootPackageName
     *            ルートパッケージ名
     * @param componentType
     *            コンポーネントタイプ
     */
    public ServiceTestModelFactoryImpl(String packageName,
            String serviceClassNameSuffix, String testClassNameSuffix,
            String rootPackageName, String componentType) {
//i        if (configPath == null) {
//i            throw new NullPointerException("configPath");
//i        }
        if (serviceClassNameSuffix == null) {
            throw new NullPointerException("serviceClassNameSuffix");
        }
        if (testClassNameSuffix == null) {
            throw new NullPointerException("testClassNameSuffix");
        }
//i
        if (rootPackageName == null) {
            throw new NullPointerException("rootPackageName");
        }
//i        this.configPath = configPath;
        this.packageName = packageName;
        this.serviceClassNameSuffix = serviceClassNameSuffix;
        this.testClassNameSuffix = testClassNameSuffix;
//i        this.useS2junit4 = useS2junit4;
//i        
        this.rootPackageName = rootPackageName;
        this.componentType = componentType;
    }

    public ServiceTestModel getServiceTestModel(EntityMeta entityMeta) {
        ServiceTestModel serviceTestModel = new ServiceTestModel();
//i        serviceTestModel.setConfigPath(configPath);
        serviceTestModel.setPackageName(packageName);
        //i
        serviceTestModel.setRootPackageName(rootPackageName);
        String shortServiceClassName = entityMeta.getEntityClass()
                .getSimpleName()
                + serviceClassNameSuffix;
        serviceTestModel.setShortServiceClassName(shortServiceClassName);
        serviceTestModel.setShortClassName(shortServiceClassName
                + testClassNameSuffix);
//i        serviceTestModel.setUseS2junit4(useS2junit4);
        //i
        serviceTestModel.setServiceClassNameSuffix(serviceClassNameSuffix);
        serviceTestModel.setComponentType(componentType);
        doImportName(serviceTestModel, entityMeta);
        doGeneratedInfo(serviceTestModel, entityMeta);
        return serviceTestModel;
    }

    /**
     * インポート名を処理します。
     * 
     * @param serviceTestModel
     *            サービステストモデル
     * @param entityMeta
     *            エンティティメタデータ
     */
    protected void doImportName(ServiceTestModel serviceTestModel,
            EntityMeta entityMeta) {
        classModelSupport.addImportName(serviceTestModel, Generated.class);
        /*i
        if (useS2junit4) {
            classModelSupport.addImportName(serviceTestModel, RunWith.class);
//i            classModelSupport.addImportName(serviceTestModel, Seasar2.class);
            classModelSupport.addStaticImportName(serviceTestModel,
                    Assert.class);
        } else {
//i            classModelSupport.addImportName(serviceTestModel, S2TestCase.class);
        }
        */
    }

    /**
     * 生成情報を処理します。
     * 
     * @param serviceTestModel
     *            サービステストモデル
     * @param entityMeta
     *            エンティティメタデータ
     */
    protected void doGeneratedInfo(ServiceTestModel serviceTestModel,
            EntityMeta entityMeta) {
        generatedModelSupport.fillGeneratedInfo(this, serviceTestModel);
    }
}
