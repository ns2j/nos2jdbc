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
package org.seasar.extension.jdbc.gen.internal.desc;

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.EntityMetaFactory;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.gen.desc.DatabaseDesc;
import org.seasar.extension.jdbc.gen.desc.TableDesc;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.meta.EntityMetaReader;
import org.seasar.extension.jdbc.gen.provider.ValueTypeProvider;

/**
 * {@link DatabaseDescFactory}の実装です。
 * 
 * @author taedium
 */
public class DatabaseDescFactory {

    /** エンティティメタデータのファクトリ */
    protected EntityMetaFactory entityMetaFactory;

    /** エンティティメタデータのリーダ */
    protected EntityMetaReader entityMetaReader;

    /** 方言 */
    protected GenDialect dialect;

    /** {@link ValueType}の提供者 */
    protected ValueTypeProvider valueTypeProvider;

    /** 関連を外部キーとみなす場合{@code true}、みなさない場合{@code false} */
    protected boolean regardRelationshipAsFk;

    /** テーブル記述のファクトリ */
    protected TableDescFactory tableDescFactory;

    /**
     * インスタンスを構築します。
     * 
     * @param entityMetaFactory
     *            エンティティメタデータのファクトリ
     * @param entityMetaReader
     *            エンティティメタデータのリーダ
     * @param dialect
     *            方言
     * @param valueTypeProvider
     *            {@link ValueType}の提供者
     * @param regardRelationshipAsFk
     *            関連を外部キーとみなす場合{@code true}、みなさない場合{@code false}
     */
    public DatabaseDescFactory(EntityMetaFactory entityMetaFactory,
            EntityMetaReader entityMetaReader, GenDialect dialect,
            ValueTypeProvider valueTypeProvider, boolean regardRelationshipAsFk) {
        if (entityMetaFactory == null) {
            throw new NullPointerException("entityMetaFactory");
        }
        if (entityMetaReader == null) {
            throw new NullPointerException("entityMetaReader");
        }
        if (dialect == null) {
            throw new NullPointerException("dialect");
        }
        if (valueTypeProvider == null) {
            throw new NullPointerException("valueTypeResolver");
        }
        this.entityMetaFactory = entityMetaFactory;
        this.entityMetaReader = entityMetaReader;
        this.dialect = dialect;
        this.valueTypeProvider = valueTypeProvider;
        this.regardRelationshipAsFk = regardRelationshipAsFk;
        this.tableDescFactory = createTableDescFactory();
    }

    public DatabaseDesc getDatabaseDesc() {
        DatabaseDesc databaseDesc = new DatabaseDesc();
        databaseDesc.setFiltered(entityMetaReader.isFiltered());
        for (EntityMeta entityMeta : entityMetaReader.read()) {
            TableDesc tableDesc = tableDescFactory.getTableDesc(entityMeta);
            databaseDesc.addTableDesc(tableDesc);
            for (TableDesc idTableDesc : tableDesc.getIdTableDescList()) {
                databaseDesc.addTableDesc(idTableDesc);
            }
        }
        return databaseDesc;
    }

    /**
     * テーブル記述のファクトリを作成します。
     * 
     * @return テーブル記述のファクトリ
     */
    protected TableDescFactory createTableDescFactory() {
        ColumnDescFactory colFactory = new ColumnDescFactory(dialect, valueTypeProvider);
        PrimaryKeyDescFactory pkFactory = new PrimaryKeyDescFactory(dialect);
        UniqueKeyDescFactory ukFactory = new UniqueKeyDescFactory(dialect);
        ForeignKeyDescFactory fkFactory = new ForeignKeyDescFactory(dialect, entityMetaFactory,
                regardRelationshipAsFk);
        SequenceDescFactory seqFactory = new SequenceDescFactory(dialect, valueTypeProvider);
        IdTableDescFactory idTabFactory = new IdTableDescFactory(dialect, ukFactory);
        return new TableDescFactory(dialect, colFactory, pkFactory,
                ukFactory, fkFactory, seqFactory, idTabFactory);
    }
}
