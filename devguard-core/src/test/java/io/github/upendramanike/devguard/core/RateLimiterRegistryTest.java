package io.github.upendramanike.devguard.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class RateLimiterRegistryTest {

    @Test
    void allowsUpToCapacityThenRejects() {
        RateLimiterRegistry registry = new RateLimiterRegistry();
        assertThat(registry.tryAcquire("k", 2, 60)).isTrue();
        assertThat(registry.tryAcquire("k", 2, 60)).isTrue();
        assertThat(registry.tryAcquire("k", 2, 60)).isFalse();
    }

    @Test
    void separateKeysHaveSeparateBudgets() {
        RateLimiterRegistry registry = new RateLimiterRegistry();
        assertThat(registry.tryAcquire("a", 1, 60)).isTrue();
        assertThat(registry.tryAcquire("a", 1, 60)).isFalse();
        assertThat(registry.tryAcquire("b", 1, 60)).isTrue();
    }
}
