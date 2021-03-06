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
package org.seasar.extension.dataset;

/**
 * 
 * DataSet機能で使われる定数を定義します。
 * 
 * @author higa
 * 
 */
public interface DataSetConstants {

    /**
     * 日付のフォーマットです。
     */
    String DATE_FORMAT = "yyyy/MM/dd";

    /**
     * Base64のフォーマットです。
     */
    String BASE64_FORMAT = "\\B\\:@";
    String BASE64_FORMAT_LIBREOFFICE = "\"B:\"@";

}
