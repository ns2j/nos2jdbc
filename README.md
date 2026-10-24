# nos2jdbc

## No Seasar2 Container's S2JDBC

nos2jdbc は、S2JDBC から Seasar2 Container への依存を取り除き、Spring Boot や Jakarta EE などの環境で利用できるようにした JDBC ベースの ORM ライブラリです。

S2JDBC の使いやすい API や SQL 自動生成機能を引き継ぎながら、独自機能の追加や現代の Java 環境への対応を進めています。

- **S2JDBC ベースの ORM** — Entity を使った CRUD、条件指定、JOIN、SQL 自動生成
- **JOIN による N+1 問題への対策** — クエリー作成時に `innerJoin()` や `leftOuterJoin()` を指定し、関連 Entity を JOIN でまとめて取得
- **コンテナ非依存** — Seasar2 Container に依存せず、利用環境に応じて構成可能
- **SQL を活用した柔軟なマッピング** — 任意の SQL の実行結果を Entity にマッピング
- **JPA 系アノテーションの拡張** — `@Embedded`、`@Embeddable`、`@EmbeddedId` に対応
- **Java Record 対応** — Bean と Record の相互変換ユーティリティ
- **コード生成** — `nos2jdbc-gen` による開発支援

## Requirements

- Java 17 以降
- Maven
- 利用するデータベースに対応した JDBC ドライバー

## Modules

| Module | Description |
|---|---|
| `nos2jdbc-core` | JDBC、Entity、SQL 生成、マッピングなどの ORM 本体 |
| `nos2jdbc-gen` | Entity や SQL 利用を支援するコード生成機能 |
| `nos2jdbc-util` | Bean 操作などのユーティリティ |
| `nos2jdbc-test` | テスト支援機能 |

## Getting Started

実際の設定方法や実行可能なサンプルは、チュートリアルリポジトリを参照してください。

**[nos2jdbc-tutorial](https://github.com/ns2j/nos2jdbc-tutorial)**

次の構成のサンプルを用意しています。

- Spring Boot
- Kotlin + Spring Boot
- WildFly

チュートリアルでは、アプリケーションの実行だけでなく、DDL 生成やマイグレーションなどの開発手順も紹介しています。

### Maven

nos2jdbc は Maven Central Repository で公開しています。

利用するモジュールを `pom.xml` に追加してください。例えば、ORM 本体を利用する場合は次のように指定します。

```xml
<dependency>
    <groupId>com.github.ns2j</groupId>
    <artifactId>nos2jdbc-core</artifactId>
    <version>2.0.0</version>
</dependency>
```

コード生成やユーティリティなど、必要に応じて各モジュールを追加してください。

## Features

### 1. S2JDBC の API

Entity を起点とした検索、登録、更新、削除、SQL 文字列や SQL ファイルによる問い合わせなど、S2JDBC の API をベースとした機能を利用できます。

```java
jdbcManager.from(Employee.class)
           .getResultList();
```

JOIN や検索条件を組み合わせた SQL 自動生成にも対応しています。

クエリー作成時に `innerJoin()` や `leftOuterJoin()` を指定することで、関連 Entity を JOIN でまとめて取得できます。関連 Entity を個別に取得するための追加クエリーを繰り返す必要を減らし、**N+1 問題への対策**として利用できます。

### 2. SQL の実行結果を Entity にマッピング

任意の SQL を実行し、その結果を Entity にマッピングできます。

集計 SQL や複雑な JOIN など、ORM の自動生成 SQL だけでは扱いにくい問い合わせにも利用できます。

この機能では、次のアノテーションを利用します。

- `@NonAuto` — 自動マッピングに依存しない Entity の定義
- `@NoFk` — 外部キーに基づく関連の自動解決を行わないための指定

カラム名が重複しないように SQL の SELECT 句を定義してください。

また、集計結果などで ID が `null` の場合でも Entity オブジェクトを生成できるため、通常のテーブル検索以外の用途にも対応できます。

### 3. `@Embedded` / `@Embeddable` / `@EmbeddedId`

JPA の埋め込み型に対応し、Entity 内に値オブジェクトをまとめて定義できます。

```java
@Entity
public class Employee {
    @Id
    public Integer id;

    @Embedded
    public Address address;
}
```

```java
@Embeddable
public class Address {
    public String city;
    public String street;
}
```

埋め込みプロパティは、内部的には `__` を区切り文字として平坦化して扱います。例えば `address.city` は `address__city` に対応します。

複合主キーを値オブジェクトとして表現する `@EmbeddedId` にも対応しています。

### 4. JSON シリアライズ時の循環参照対策

双方向の関連を持つ Entity を JSON に変換すると、親から子、子から親へと参照が循環することがあります。

この問題に対応するため、逆参照を構築せずに結果を取得する API を追加しています。

```java
getSingleResultWithoutInverseField()
getResultListWithoutInverseField()
```

JSON API などで Entity を返す際に、関連オブジェクトの循環を避けたい場合に利用できます。

### 5. Java Record と Bean の相互変換

Java Record と従来の JavaBean を相互変換するユーティリティを追加しています。

```java
Records.createRecord(MyRecord.class, bean);
Records.createRecordList(MyRecord.class, beans);
Records.copyToBean(record, bean);
```

ネストしたオブジェクトやリストの変換にも対応しています。

例えば、可変な Entity や JavaBean から、API のレスポンスに適した不変データモデルである Record を生成できます。

### 6. コード生成

`nos2jdbc-gen` は、S2JDBC のコード生成機能をベースにした開発支援モジュールです。

詳細な実行方法や設定については、[nos2jdbc-tutorial](https://github.com/ns2j/nos2jdbc-tutorial) を参照してください。

## Integration Tests

データベースごとの SQL やマッピングの動作を検証するため、統合テスト用のリポジトリを用意しています。

**[nos2jdbc-it](https://github.com/ns2j/nos2jdbc-it)**

テスト対象のデータベースや実行プロファイルなどについては、同リポジトリの README と Maven 設定を参照してください。

## Project

- Source code: [ns2j/nos2jdbc](https://github.com/ns2j/nos2jdbc)
- Tutorial: [ns2j/nos2jdbc-tutorial](https://github.com/ns2j/nos2jdbc-tutorial)
- Integration tests: [ns2j/nos2jdbc-it](https://github.com/ns2j/nos2jdbc-it)
- License: Apache License 2.0
