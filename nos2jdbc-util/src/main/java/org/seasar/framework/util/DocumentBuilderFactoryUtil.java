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
package org.seasar.framework.util;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.seasar.framework.exception.ParserConfigurationRuntimeException;

/**
 * {@link DocumentBuilderFactory}の用のユーティリティクラスです。
 * 
 * @author higa
 * 
 */
public class DocumentBuilderFactoryUtil {

    /**
     * インスタンスを構築します。
     */
    protected DocumentBuilderFactoryUtil() {
    }

    /**
     * 新しい {@link DocumentBuilderFactory}のインスタンスを返します。
     * 
     * @return 新しい {@link DocumentBuilderFactory}のインスタンス
     */
    public static DocumentBuilderFactory newInstance() {
        return DocumentBuilderFactory.newInstance();
    }

    /**
     * 新しい {@link DocumentBuilder}を作成します。
     * 
     * @return 新しい {@link DocumentBuilder}
     */
    public static DocumentBuilder newDocumentBuilder() {
        try {
            return newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new ParserConfigurationRuntimeException(e);
        }
    }
}