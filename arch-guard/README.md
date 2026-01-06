# arch-guard

**Dependency boundary checking and architecture rule enforcement for maintaining clean code structure.**

[![Maven Central](https://img.shields.io/maven-central/v/io.github.upendra-manike/arch-guard)](https://central.sonatype.com/artifact/io.github.upendra-manike/arch-guard)

## Features

- **DependencyChecker**: Architecture rule enforcement
- Pattern-based dependency validation

## Installation

```xml
<dependency>
    <groupId>io.github.upendra-manike</groupId>
    <artifactId>arch-guard</artifactId>
    <version>0.1.0</version>
</dependency>
```

## Usage

```java
import io.github.upendramanike.archguard.DependencyChecker;

DependencyChecker checker = DependencyChecker.create()
    .allow("com.example.api.*")
    .forbid("com.example.internal.*");

// Validate dependencies
try {
    checker.validate("com.example.api.Client"); // OK
    checker.validate("com.example.internal.Secret"); // Throws exception
} catch (DependencyChecker.DependencyViolationException e) {
    // Architecture violation detected
}

// Check if allowed
boolean allowed = checker.isAllowed("com.example.api.Service");
```

## Use Cases

- Dependency boundary enforcement
- Architecture rule validation
- Layer violation detection
- Code governance

## Keywords

architecture, dependency checking, code governance, layer enforcement, clean architecture, dependency rules

