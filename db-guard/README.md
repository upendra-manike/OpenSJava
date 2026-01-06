# db-guard

**Lightweight database safety utilities including SQL injection prevention and pagination helpers.**

[![Maven Central](https://img.shields.io/maven-central/v/io.github.upendra-manike/db-guard)](https://central.sonatype.com/artifact/io.github.upendra-manike/db-guard)

## Features

- **SqlBuilder**: Safe SQL query builder (prevents injection)
- **Pagination**: Cross-database pagination helper

## Installation

```xml
<dependency>
    <groupId>io.github.upendra-manike</groupId>
    <artifactId>db-guard</artifactId>
    <version>0.1.1</version>
</dependency>
```

## Usage

### SQL Builder

```java
import io.github.upendramanike.dbguard.SqlBuilder;

SqlBuilder builder = SqlBuilder.select("id", "name", "email")
    .from("users")
    .where("age > ? AND status = ?", 18, "active")
    .orderBy("name")
    .limit(10);

String sql = builder.build();
Object[] params = builder.getParameters();

// Use with PreparedStatement
PreparedStatement stmt = connection.prepareStatement(sql);
for (int i = 0; i < params.length; i++) {
    stmt.setObject(i + 1, params[i]);
}
```

### Pagination

```java
import io.github.upendramanike.dbguard.Pagination;

Pagination page = Pagination.of(2, 20); // page 2, 20 items per page

String sql = "SELECT * FROM products LIMIT ? OFFSET ?";
PreparedStatement stmt = connection.prepareStatement(sql);
stmt.setInt(1, page.getLimit());
stmt.setInt(2, page.getOffset());
```

## Use Cases

- Safe SQL query building
- SQL injection prevention
- Cross-database pagination
- Dynamic query construction

## Keywords

database, SQL, pagination, query builder, SQL injection prevention, data access, PreparedStatement, JDBC

