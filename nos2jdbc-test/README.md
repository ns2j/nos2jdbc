# nos2jdbc-test

`nos2jdbc-test` is a test support module for [nos2jdbc](https://github.com/ns2j/nos2jdbc), a Java JDBC-based ORM derived from S2JDBC without requiring the Seasar2 Container.

This module provides utilities for database testing, including dataset operations and JUnit 5-based test support.

## Features

- **Dataset support** — Read, write, and compare tabular test data.
- **Database test utilities** — Support for preparing and verifying database state during tests.
- **JUnit 5 integration** — Test extensions for managing test execution and database-related operations.
- **Reusable test components** — Dataset readers, writers, and assertion utilities.

## Requirements

- Java 17 or later
- Maven
- nos2jdbc 2.0.0

## Modules

`nos2jdbc-test` depends on `nos2jdbc-core` and uses JUnit 5 for testing.

| Dependency | Purpose |
| --- | --- |
| `com.github.ns2j:nos2jdbc-core` | JDBC and ORM functionality |
| `org.junit.jupiter:junit-jupiter-engine` | JUnit 5 test execution |
| `org.hsqldb:hsqldb` | HSQLDB for tests |
| `jakarta.transaction:jakarta.transaction-api` | Transaction API for tests |
| `jakarta.persistence:jakarta.persistence-api` | Persistence API for tests |

## Build and test

Run the tests from the root of the nos2jdbc repository:

```bash
mvn test
```

To build the module and its required dependencies:

```bash
mvn -pl nos2jdbc-test -am package
```

## Related projects

- [nos2jdbc](https://github.com/ns2j/nos2jdbc) — Project overview
- [nos2jdbc-core](https://github.com/ns2j/nos2jdbc/tree/master/nos2jdbc-core) — ORM core
- [nos2jdbc-gen](https://github.com/ns2j/nos2jdbc/tree/master/nos2jdbc-gen) — Code generation
- [nos2jdbc-util](https://github.com/ns2j/nos2jdbc/tree/master/nos2jdbc-util) — Utility classes

## License

Apache License 2.0. See [LICENSE](https://github.com/ns2j/nos2jdbc/blob/master/LICENSE).
