# async-flow

**Structured concurrency utilities and async task orchestration for modern Java applications.**

[![Maven Central](https://img.shields.io/maven-central/v/io.github.upendra-manike/async-flow)](https://central.sonatype.com/artifact/io.github.upendra-manike/async-flow)

## Features

- **TaskExecutor**: Structured concurrency utilities
- **CancellationToken**: Async operation cancellation

## Installation

```xml
<dependency>
    <groupId>io.github.upendra-manike</groupId>
    <artifactId>async-flow</artifactId>
    <version>0.1.1</version>
</dependency>
```

## Usage

### Task Executor

```java
import io.github.upendramanike.asyncflow.TaskExecutor;
import java.util.concurrent.CompletableFuture;

TaskExecutor executor = TaskExecutor.create();

// Execute single task
CompletableFuture<String> future = executor.execute(() -> {
    return processData();
});

// Execute multiple tasks in parallel
CompletableFuture<Void> all = executor.executeAll(
    () -> fetchUserData(),
    () -> fetchOrderData(),
    () -> fetchProductData()
);

all.thenRun(() -> {
    // All tasks completed
});
```

### Cancellation Token

```java
import io.github.upendramanike.asyncflow.CancellationToken;

CancellationToken token = new CancellationToken();

// In your async operation
while (!token.isCancelled()) {
    token.checkCancellation(); // Throws if cancelled
    // Do work
}

// Cancel from another thread
token.cancel();
```

## Use Cases

- Async task execution
- Structured concurrency
- Task cancellation
- Parallel workflow orchestration

## Keywords

async, concurrency, parallel, cancellation, futures, structured concurrency, virtual threads, CompletableFuture

