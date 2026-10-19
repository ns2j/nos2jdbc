# nos2jdbc-util

`nos2jdbc-util` is the utility module of [nos2jdbc](https://github.com/ns2j/nos2jdbc).

It provides common utility classes used by nos2jdbc, including JavaBeans metadata access, Bean manipulation, Java Record conversion, collection utilities, file and text operations, and JDBC-related helpers.

The module is based on utility components from the Seasar framework and is used by `nos2jdbc-core` and other modules in the project.

## Requirements

- Java 17 or later
- Maven

## Installation

Add the following dependency to your Maven project:

```xml
<dependency>
    <groupId>com.github.ns2j</groupId>
    <artifactId>nos2jdbc-util</artifactId>
    <version>2.0.0</version>
</dependency>
```

The module uses OGNL for expression-related functionality. SLF4J and Jakarta Transactions are declared as provided dependencies, so applications should supply the dependencies required by their runtime environment.

## Features

### JavaBeans utilities

The `org.seasar.framework.beans` packages provide JavaBeans metadata access and related functionality.

Utilities include:

- `BeanDesc` and related metadata abstractions
- Bean property access and manipulation
- Bean copying and conversion
- `BeanMap` for working with bean properties as a map
- Factory and implementation classes for Bean metadata

These utilities help other modules inspect and manipulate Java objects without duplicating reflection-related code.

### Java Record utilities

The `org.seasar.framework.beans.util.Records` class provides conversion between JavaBeans and Java Records.

Available methods include:

```java
Records.createRecord(MyRecord.class, bean);
Records.createRecordList(MyRecord.class, beans);
Records.copyToBean(record, bean);
```

For example, a JavaBean can be converted into an immutable Record for use as a data-transfer object.

```java
public record EmployeeDto(Integer id, String name) {
}
```

```java
EmployeeDto dto =
    Records.createRecord(EmployeeDto.class, employee);
```

The conversion utilities also support nested objects and lists. The source and target property structures should be compatible with the conversion being performed.

### General-purpose utilities

The `org.seasar.framework.util` package provides reusable utilities for common programming tasks.

Examples include:

- `Pair` — represents a pair of values
- `MapUtil` — map-related operations
- `FileUtil` — file operations
- `TextUtil` — text-related operations
- `ReaderUtil` — reader-related operations
- `ResultSetUtil` — JDBC `ResultSet` utilities
- `UtcUtil` — UTC and date-time-related operations
- `LruHashMap` — a least-recently-used map implementation

The module also contains supporting utilities for resource handling, exceptions, and other common framework operations.

### Exception classes

The `org.seasar.framework.exception` package provides exception classes used by the framework, including runtime exceptions for I/O and SQL-related errors.

These classes allow the library to represent certain checked exceptions through framework-specific runtime exception types.

## Relationship to nos2jdbc-core

`nos2jdbc-util` is a foundational module used by `nos2jdbc-core`.

Applications that use `nos2jdbc-core` normally receive this module as a transitive Maven dependency. Add `nos2jdbc-util` directly when your application needs to use its utility APIs independently.

For ORM functionality and database access, see:

- [nos2jdbc-core](https://github.com/ns2j/nos2jdbc/tree/master/nos2jdbc-core)

## Related projects

- [nos2jdbc](https://github.com/ns2j/nos2jdbc) — main project
- [nos2jdbc-core](https://github.com/ns2j/nos2jdbc/tree/master/nos2jdbc-core) — ORM core module
- [nos2jdbc-gen](https://github.com/ns2j/nos2jdbc/tree/master/nos2jdbc-gen) — code generation module
- [nos2jdbc-tutorial](https://github.com/ns2j/nos2jdbc-tutorial) — tutorials and examples

## License

Apache License 2.0
