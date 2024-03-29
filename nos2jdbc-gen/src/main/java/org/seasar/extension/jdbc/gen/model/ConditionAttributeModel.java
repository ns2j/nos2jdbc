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
 * 条件クラスの属性モデルです。
 * 
 * @author taedium
 */
public class ConditionAttributeModel {

    /** 名前 */
    protected String name;

    /** 属性のクラス */
    protected Class<?> attributeClass;

    /** 条件クラス */
    protected Class<?> conditionClass;

    /** パラメタ化される場合{@code true} */
    protected boolean parameterized;

    /**
     * 名前を返します。
     * 
     * @return 名前
     */
    public String getName() {
        return name;
    }

    /**
     * 名前を設定します。
     * 
     * @param name
     *            名前
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 属性のクラスを返します。
     * 
     * @return 属性のクラス
     */
    public Class<?> getAttributeClass() {
        return attributeClass;
    }

    /**
     * 属性のクラスを設定します。
     * 
     * @param attributeClass
     *            属性のクラス
     */
    public void setAttributeClass(Class<?> attributeClass) {
        this.attributeClass = attributeClass;
    }

    /**
     * 条件クラスを返します。
     * 
     * @return 条件クラス
     */
    public Class<?> getConditionClass() {
        return conditionClass;
    }

    /**
     * 条件クラスを設定します。
     * 
     * @param conditionClass
     *            条件クラス
     */
    public void setConditionClass(Class<?> conditionClass) {
        this.conditionClass = conditionClass;
    }

    /**
     * パラメタ化されている場合{@code true}を返します。
     * 
     * @return boolean
     */
    public boolean isParameterized() {
        return parameterized;
    }

    /**
     * パラメタ化されている場合{@code true}を設定します。
     * 
     * @param parameterized
     *            パラメタ化されている場合{@code true}
     */
    public void setParameterized(boolean parameterized) {
        this.parameterized = parameterized;
    }

}
