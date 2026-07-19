package io.github.upendramanike.devguard.autoconfigure;

import io.github.upendramanike.devguard.aspect.AuditAspect;
import io.github.upendramanike.devguard.aspect.BulkheadAspect;
import io.github.upendramanike.devguard.aspect.CacheResultAspect;
import io.github.upendramanike.devguard.aspect.CircuitBreakerAspect;
import io.github.upendramanike.devguard.aspect.DistributedLockAspect;
import io.github.upendramanike.devguard.aspect.FeatureFlagAspect;
import io.github.upendramanike.devguard.aspect.IdempotentAspect;
import io.github.upendramanike.devguard.aspect.LogExecutionAspect;
import io.github.upendramanike.devguard.aspect.MeasurePerformanceAspect;
import io.github.upendramanike.devguard.aspect.RateLimitAspect;
import io.github.upendramanike.devguard.aspect.RetryAspect;
import io.github.upendramanike.devguard.aspect.SecureAspect;
import io.github.upendramanike.devguard.core.AuditSink;
import io.github.upendramanike.devguard.core.BulkheadRegistry;
import io.github.upendramanike.devguard.core.CircuitBreakerRegistry;
import io.github.upendramanike.devguard.core.FeatureFlagProvider;
import io.github.upendramanike.devguard.core.IdempotencyStore;
import io.github.upendramanike.devguard.core.InMemoryIdempotencyStore;
import io.github.upendramanike.devguard.core.InMemoryLockProvider;
import io.github.upendramanike.devguard.core.InMemoryResultCache;
import io.github.upendramanike.devguard.core.LockProvider;
import io.github.upendramanike.devguard.core.LoggingAuditSink;
import io.github.upendramanike.devguard.core.MaskingService;
import io.github.upendramanike.devguard.core.NoOpSecurityContextProvider;
import io.github.upendramanike.devguard.core.PropertiesFeatureFlagProvider;
import io.github.upendramanike.devguard.core.RateLimiterRegistry;
import io.github.upendramanike.devguard.core.ResultCache;
import io.github.upendramanike.devguard.core.SecurityContextProvider;
import io.github.upendramanike.devguard.core.SpelKeyResolver;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/** Auto-configuration wiring the DevGuard SPI defaults and AOP aspects. */
@AutoConfiguration
@EnableConfigurationProperties(DevGuardProperties.class)
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ConditionalOnProperty(prefix = "devguard", name = "enabled", havingValue = "true", matchIfMissing = true)
public class DevGuardAutoConfiguration {

    // ---- Shared infrastructure ----

    @Bean
    @ConditionalOnMissingBean
    SpelKeyResolver devGuardSpelKeyResolver() {
        return new SpelKeyResolver();
    }

    @Bean
    @ConditionalOnMissingBean
    LockProvider devGuardLockProvider() {
        return new InMemoryLockProvider();
    }

    @Bean
    @ConditionalOnMissingBean
    IdempotencyStore devGuardIdempotencyStore() {
        return new InMemoryIdempotencyStore();
    }

    @Bean
    @ConditionalOnMissingBean
    AuditSink devGuardAuditSink() {
        return new LoggingAuditSink();
    }

    @Bean
    @ConditionalOnMissingBean
    FeatureFlagProvider devGuardFeatureFlagProvider(DevGuardProperties properties) {
        DevGuardProperties.FeatureFlags cfg = properties.getFeatureFlags();
        return new PropertiesFeatureFlagProvider(cfg.getFlags(), cfg.isDefaultEnabled());
    }

    @Bean
    @ConditionalOnMissingBean
    ResultCache devGuardResultCache() {
        return new InMemoryResultCache();
    }

    @Bean
    @ConditionalOnMissingBean
    SecurityContextProvider devGuardSecurityContextProvider() {
        return new NoOpSecurityContextProvider();
    }

    @Bean
    @ConditionalOnMissingBean
    CircuitBreakerRegistry devGuardCircuitBreakerRegistry() {
        return new CircuitBreakerRegistry();
    }

    @Bean
    @ConditionalOnMissingBean
    BulkheadRegistry devGuardBulkheadRegistry() {
        return new BulkheadRegistry();
    }

    @Bean
    @ConditionalOnMissingBean
    RateLimiterRegistry devGuardRateLimiterRegistry() {
        return new RateLimiterRegistry();
    }

    @Bean
    @ConditionalOnMissingBean
    MaskingService devGuardMaskingService() {
        return new MaskingService();
    }

    // ---- Aspects ----

    @Bean
    @ConditionalOnMissingBean
    RetryAspect devGuardRetryAspect() {
        return new RetryAspect();
    }

    @Bean
    @ConditionalOnMissingBean
    RateLimitAspect devGuardRateLimitAspect(RateLimiterRegistry registry, SpelKeyResolver spel) {
        return new RateLimitAspect(registry, spel);
    }

    @Bean
    @ConditionalOnMissingBean
    IdempotentAspect devGuardIdempotentAspect(IdempotencyStore store, SpelKeyResolver spel) {
        return new IdempotentAspect(store, spel);
    }

    @Bean
    @ConditionalOnMissingBean
    DistributedLockAspect devGuardDistributedLockAspect(LockProvider lockProvider, SpelKeyResolver spel) {
        return new DistributedLockAspect(lockProvider, spel);
    }

    @Bean
    @ConditionalOnMissingBean
    AuditAspect devGuardAuditAspect(AuditSink auditSink, SecurityContextProvider securityContextProvider) {
        return new AuditAspect(auditSink, securityContextProvider);
    }

    @Bean
    @ConditionalOnMissingBean
    LogExecutionAspect devGuardLogExecutionAspect() {
        return new LogExecutionAspect();
    }

    @Bean
    @ConditionalOnMissingBean
    MeasurePerformanceAspect devGuardMeasurePerformanceAspect() {
        return new MeasurePerformanceAspect();
    }

    @Bean
    @ConditionalOnMissingBean
    CircuitBreakerAspect devGuardCircuitBreakerAspect(CircuitBreakerRegistry registry) {
        return new CircuitBreakerAspect(registry);
    }

    @Bean
    @ConditionalOnMissingBean
    FeatureFlagAspect devGuardFeatureFlagAspect(FeatureFlagProvider provider) {
        return new FeatureFlagAspect(provider);
    }

    @Bean
    @ConditionalOnMissingBean
    CacheResultAspect devGuardCacheResultAspect(ResultCache cache, SpelKeyResolver spel) {
        return new CacheResultAspect(cache, spel);
    }

    @Bean
    @ConditionalOnMissingBean
    SecureAspect devGuardSecureAspect(SecurityContextProvider provider) {
        return new SecureAspect(provider);
    }

    @Bean
    @ConditionalOnMissingBean
    BulkheadAspect devGuardBulkheadAspect(BulkheadRegistry registry) {
        return new BulkheadAspect(registry);
    }
}
