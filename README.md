# nos2jdbc

[![Maven Central](https://img.shields.io/maven-central/v/com.github.ns2j/nos2jdbc-core.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/com.github.ns2j/nos2jdbc-core)
![Java 17+](https://img.shields.io/badge/Java-17%2B-blue.svg)
[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## No Seasar2 Container's S2JDBC

nos2jdbc は、S2JDBC から Seasar2 Container への依存を取り除き、Spring Boot や Jakarta EE などの環境で利用できるようにした JDBC ベースの ORM ライブラリです。

S2JDBC の API や SQL 自動生成機能を引き継ぎ、現代の Java 環境で利用できるようにしています。また、独自の機能拡張も行っています。

- **S2JDBC 由来の ORM 機能** — Entity を使った CRUD、条件指定、JOIN、SQL 自動生成
- **JOIN による N+1 問題への対策** — Entityの定義時でなく、クエリー作成時に`innerJoin()` や `leftOuterJoin()` で関連 Entity を JOIN 取得
- **コード生成** — `nos2jdbc-gen` による Entity、Service、検索条件関連クラス、DDL などの生成
- **型安全なテーブル・カラム参照** — `gen-names` で `Names` クラスを生成し、テーブル名やカラム名を型安全に扱える
- **コンテナ非依存** — Seasar2 Container に依存せず、利用環境に応じて構成可能
- **SQL を活用した柔軟なマッピング** — 任意の SQL の実行結果を Entity にマッピング
- **JPA 系アノテーションの拡張** — `@Embedded`、`@Embeddable`、`@EmbeddedId` に対応
- **Java Record 対応** — Bean と Record の相互変換ユーティリティ

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

プロジェクトの概要は、Qiita の紹介記事「[s2jdbcを他のフレームワークで使えるようにしたnos2jdbc](https://qiita.com/igariy/items/ff0e4e1a82dd65cf078c)」もご覧ください。

実際の設定方法や実行可能なサンプルは、チュートリアルリポジトリを参照してください。

**[nos2jdbc-tutorial](https://github.com/ns2j/nos2jdbc-tutorial)**

次の構成のサンプルを用意しています。

- Spring Boot
- Kotlin + Spring Boot
- WildFly

チュートリアルでは、アプリケーションの実行だけでなく、DDL 生成やマイグレーションなどの開発手順も紹介しています。データソースやトランザクション管理を含む `jdbcManager` の構成・取得方法も、利用する環境に応じてチュートリアルの例を参照してください。

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

### 1. S2JDBC から継承した機能

Entity を起点とした検索、登録、更新、削除、SQL 文字列や SQL ファイルによる問い合わせなど、S2JDBC の API をベースとした機能を利用できます。

生成された `Names` クラスを static import して、関連プロパティを指定した JOIN 検索を記述できます。

```java
import static com.example.entity.EmployeeNames.*;

jdbcManager.from(Employee.class)
           .innerJoin(department())
           .getResultList();
```

`department()` は、例として `Employee` の関連プロパティに対応する `Names` のメソッドを示しています。実際のクラス名やメソッド名は、生成された `Names` クラスに合わせてください。

S2JDBC 由来の JOIN 機能にも対応しています。クエリー作成時に `innerJoin()` や `leftOuterJoin()` を指定することで、関連 Entity を JOIN でまとめて取得できます。関連 Entity を個別に取得するための追加クエリーを減らせるため、N+1 問題への対策として利用できます。S2JDBC には遅延ロードがないため、関連 Entity の取得が暗黙に追加クエリーとして実行されることはなく、意図しない N+1 問題は発生しません。

#### コード生成

`nos2jdbc-gen` は、S2JDBC-Gen をベースにした開発支援モジュールです。

`gen-names` タスクでは、テーブル名やカラム名などを扱う `Names` クラスを生成できます。テーブル名やカラム名を文字列で直接記述する箇所を減らし、コンパイル時のチェックを活用したコードを記述できます。

例えば、`gen-names` で生成した `Names` クラスを static import すると、次のようにカラムをメソッドとして参照して検索条件を記述できます。

```java
import static com.example.entity.EmployeeNames.*;
import static org.seasar.extension.jdbc.operation.Operations.*;

jdbcManager.from(Employee.class)
    .where(eq(salary(), 1000))
    .getResultList();
```

このように、カラム名を文字列で直接指定せず、`salary()` などのメソッドを使って条件を記述できます。

そのほか、Entity、Entity のテストクラス、Service、Service のテストクラス、検索条件関連のクラス、SQL ファイル用の定数クラス、DDL などの生成にも対応しています。

詳細な実行方法や設定については、[nos2jdbc-tutorial](https://github.com/ns2j/nos2jdbc-tutorial) を参照してください。

### 2. nos2jdbc で追加・拡張した機能

#### 2.1. SQL の実行結果を Entity にマッピング

`selectBySql()` や `selectBySqlFile()` を使うと、任意の SQL の結果を Entity にマッピングできます。単純な列のマッピングだけでなく、`@NonAuto` を指定した Entity と関連定義を組み合わせ、SQL の結果を階層的なオブジェクト構造として扱うこともできます。

チュートリアルの Spring Boot サンプルでは、昼食代の明細と会員情報を JOIN する `sum.sql` を `selectBySqlFile()` で実行し、結果を `Ym` とその `itemList` に階層的にマッピングしています。SQL 自体は集計しておらず、`GROUP BY` や集計関数もありません。結果マッピングでは、同じ `y` と `m` を持つ行が、複合 ID を持つ同じ `Ym` に束ねられます。

`Ym` は年月を表し、対応する明細を `itemList` に持つ Entity です。

```java
@Entity
@NonAuto
public class Ym {
    @Id
    public Integer y;
    @Id
    public Integer m;

    @OneToMany(mappedBy = "ym")
    public List<YmItem> itemList;
}
```

明細側の `YmItem` では、SQL の結果に含まれる項目と、親子関係を表すフィールドを定義します。

```java
@Entity
@NonAuto
public class YmItem {
    @Id
    public Long lunchFeeId;

    @OneToOne(mappedBy = "ymItem")
    public YmItemMember member;

    public LocalDate payOn;
    public BigDecimal amount;

    @Transient
    public Ym ym;
}
```

`YmItem.ym` は、`Ym.itemList` から明細をマッピングする際に親への逆参照を設定するためのフィールドです。`mappedBy = "ym"` はこのフィールドを参照し、結果マッパーが親 `Ym` を設定します。この用途では `@Transient` だけで足ります。

```java
@Entity
@NonAuto
public class YmItemMember {
    public Long memberId;
    public String memberName;

    @OneToOne
    @NoFk
    public YmItem ymItem;
}
```

`@OneToOne(mappedBy = "ymItem")` とこの `ymItem` が対応するため、明細から `item.member.memberName` とたどれます。

`@NoFk` は、関連先を結ぶ外部キー列が Entity の通常のカラムプロパティとして存在しない場合に、その列の存在チェックを抑制する指定です。

`YmItem.ym` と違い、`YmItemMember.ymItem` は `@Transient` が付いていないため、`@OneToOne` で独立した関連として扱われます。これは `YmItem.member` の `mappedBy = "ymItem"` が参照するフィールドです。


`nos2jdbc-tutorial-spring/src/main/resources/sql/sum.sql`:

```sql
select year(lf.pay_on) as y, month(lf.pay_on) as m,
  lf.id as lunch_fee_id, lf.member_id, lf.pay_on, lf.amount,
  mem.name as member_name
from lunch_fee as lf
  join member as mem on lf.member_id = mem.id;
```

この SQL ファイルをサービスから指定して結果を取得しています。

```java
public List<Ym> getYm() {
    sqlFilePathPrefix = "sql/";
    return selectBySqlFile(Ym.class, "sum.sql").getResultList();
}
```

取得した結果は、次のように親から明細、明細から会員情報へたどれます。

```java
for (Ym ym : lfService.getYm()) {
    for (YmItem item : ym.itemList) {
        System.out.println(item.member.memberName);
    }
}
```

`y` と `m` は `Ym` の複合 ID、`lunch_fee_id` は `YmItem` の ID です。親 `Ym` と明細 `YmItem` は、`Ym.itemList` の `@OneToMany(mappedBy = "ym")` によって対応付けられます。

実際のマッピングは、[nos2jdbc-tutorial の Spring Boot サンプル](https://github.com/ns2j/nos2jdbc-tutorial/tree/master/nos2jdbc-tutorial-spring) の Entity 定義と SQL を併せて参照してください。

#### 2.2. `@Embedded` / `@Embeddable` / `@EmbeddedId`

JPA の埋め込み型に対応し、Entity 内に値オブジェクトをまとめて定義できます。

```java
@Entity
public class Employee {
    @Id
    public Integer id;

    @Embedded
    public Address address;
}

@Embeddable
public class Address {
    public String city;
    public String street;
}
```

埋め込みプロパティは、埋め込み先のフィールド名を接頭辞として、`__` を区切り文字に平坦化して扱います。例えば、`Employee` の `address.city` は `address__city` に対応します。

複合主キーを値オブジェクトとして表現する `@EmbeddedId` も利用できます。

```java
@Embeddable
public class EmployeeId {
    public Integer companyId;
    public Integer employeeNo;
}

@Entity
public class Employee {
    @EmbeddedId
    public EmployeeId id;

    public String name;
}
```

この例では、埋め込み ID のプロパティは `id__companyId`、`id__employeeNo` のように扱われます。

#### 2.3. JSON シリアライズ時の循環参照対策

双方向の関連を持つ Entity を JSON に変換すると、親から子、子から親へと参照が循環することがあります。この問題に対応するため、逆参照を構築せずに結果を取得する API を用意しています。

```java
getSingleResultWithoutInverseField()
getResultListWithoutInverseField()
```

JSON API などで Entity を返す際に、関連オブジェクトの循環を避けたい場合に利用できます。

#### 2.4. Java Record と Bean の相互変換

Java Record と従来の JavaBean を相互変換するユーティリティを追加しています。ユーティリティのクラス名は `org.seasar.framework.beans.util.Records` です。

```java
import org.seasar.framework.beans.util.Records;

MyRecord record = Records.createRecord(MyRecord.class, bean);
List<MyRecord> records = Records.createRecordList(MyRecord.class, beans);
Records.copyToBean(record, bean);
```

ネストしたオブジェクトやリストの変換にも対応しています。例えば、可変な Entity や JavaBean から、API のレスポンスに適した不変データモデルである Record を生成できます。

## Integration Tests

データベースごとの SQL やマッピングの動作を検証するため、統合テスト用のリポジトリを用意しています。

**[nos2jdbc-it](https://github.com/ns2j/nos2jdbc-it)**

テスト対象のデータベースや実行プロファイルなどについては、同リポジトリの README と Maven 設定を参照してください。

## Project

- Source code: [ns2j/nos2jdbc](https://github.com/ns2j/nos2jdbc)
- Tutorial: [ns2j/nos2jdbc-tutorial](https://github.com/ns2j/nos2jdbc-tutorial)
- Integration tests: [ns2j/nos2jdbc-it](https://github.com/ns2j/nos2jdbc-it)
- License: Apache License 2.0
