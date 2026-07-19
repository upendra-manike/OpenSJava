# devguard-core

Production-grade Java annotations for resilience, security, and observability — implemented with
Spring Boot AOP. Drop the dependency on your classpath, annotate your methods, and get retries,
rate limiting, idempotency, distributed locking, auditing, masking, circuit breaking, and more with
zero boilerplate.

Every capability ships with a sensible **in-memory default** and a **pluggable SPI**, so you can
start immediately and later swap in Redis / ZooKeeper / your own auth without touching call sites.

## Installation

```xml
<dependency>
    <groupId>io.github.upendra-manike</groupId>
    <artifactId>devguard-core</artifactId>
    <version>0.1.0</version>
</dependency>
```

Requires Java 17+ and Spring Boot 3.x. Auto-configuration activates automatically; disable everything
with `devguard.enabled=false`.

## Annotations

| Annotation | Purpose | Throws on failure |
|---|---|---|
| `@Retry` | Retry with exponential backoff | `RetryExhaustedException` |
| `@RateLimit` | Token-bucket rate limiting | `RateLimitExceededException` |
| `@Idempotent` | Deduplicate / replay by key | `DuplicateRequestException` |
| `@DistributedLock` | Run under a named lock | `DistributedLockException` |
| `@Audit` | Emit audit events (who/when/what) | — |
| `@Mask` | Mask sensitive fields | — |
| `@LogExecution` | Log args, result, duration | — |
| `@MeasurePerformance` | Time + heap usage, slow-call warnings | — |
| `@CircuitBreaker` | CLOSED / OPEN / HALF_OPEN state machine | `CircuitOpenException` |
| `@FeatureFlag` | Gate methods behind flags | `FeatureDisabledException` |
| `@CacheResult` | TTL result caching | — |
| `@Secure` | Role / permission authorization | `AccessDeniedException` |
| `@Bulkhead` | Bound concurrent invocations | `BulkheadFullException` |

Keys use SpEL against method arguments, e.g. `key = "#orderId"` or `key = "#request.transactionId"`.

## Usage

```java
@Retry(attempts = 3, delay = 1000, backoff = 2.0)
public PaymentResponse pay() { ... }

@RateLimit(requests = 100, duration = 60, key = "#userId")
public void call(String userId) { ... }

@Idempotent(key = "#request.transactionId", ttlSeconds = 3600)
public PaymentResponse submit(PaymentRequest request) { ... }

@DistributedLock(key = "#orderId")
public void processOrder(Long orderId) { ... }

@Audit(action = "UPDATE_CUSTOMER")
public void updateCustomer(Customer c) { ... }

@CircuitBreaker(failureThreshold = 5, openMillis = 10_000)
public String callDownstream() { ... }

@FeatureFlag("NEW_PAYMENT")
public PaymentResponse payV2() { ... }

@CacheResult(ttl = 300, key = "#id")
public Product findById(Long id) { ... }

@Secure(roles = {"ADMIN", "MANAGER"})
public void deleteUser(Long id) { ... }

@Bulkhead(maxConcurrent = 10, maxWaitMillis = 100)
public String callExpensiveService() { ... }

@LogExecution(logArgs = true, logResult = true)
@MeasurePerformance(warnAboveMillis = 200)
public Report generate() { ... }
```

### Masking sensitive data

```java
class User {
    @Mask(type = MaskType.EMAIL) String email;   // up*****@gmail.com
    @Mask(type = MaskType.PHONE) String phone;   // 98******10
}

// Anywhere you have a MaskingService bean:
Map<String, Object> safe = maskingService.maskFields(user);
```

## Configuration

```yaml
devguard:
  enabled: true
  feature-flags:
    default-enabled: false
    flags:
      NEW_PAYMENT: true
      LEGACY_EXPORT: false
```

## Pluggable providers (SPI)

Override any default simply by declaring your own bean — every default is
`@ConditionalOnMissingBean`:

| Interface | Default | Swap in |
|---|---|---|
| `LockProvider` | `InMemoryLockProvider` | Redis / ZooKeeper / Hazelcast |
| `IdempotencyStore` | `InMemoryIdempotencyStore` | Redis |
| `ResultCache` | `InMemoryResultCache` | Redis / Caffeine |
| `AuditSink` | `LoggingAuditSink` | Database / Kafka / SIEM |
| `FeatureFlagProvider` | `PropertiesFeatureFlagProvider` | LaunchDarkly / Unleash |
| `SecurityContextProvider` | `NoOpSecurityContextProvider` | Spring Security |

```java
@Bean
LockProvider redisLockProvider(StringRedisTemplate redis) {
    return new MyRedisLockProvider(redis);
}
```

## Notes

- Aspects apply to **Spring-managed beans** only, and (like all Spring AOP) not to self-invocations
  within the same class.
- The in-memory `LockProvider`, `IdempotencyStore`, and `ResultCache` are single-JVM; provide a
  distributed implementation for multi-instance deployments.

## License

Apache License 2.0.
