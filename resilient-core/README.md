# resilient-core

**Unified retry, timeout, and circuit breaker utilities for resilient Java services.**

[![Maven Central](https://img.shields.io/maven-central/v/io.github.upendra-manike/resilient-core)](https://central.sonatype.com/artifact/io.github.upendra-manike/resilient-core)

## Features

- **RetryPolicy**: Configurable retry with exponential backoff
- **Timeout**: Operation timeout utilities
- **CircuitBreaker**: Circuit breaker pattern implementation

## Installation

```xml
<dependency>
    <groupId>io.github.upendra-manike</groupId>
    <artifactId>resilient-core</artifactId>
    <version>0.1.1</version>
</dependency>
```

## Usage

### Retry Policy

```java
import io.github.upendramanike.resilientcore.RetryPolicy;
import java.time.Duration;

RetryPolicy policy = RetryPolicy.builder()
    .maxAttempts(3)
    .initialDelay(Duration.ofMillis(100))
    .backoffMultiplier(2.0)
    .maxDelay(Duration.ofSeconds(30))
    .retryOn(IOException.class, TimeoutException.class)
    .build();

String result = policy.execute(() -> {
    // Your operation that might fail
    return callExternalService();
});
```

### Timeout

```java
import io.github.upendramanike.resilientcore.Timeout;
import java.time.Duration;

try {
    String result = Timeout.execute(
        () -> longRunningOperation(),
        Duration.ofSeconds(5)
    );
} catch (Timeout.TimeoutException e) {
    // Handle timeout
}
```

### Circuit Breaker

```java
import io.github.upendramanike.resilientcore.CircuitBreaker;
import java.time.Duration;

CircuitBreaker breaker = CircuitBreaker.create(
    5,  // failure threshold
    Duration.ofSeconds(30)  // timeout before retry
);

try {
    String result = breaker.execute(() -> {
        return callExternalService();
    });
} catch (CircuitBreaker.CircuitOpenException e) {
    // Circuit is open, service unavailable
}
```

## Use Cases

- API call retries with exponential backoff
- Operation timeouts
- Circuit breaker pattern implementation
- Resilience patterns for microservices

## Keywords

retry, timeout, circuit breaker, resilience, backoff, fault tolerance, reliability, microservices, resilience patterns

