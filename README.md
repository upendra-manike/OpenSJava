# OpenSJava - High-Impact Java Libraries Suite

A curated collection of focused, production-ready Java libraries solving real-world problems in the Java ecosystem. Each library is lightweight, well-tested, and designed for easy adoption.

## 🌱 Spring Boot Ecosystem

Many libraries in this suite are designed to solve **real, recurring Spring Boot production problems** that teams face daily. These are **architect-level solutions**, not beginner utilities.

**Spring Framework and Spring Boot are powerful, but many production problems are not solved cleanly out-of-the-box.** This suite addresses those gaps.

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

## 🌸 Spring Boot Libraries

### 8. [spring-observability-kit](spring-observability-kit/) - Observability & Logging
**Maven Central:** `io.github.upendra-manike:spring-observability-kit:0.1.1`

Auto correlation IDs, structured JSON logs, and request lifecycle tracing for Spring Boot applications.

**Problem solved:**
- No correlation IDs across async operations, HTTP clients, and message queues
- Logs too noisy without structured context
- Hard to trace request lifecycle across distributed systems

**Use cases:**
- Automatic correlation ID generation and propagation
- Structured logging with MDC integration
- Request lifecycle tracing
- Distributed tracing support

**Keywords:** observability, logging, correlation ID, tracing, structured logging, Spring Boot, MDC, request tracing, distributed tracing

---

### 9. [spring-test-kit](spring-test-kit/) - Testing Utilities
**Maven Central:** `io.github.upendra-manike:spring-test-kit:0.1.1`

Test data builders, deterministic async testing, and lightweight test slices for Spring Boot applications.

**Problem solved:**
- Slow `@SpringBootTest` with full application context
- Flaky integration tests due to timing issues
- Hard test data setup with boilerplate code
- Async testing complexity

**Use cases:**
- Fluent test data builders
- Deterministic async testing utilities
- Lightweight test slices (faster than `@SpringBootTest`)
- Timeout and polling utilities

**Keywords:** testing, test utilities, async testing, test data builders, Spring Boot testing, test slices, integration testing, unit testing

---

### 10. [spring-security-guard](spring-security-guard/) - Security Validation
**Maven Central:** `io.github.upendra-manike:spring-security-guard:0.1.1`

Security config linting, runtime validation, and role & permission sanity checks for Spring Boot applications.

**Problem solved:**
- Over-permissive security rules (`permitAll()` on sensitive endpoints)
- Missing method-level security checks
- Token validation mistakes
- Role/permission inconsistencies

**Use cases:**
- Security configuration validation at startup
- Runtime role/permission checking
- Method-level security validation
- Endpoint security analysis

**Keywords:** security, Spring Security, validation, roles, permissions, security linting, access control, authentication, authorization

---

### 11. [spring-feature-toggle](spring-feature-toggle/) - Feature Flags
**Maven Central:** `io.github.upendra-manike:spring-feature-toggle:0.1.1`

Typed feature flags, runtime toggles, rollback support, and audit logs for Spring Boot applications.

**Problem solved:**
- Ad-hoc if-else flags that are hard to manage
- No rollback strategy for features
- No audit trail of flag usage
- Type safety issues with string-based flags

**Use cases:**
- Typed feature flags with runtime toggling
- Quick rollback for incident response
- Complete audit trail of flag usage
- Centralized flag registry

**Keywords:** feature flags, feature toggles, A/B testing, rollout, rollback, audit logging, Spring Boot, runtime configuration

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

**Core Java Libraries:**
- [safe-config README](safe-config/README.md)
- [resilient-core](resilient-core/)
- [async-flow](async-flow/)
- [db-guard](db-guard/)
- [api-shield](api-shield/)
- [arch-guard](arch-guard/)
- [java-genai-kit](java-genai-kit/)

**Spring Boot Libraries:**
- [spring-observability-kit](spring-observability-kit/)
- [spring-test-kit](spring-test-kit/)
- [spring-security-guard](spring-security-guard/)
- [spring-feature-toggle](spring-feature-toggle/)

## 🎯 Design Principles

- **Focused**: Each library solves one specific problem well
- **Lightweight**: Minimal dependencies, no framework lock-in (Spring libraries are optional)
- **Production-ready**: Well-tested, documented, and maintained
- **Modern Java**: Built for Java 17+ with modern patterns
- **Zero-config**: Works out of the box with sensible defaults
- **Spring Boot Ready**: Spring libraries integrate seamlessly with Spring Boot auto-configuration

## 🏆 Top 5 Spring Packages (By Priority)

| Priority | Package                    | Problem Solved                    |
| -------- | -------------------------- | ---------------------------------- |
| ⭐ 1      | `spring-observability-kit` | Correlation IDs & structured logs  |
| ⭐ 2      | `spring-test-kit`          | Fast, reliable testing             |
| ⭐ 3      | `spring-security-guard`     | Security misconfigurations         |
| ⭐ 4      | `spring-feature-toggle`     | Feature rollout & rollback          |
| ⭐ 5      | `safe-config`              | Configuration validation           |

These have **mass adoption potential** in Spring Boot production environments.

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


