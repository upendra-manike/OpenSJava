package io.github.upendramanike.devguard.autoconfigure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.upendramanike.devguard.annotations.CacheResult;
import io.github.upendramanike.devguard.annotations.CircuitBreaker;
import io.github.upendramanike.devguard.annotations.FeatureFlag;
import io.github.upendramanike.devguard.annotations.Idempotent;
import io.github.upendramanike.devguard.annotations.RateLimit;
import io.github.upendramanike.devguard.annotations.Retry;
import io.github.upendramanike.devguard.annotations.Secure;
import io.github.upendramanike.devguard.core.AccessDeniedException;
import io.github.upendramanike.devguard.core.CircuitOpenException;
import io.github.upendramanike.devguard.core.RateLimitExceededException;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = DevGuardIntegrationTest.App.class)
@TestPropertySource(properties = {"devguard.feature-flags.flags.NEW_PAYMENT=true"})
class DevGuardIntegrationTest {

    @Autowired GuardedService service;

    @Test
    void retryEventuallySucceeds() {
        assertThat(service.flakyCall()).isEqualTo("ok");
        assertThat(service.flakyAttempts()).isEqualTo(3);
    }

    @Test
    void rateLimitRejectsAfterBudget() {
        assertThat(service.limited()).isEqualTo("ok");
        assertThat(service.limited()).isEqualTo("ok");
        assertThatThrownBy(service::limited).isInstanceOf(RateLimitExceededException.class);
    }

    @Test
    void idempotentReplaysResult() {
        int first = service.charge("txn-1");
        int second = service.charge("txn-1");
        assertThat(first).isEqualTo(second);
        assertThat(service.chargeCount()).isEqualTo(1);
    }

    @Test
    void cacheResultAvoidsSecondExecution() {
        int first = service.load(7L);
        int second = service.load(7L);
        assertThat(first).isEqualTo(second);
        assertThat(service.loadCount()).isEqualTo(1);
    }

    @Test
    void featureFlagEnabledRuns() {
        assertThat(service.payV2()).isEqualTo("v2");
    }

    @Test
    void featureFlagDisabledReturnsDefault() {
        assertThat(service.payV3()).isNull();
    }

    @Test
    void secureDeniesAnonymous() {
        assertThatThrownBy(service::adminOnly).isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void circuitBreakerOpensAfterFailures() {
        assertThatThrownBy(service::alwaysFails).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(service::alwaysFails).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(service::alwaysFails).isInstanceOf(CircuitOpenException.class);
    }

    @Configuration
    @ImportAutoConfiguration(DevGuardAutoConfiguration.class)
    static class App {
        @Bean
        GuardedService guardedService() {
            return new GuardedService();
        }
    }

    static class GuardedService {

        final AtomicInteger flakyAttempts = new AtomicInteger();
        final AtomicInteger chargeCount = new AtomicInteger();
        final AtomicInteger loadCount = new AtomicInteger();

        public int flakyAttempts() {
            return flakyAttempts.get();
        }

        public int chargeCount() {
            return chargeCount.get();
        }

        public int loadCount() {
            return loadCount.get();
        }

        @Retry(attempts = 3, delay = 1, backoff = 1.0)
        public String flakyCall() {
            if (flakyAttempts.incrementAndGet() < 3) {
                throw new IllegalStateException("transient");
            }
            return "ok";
        }

        @RateLimit(requests = 2, duration = 60)
        public String limited() {
            return "ok";
        }

        @Idempotent(key = "#id", ttlSeconds = 60)
        public int charge(String id) {
            return chargeCount.incrementAndGet();
        }

        @CacheResult(ttl = 60, key = "#id")
        public int load(Long id) {
            return loadCount.incrementAndGet();
        }

        @FeatureFlag("NEW_PAYMENT")
        public String payV2() {
            return "v2";
        }

        @FeatureFlag("MISSING_FLAG")
        public String payV3() {
            return "v3";
        }

        @Secure(roles = {"ADMIN"})
        public String adminOnly() {
            return "secret";
        }

        @CircuitBreaker(failureThreshold = 2, openMillis = 10_000)
        public String alwaysFails() {
            throw new IllegalStateException("boom");
        }
    }
}
