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
package org.seasar.extension.jdbc.gen.model;

/**
 * サービステストモデルです。
 * 
 * @author taedium
 */
public class ServiceTestModel extends TestClassModel {

    /** サービスクラスの単純名 */
    protected String shortServiceClassName;
  //i
    protected String serviceClassNameSuffix;

    /** 設定ファイルのパス */
//i    protected String configPath;
    /**
     * サービスクラスの単純名を返します。
     * 
     * @return サービスクラスの単純名
     */
    public String getShortServiceClassName() {
        return shortServiceClassName;
    }

    /**
     * サービスクラスの単純名を設定します。
     * 
     * @param shortServiceClassName
     *            サービスクラスの単純名
     */
    public void setShortServiceClassName(String shortServiceClassName) {
        this.shortServiceClassName = shortServiceClassName;
    }

  //i
    public String getServiceClassNameSuffix() {
	return serviceClassNameSuffix;
    }
//i
    public void setServiceClassNameSuffix(String serviceClassNameSuffix) {
	this.serviceClassNameSuffix = serviceClassNameSuffix;
    }

    /**
     * 設定ファイルのパスを返します。
     * 
     * @return 設定ファイルのパス
     */
//i    public String getConfigPath() {
//i        return configPath;
//i    }

    /**
     * 設定ファイルのパスを設定します。
     * 
     * @param configPath
     *            設定ファイルのパス
     */
//i    public void setConfigPath(String configPath) {
//i        this.configPath = configPath;
//i    }
}
