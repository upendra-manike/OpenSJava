# api-shield

**Safe HTTP client wrapper and request validation utilities for building secure API integrations.**

[![Maven Central](https://img.shields.io/maven-central/v/io.github.upendra-manike/api-shield)](https://central.sonatype.com/artifact/io.github.upendra-manike/api-shield)

## Features

- **ApiClient**: Safe HTTP client wrapper
- **RequestValidator**: Input validation (SQL injection, XSS protection)

## Installation

```xml
<dependency>
    <groupId>io.github.upendra-manike</groupId>
    <artifactId>api-shield</artifactId>
    <version>0.1.0</version>
</dependency>
```

## Usage

### API Client

```java
import io.github.upendramanike.apishield.ApiClient;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

ApiClient client = ApiClient.create(
    "https://api.example.com",
    Duration.ofSeconds(10)
);

// GET request
CompletableFuture<String> response = client.get("/users/123");

// POST request
Map<String, String> headers = Map.of("Content-Type", "application/json");
CompletableFuture<String> result = client.post(
    "/users",
    "{\"name\":\"John\"}",
    headers
);
```

### Request Validator

```java
import io.github.upendramanike.apishield.RequestValidator;

// Validate input
try {
    RequestValidator.validateNoSqlInjection(userInput);
    RequestValidator.validateNoXss(userInput);
    RequestValidator.validateLength(userInput, 100);
} catch (RequestValidator.ValidationException e) {
    // Handle validation error
}
```

## Use Cases

- Safe HTTP client wrapper
- Input validation (SQL injection, XSS)
- API request/response handling
- Security validation

## Keywords

HTTP client, API, validation, security, XSS prevention, REST client, web security, input validation

