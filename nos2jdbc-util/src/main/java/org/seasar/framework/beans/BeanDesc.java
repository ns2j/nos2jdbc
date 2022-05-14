/*
 * Copyright 2004-2015 the Seasar Foundatiovn and the Others.
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
package org.seasar.framework.beans;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Bean(JavaBeans)を扱うためのインターフェースです。
 * 
 * @author higa
 * 
 */
public interface BeanDesc {

    /**
     * Beanのクラスを返します。
     * 
     * @return bean class
     */
    Class<?> getBeanClass();

    /**
     * {@link PropertyDesc}を持っているかどうかを返します。
     * 
     * @param propertyName propertyName
     * @return {@link PropertyDesc}を持っているかどうか
     */
    boolean hasPropertyDesc(String propertyName);

    /**
     * {@link PropertyDesc}を返します。
     * 
     * @param propertyName propertyName
     * @return {@link PropertyDesc}
     * @throws PropertyNotFoundRuntimeException
     *             {@link PropertyDesc}が見つからない場合
     */
    PropertyDesc getPropertyDesc(String propertyName)
            throws PropertyNotFoundRuntimeException;

    /**
     * {@link PropertyDesc}を返します。
     * 
     * @param index index
     * @return {@link PropertyDesc}
     */
    PropertyDesc getPropertyDesc(int index);

    /**
     * {@link PropertyDesc}の数を返します。
     * 
     * @return int
     */
    int getPropertyDescSize();

    /**
     * {@link Field}を持っているかどうかを返します。
     * 
     * @param fieldName fieldName
     * @return {@link Field}を持っているかどうか
     */
    boolean hasField(String fieldName);

    /**
     * {@link Field}を返します。
     * 
     * @param fieldName fieldName
     * @return {@link Field}
     * @throws FieldNotFoundRuntimeException {@link FieldNotFoundRuntimeException}
     */
    Field getField(String fieldName) throws FieldNotFoundRuntimeException;

    /**
     * {@link Field}を返します。
     * 
     * @param index index
     * @return {@link Field}
     */
    Field getField(int index);

    /**
     * {@link Field}の値を返します。
     * 
     * @param fieldName fieldName
     * @param target target
     * @return {@link Field}の値
     * @throws FieldNotFoundRuntimeException
     *             {@link Field}が見つからない場合
     */
    Object getFieldValue(String fieldName, Object target)
            throws FieldNotFoundRuntimeException;

    /**
     * {@link Field}の数を返します。
     * 
     * @return {@link Field}の数
     */
    int getFieldSize();

    /**
     * 新しいインスタンスを作成します。
     * 
     * @param args args
     * @return 新しいインスタンス
     * @throws ConstructorNotFoundRuntimeException {@link ConstructorNotFoundRuntimeException}
     */
    Object newInstance(Object[] args)
            throws ConstructorNotFoundRuntimeException;

    /**
     * 引数に応じた{@link Constructor}を返します。
     * 
     * @param args args
     * @return 引数に応じた{@link Constructor}
     * @throws ConstructorNotFoundRuntimeException {@link ConstructorNotFoundRuntimeException}
     */
    Constructor<?> getSuitableConstructor(Object[] args)
            throws ConstructorNotFoundRuntimeException;

    /**
     * 型に応じた{@link Constructor}を返します。
     * 
     * @param paramTypes paramTypes
     * @return 型に応じた{@link Constructor}
     */
    Constructor<?> getConstructor(Class<?>[] paramTypes);

    /**
     * Diiguでエンハンスした{@link Constructor}のパラメータ名の配列を返します。
     * 
     * @param paramTypes paramTypes
     * @return パラメータ名の配列
     */
    String[] getConstructorParameterNames(final Class<?>[] paramTypes);

    /**
     * Diiguでエンハンスした{@link Constructor}のパラメータ名の配列を返します。
     * 
     * @param constructor constructor
     * @return パラメータ名の配列
     */
    String[] getConstructorParameterNames(Constructor<?> constructor);

    /**
     * ターゲットのメソッドを呼び出します。
     * 
     * @param target target
     * @param methodName methodName
     * @param args args
     * @return 戻り値
     * @throws MethodNotFoundRuntimeException
     *             {@link Method}が見つからない場合。
     */
    Object invoke(Object target, String methodName, Object[] args)
            throws MethodNotFoundRuntimeException;

    /**
     * {@link Method}を返します。
     * 
     * @param methodName methodName
     * @return {@link Method}
     * @throws MethodNotFoundRuntimeException
     *             {@link Method}が見つからない場合。
     */
    Method getMethod(String methodName) throws MethodNotFoundRuntimeException;

    /**
     * {@link Method}を返します。
     * 
     * @param methodName methodName
     * @param paramTypes paramTypes
     * @return {@link Method}
     * @throws MethodNotFoundRuntimeException
     *             {@link Method}が見つからない場合。
     */
    Method getMethod(String methodName, Class<?>[] paramTypes)
            throws MethodNotFoundRuntimeException;

    /**
     * {@link Method}を返します。見つからない場合は、nullを返します。
     * 
     * @param methodName methodName
     * @return {@link Method}
     */
    Method getMethodNoException(String methodName);

    /**
     * {@link Method}を返します。見つからない場合は、nullを返します。
     * 
     * @param methodName methodName
     * @param paramTypes paramTypes
     * @return {@link Method}
     */
    Method getMethodNoException(String methodName, Class<?>[] paramTypes);

    /**
     * {@link Method}の配列を返します。
     * 
     * @param methodName methodName
     * @return array of Methods
     * @throws MethodNotFoundRuntimeException
     *             {@link Method}が見つからない場合。
     */
    Method[] getMethods(String methodName)
            throws MethodNotFoundRuntimeException;

    /**
     * {@link Method}があるかどうか返します。
     * 
     * @param methodName methodName
     * @return {@link Method}があるかどうか
     */
    boolean hasMethod(String methodName);

    /**
     * メソッド名の配列を返します。
     * 
     * @return array of method names
     */
    String[] getMethodNames();

    /**
     * {@link Method}のパラメータ名の配列を返します。
     * 
     * @param methodName methodName
     * @param paramTypes paramTypes
     * @return {@link Method}のパラメータ名の配列
     * @throws MethodNotFoundRuntimeException
     *             {@link Method}が見つからない場合。
     * @throws IllegalDiiguRuntimeException
     *             Diiguでエンハンスされていない場合。
     */
    String[] getMethodParameterNames(String methodName, final Class<?>[] paramTypes)
            throws MethodNotFoundRuntimeException, IllegalDiiguRuntimeException;

    /**
     * {@link Method}のパラメータ名の配列を返します。
     * 
     * @param methodName methodName
     * @param paramTypes paramTypes
     * @return {@link Method}のパラメータ名の配列
     * @throws MethodNotFoundRuntimeException
     *             {@link Method}が見つからない場合。
     */
    String[] getMethodParameterNamesNoException(String methodName,
            final Class<?>[] paramTypes) throws MethodNotFoundRuntimeException;

    /**
     * {@link Method}のパラメータ名の配列を返します。
     * 
     * @param method method
     * @return {@link Method}のパラメータ名の配列
     * @throws MethodNotFoundRuntimeException
     *             {@link Method}が見つからない場合。
     * @throws IllegalDiiguRuntimeException
     *             Diiguでエンハンスされていない場合。
     */
    String[] getMethodParameterNames(Method method)
            throws MethodNotFoundRuntimeException, IllegalDiiguRuntimeException;

    /**
     * {@link Method}のパラメータ名の配列を返します。
     * 
     * @param method method
     * @return {@link Method}のパラメータ名の配列
     * @throws MethodNotFoundRuntimeException
     *             {@link Method}が見つからない場合。
     */
    String[] getMethodParameterNamesNoException(Method method)
            throws MethodNotFoundRuntimeException;
}