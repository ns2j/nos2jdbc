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
package org.seasar.extension.jdbc.mapper;

import java.lang.reflect.Field;

import org.seasar.extension.jdbc.PropertyMapper;
import org.seasar.extension.jdbc.PropertyMeta;

/**
 * {@link PropertyMapper}の実装クラスです。
 * 
 * @author higa
 * 
 */
public class PropertyMapperImpl implements PropertyMapper {

	/**
	 * プロパティメタデータです。
	 */
	protected PropertyMeta propertyMeta;

	/**
	 * プロパティのインデックスです。
	 */
	protected int propertyIndex;

	/**
	 * {@link PropertyMapperImpl}を作成します。
	 * 
	 * @param propertyMeta
	 *            プロパティメタデータ
	 * @param propertyIndex
	 *            プロパティインデックス
	 */
	public PropertyMapperImpl(PropertyMeta propertyMeta, int propertyIndex) {
		this.propertyMeta = propertyMeta;
		this.propertyIndex = propertyIndex;
	}
	
	@Override
	public void map(Object entity, Object[] values) {
		setFieldValue(entity, values[propertyIndex]);
	}

	/**
	 * フィールドの値を設定します。
	 * 
	 * @param entity
	 *            エンティティ
	 * @param value
	 *            プロパティの値
	 */
	protected void setFieldValue(Object entity, Object value) {
		if (value == null) {
			return;
		}
		propertyMeta.setValue(entity, value);
	}

	/**
	 * フィールドを返します。
	 * 
	 * @return フィールド
	 */
	@Override
	public Field getField() {
		return propertyMeta.getField();
	}

	/**
	 * プロパティインデックスを返します。
	 * 
	 * @return プロパティインデックス
	 */
	public int getPropertyIndex() {
		return propertyIndex;
	}
}
