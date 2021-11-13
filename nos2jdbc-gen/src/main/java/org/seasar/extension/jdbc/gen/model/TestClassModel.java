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
package org.seasar.extension.jdbc.gen.model;

/**
 * テストクラスのモデルです。
 * 
 * @author taedium
 */
public class TestClassModel extends ClassModel {

    /** S2JUnit4を使用する場合{@code true}、S2Unitを使用する場合{@code false} */
//i    boolean useS2junit4;
  //i    
    protected String rootPackageName;
//i
    protected String componentType = "none";
//i
    protected String springAppConfig = "";

    /**
     * S2JUnit4を使用する場合{@code true}、S2Unitを使用する場合{@code false}を返します。
     * 
     * @return S2JUnit4を使用する場合{@code true}、S2Unitを使用する場合{@code false}
     */
//i    public boolean isUseS2junit4() {
//i        return useS2junit4;
//i    }

    /**
     * S2JUnit4を使用する場合{@code true}、S2Unitを使用する場合{@code false}を設定します。
     * @return String
     */
//i    public void setUseS2junit4(boolean useS2junit4) {
//i        this.useS2junit4 = useS2junit4;
//i    }
  //i
    public String getRootPackageName() {
        return rootPackageName;
    }
//i
    public void setRootPackageName(String rootPackageName) {
        this.rootPackageName = rootPackageName;
    }
//i
    public String getComponentType() {
        return componentType;
    }
//i
    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }
  //i
    public String getSpringAppConfig() {
        return springAppConfig;
    }
//i
    public void setSpringAppConfig(String springAppConfig) {
        this.springAppConfig = springAppConfig;
    }
}
