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
package org.seasar.extension.jdbc.where.condition;

/**
 * Nullableな<code>String</code>型のプロパティの問い合わせ条件を表現するクラスです。
 * 
 * @author koichik
 * @param <CONDITION>
 *            このプロパティを持つエンティティの問い合わせ条件を構築するクラス
 */
public class NullableStringCondition<CONDITION extends AbstractEntityCondition<CONDITION>>
        extends NullableCondition<CONDITION, String> {

    /**
     * インスタンスを構築します。
     * 
     * @param propertyName
     *            プロパティ名
     * @param condition
     *            このプロパティを持つエンティティの問い合わせ条件
     */
    public NullableStringCondition(final String propertyName,
            final CONDITION condition) {
        super(propertyName, condition);
    }

    /**
     * <code>like</code>の条件を追加します。
     * 
     * @param value
     *            条件となる値
     * @return このプロパティを持つエンティティの問い合わせ条件
     */
    public CONDITION like(final String value) {
        condition.where.like(condition.prefix + propertyName, value);
        return condition;
    }

    /**
     * <code>not like</code>の条件を追加します。
     * 
     * @param value
     *            条件となる値
     * @return このプロパティを持つエンティティの問い合わせ条件
     */
    public CONDITION notLike(final String value) {
        condition.where.notLike(condition.prefix + propertyName, value);
        return condition;
    }

    /**
     * <code>like '?%'</code>の条件を追加します。
     * 
     * @param value
     *            条件となる値
     * @return このプロパティを持つエンティティの問い合わせ条件
     */
    public CONDITION starts(final String value) {
        condition.where.starts(condition.prefix + propertyName, value);
        return condition;
    }

    /**
     * <code>not like '?%'</code>の条件を追加します。
     * 
     * @param value
     *            条件となる値
     * @return このプロパティを持つエンティティの問い合わせ条件
     */
    public CONDITION notStarts(final String value) {
        condition.where.notStarts(condition.prefix + propertyName, value);
        return condition;
    }

    /**
     * <code>like '%?'</code>の条件を追加します。
     * 
     * @param value
     *            条件となる値
     * @return このプロパティを持つエンティティの問い合わせ条件
     */
    public CONDITION ends(final String value) {
        condition.where.ends(condition.prefix + propertyName, value);
        return condition;
    }

    /**
     * <code>not like '%?'</code>の条件を追加します。
     * 
     * @param value
     *            条件となる値
     * @return このプロパティを持つエンティティの問い合わせ条件
     */
    public CONDITION notEnds(final String value) {
        condition.where.notEnds(condition.prefix + propertyName, value);
        return condition;
    }

    /**
     * <code>like '%?%'</code>の条件を追加します。
     * 
     * @param value
     *            条件となる値
     * @return このプロパティを持つエンティティの問い合わせ条件
     */
    public CONDITION contains(final String value) {
        condition.where.contains(condition.prefix + propertyName, value);
        return condition;
    }

    /**
     * <code>not like '%?%'</code>の条件を追加します。
     * 
     * @param value
     *            条件となる値
     * @return このプロパティを持つエンティティの問い合わせ条件
     */
    public CONDITION notContains(final String value) {
        condition.where.notContains(condition.prefix + propertyName, value);
        return condition;
    }

}
