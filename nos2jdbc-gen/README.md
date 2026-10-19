# nos2jdbc-gen

`nos2jdbc-gen` は、[nos2jdbc](https://github.com/ns2j/nos2jdbc) のコード生成・開発支援モジュールです。

S2JDBC-Gen をベースに、データベースのメタデータや設定情報から Java のソースコード、DDL などを生成する機能を提供します。Entity の作成やデータベース開発に伴う定型作業を減らし、nos2jdbc を利用するアプリケーションの開発を支援します。

## Requirements

- Java 17 or later
- Maven
- 対象データベースに対応した JDBC ドライバー
- 生成対象のデータベースやテーブルに応じた設定

## Installation

`pom.xml` に次の依存関係を追加してください。

```xml
<dependency>
    <groupId>com.github.ns2j</groupId>
    <artifactId>nos2jdbc-gen</artifactId>
    <version>2.0.0</version>
</dependency>
```

`nos2jdbc-gen` は `nos2jdbc-core` に依存しています。Maven を使用する場合、推移的依存関係として解決されます。

## Features

### Code generation

主なコード生成タスクは次のとおりです。

| Task | Description |
|---|---|
| `gen-entity` | Entity クラスを生成 |
| `gen-entitytest` | Entity のテストクラスを生成 |
| `gen-service` | Service クラスを生成 |
| `gen-servicetest` | Service のテストクラスを生成 |
| `gen-names` | テーブル名やカラム名などを扱う Names クラスを生成 |
| `gen-condition` | 検索条件に関連するクラスを生成 |
| `gen-sqlfileconstants` | SQL ファイルに関する定数クラスを生成 |
| `gen-ddl` | DDL を生成 |

生成されるファイルや内容は、設定およびテンプレートによって異なります。

### Database metadata

データベースのメタデータを利用して、テーブルやカラムの定義をコード生成に反映できます。

既存データベースを利用するアプリケーションでは、データベース構造に対応する Entity の作成などに活用できます。

`dump-dbmeta` タスクでは、データベースメタデータの出力を行えます。

### SQL execution

`exec-sql` タスクを利用して、設定された SQL の実行を行えます。

データベース接続情報や SQL の指定方法など、実行に必要な設定は利用する環境に合わせて行ってください。

### Template-based generation

コード生成には FreeMarker を使用しています。

テンプレートを利用することで、生成されるソースコードの形式を管理できます。プロジェクトのコーディング規約やアプリケーション構成に合わせて生成結果を調整する場合に役立ちます。

## Configuration and usage

実際にタスクを実行するには、データベース接続、生成対象、出力先、パッケージ名などの設定が必要です。

タスクの具体的な設定方法や実行手順については、チュートリアルリポジトリを参照してください。

**[nos2jdbc-tutorial](https://github.com/ns2j/nos2jdbc-tutorial)**

チュートリアルには Spring Boot、Kotlin + Spring Boot、WildFly のサンプルがあり、アプリケーション開発や DDL 生成などの手順を確認できます。

## Related projects

- [nos2jdbc](https://github.com/ns2j/nos2jdbc) — メインプロジェクト
- [nos2jdbc-core](https://github.com/ns2j/nos2jdbc/tree/master/nos2jdbc-core) — ORM コアモジュール
- [nos2jdbc-tutorial](https://github.com/ns2j/nos2jdbc-tutorial) — チュートリアルとサンプル
- [nos2jdbc-it](https://github.com/ns2j/nos2jdbc-it) — 統合テスト

## License

Apache License 2.0
