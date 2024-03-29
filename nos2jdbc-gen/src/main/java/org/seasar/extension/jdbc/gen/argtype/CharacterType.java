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
package org.seasar.extension.jdbc.gen.argtype;

import org.seasar.framework.conversion.StringConversionUtil;
import org.seasar.framework.util.StringUtil;

/**
 * {@link Character}を扱う{@link ArgumentType}の実装クラスです。
 * 
 * @author taedium
 */
public class CharacterType implements ArgumentType<Character> {

    public Character toObject(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        String s = value.replace("\\u", "");
        int ch = Integer.valueOf(s);
        return (char) ch;
    }

    public String toText(Character value) {
        if (value == null) {
            return "";
        }
        char ch = value.charValue();
        return "\\u" + StringConversionUtil.toString((int) ch, "0000");
    }

}
