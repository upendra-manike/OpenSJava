# OpenSJava - High-Impact Java Libraries Suite

A curated collection of focused, production-ready Java libraries solving real-world problems in the Java ecosystem. Each library is lightweight, well-tested, and designed for easy adoption.

## 📚 Libraries

### 1. [safe-config](safe-config/) - Configuration Validation & Secret Masking
**Maven Central:** `io.github.upendra-manike:safe-config:0.1.1`

Fail-fast configuration loading with rich error messages and automatic secret masking for safe logging.

**Use cases:**
- Runtime configuration validation
- Preventing secrets from leaking into logs
- Type-safe configuration binding
- Environment variable management

**Keywords:** configuration, validation, secrets, environment variables, fail-fast, security, logging

---

### 2. [resilient-core](resilient-core/) - Retry, Timeout & Circuit Breaker
**Maven Central:** `io.github.upendra-manike:resilient-core:0.1.1`

Unified retry, timeout, and circuit breaker utilities for building resilient Java services.

**Use cases:**
- API call retries with exponential backoff
- Operation timeouts
- Circuit breaker pattern implementation
- Resilience patterns

**Keywords:** retry, timeout, circuit breaker, resilience, backoff, fault tolerance, reliability

---

### 3. [async-flow](async-flow/) - Structured Concurrency & Async Workflows
**Maven Central:** `io.github.upendra-manike:async-flow:0.1.1`

Structured concurrency utilities and async task orchestration for modern Java applications.

**Use cases:**
- Async task execution
- Structured concurrency
- Task cancellation
- Parallel workflow orchestration

**Keywords:** async, concurrency, parallel, cancellation, futures, structured concurrency, virtual threads

---

### 4. [db-guard](db-guard/) - Safe Database Access Helpers
**Maven Central:** `io.github.upendra-manike:db-guard:0.1.1`

Lightweight database safety utilities including SQL injection prevention and pagination helpers.

**Use cases:**
- Safe SQL query building
- SQL injection prevention
- Cross-database pagination
- Dynamic query construction

**Keywords:** database, SQL, pagination, query builder, SQL injection prevention, data access

---

### 5. [api-shield](api-shield/) - Safe API Clients & Validation
**Maven Central:** `io.github.upendra-manike:api-shield:0.1.1`

Safe HTTP client wrapper and request validation utilities for building secure API integrations.

**Use cases:**
- Safe HTTP client wrapper
- Input validation (SQL injection, XSS)
- API request/response handling
- Security validation

**Keywords:** HTTP client, API, validation, security, XSS prevention, REST client, web security

---

### 6. [arch-guard](arch-guard/) - Architecture Rule Enforcement
**Maven Central:** `io.github.upendra-manike:arch-guard:0.1.1`

Dependency boundary checking and architecture rule enforcement for maintaining clean code structure.

**Use cases:**
- Dependency boundary enforcement
- Architecture rule validation
- Layer violation detection
- Code governance

**Keywords:** architecture, dependency checking, code governance, layer enforcement, clean architecture

---

### 7. [java-genai-kit](java-genai-kit/) - GenAI Utilities & Observability
**Maven Central:** `io.github.upendra-manike:java-genai-kit:0.1.1`

GenAI utilities including prompt management, versioning, and token usage tracking for Java applications.

**Use cases:**
- LLM prompt lifecycle management
- Prompt versioning and rollback
- Token usage tracking
- GenAI observability

**Keywords:** genai, LLM, AI, prompt management, token tracking, AI observability, machine learning

---

## 🚀 Quick Start

Add any library to your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.upendra-manike</groupId>
    <artifactId>safe-config</artifactId>
    <version>0.1.1</version>
</dependency>
```

Replace `safe-config` with any library name from the list above.

## 📖 Documentation

Each library has its own README with detailed usage examples:
- [safe-config README](safe-config/README.md)
- [resilient-core](resilient-core/)
- [async-flow](async-flow/)
- [db-guard](db-guard/)
- [api-shield](api-shield/)
- [arch-guard](arch-guard/)
- [java-genai-kit](java-genai-kit/)

## 🎯 Design Principles

- **Focused**: Each library solves one specific problem well
- **Lightweight**: Minimal dependencies, no framework lock-in
- **Production-ready**: Well-tested, documented, and maintained
- **Modern Java**: Built for Java 17+ with modern patterns
- **Zero-config**: Works out of the box with sensible defaults

## 📦 Maven Central

All libraries are published to Maven Central and can be used immediately:

```xml
<repositories>
    <repository>
        <id>central</id>
        <url>https://repo1.maven.org/maven2</url>
    </repository>
</repositories>
```

## 🤝 Contributing

Contributions welcome! Each library is designed to be extended and improved.

## 📄 License

Apache License 2.0 - See LICENSE file for details.

## 🔗 Links

- **GitHub**: https://github.com/upendrakumarmanike/OpenSJava
- **Maven Central**: https://central.sonatype.com/artifact/io.github.upendra-manike
- **Issues**: https://github.com/upendrakumarmanike/OpenSJava/issues

---

**Built for developers who want focused, high-quality Java libraries without the bloat.**

