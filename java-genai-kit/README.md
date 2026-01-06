# java-genai-kit

**GenAI utilities including prompt management, versioning, and token usage tracking for Java applications.**

[![Maven Central](https://img.shields.io/maven-central/v/io.github.upendra-manike/java-genai-kit)](https://central.sonatype.com/artifact/io.github.upendra-manike/java-genai-kit)

## Features

- **PromptManager**: LLM prompt lifecycle management with versioning
- **TokenTracker**: Token usage tracking for LLM API calls

## Installation

```xml
<dependency>
    <groupId>io.github.upendra-manike</groupId>
    <artifactId>java-genai-kit</artifactId>
    <version>0.1.1</version>
</dependency>
```

## Usage

### Prompt Manager

```java
import io.github.upendramanike.genai.PromptManager;

PromptManager manager = PromptManager.create();

// Register prompts with versions
manager.register("user-prompt", "1.0", "You are a helpful assistant.");
manager.register("user-prompt", "2.0", "You are a helpful and friendly assistant.");

// Get specific version
String prompt = manager.get("user-prompt", "1.0");

// Get latest version
String latest = manager.getLatest("user-prompt");
```

### Token Tracker

```java
import io.github.upendramanike.genai.TokenTracker;

TokenTracker tracker = TokenTracker.create();

// Record token usage
tracker.record(100, 50); // 100 input tokens, 50 output tokens
tracker.record(200, 100);

// Get totals
long totalInput = tracker.getTotalInputTokens(); // 300
long totalOutput = tracker.getTotalOutputTokens(); // 150
long total = tracker.getTotalTokens(); // 450

// Reset
tracker.reset();
```

## Use Cases

- LLM prompt lifecycle management
- Prompt versioning and rollback
- Token usage tracking
- GenAI observability

## Keywords

genai, LLM, AI, prompt management, token tracking, AI observability, machine learning, OpenAI, GPT

