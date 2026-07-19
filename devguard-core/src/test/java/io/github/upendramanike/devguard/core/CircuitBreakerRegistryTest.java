package io.github.upendramanike.devguard.core;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.upendramanike.devguard.core.CircuitBreakerRegistry.Breaker;
import io.github.upendramanike.devguard.core.CircuitBreakerRegistry.State;
import org.junit.jupiter.api.Test;

class CircuitBreakerRegistryTest {

    @Test
    void tripsToOpenAfterThreshold() {
        Breaker breaker = new CircuitBreakerRegistry().get("cb", 2, 10_000, 1);
        assertThat(breaker.allowRequest()).isTrue();
        breaker.recordFailure();
        breaker.recordFailure();
        assertThat(breaker.state()).isEqualTo(State.OPEN);
        assertThat(breaker.allowRequest()).isFalse();
    }

    @Test
    void recoversThroughHalfOpen() throws InterruptedException {
        Breaker breaker = new CircuitBreakerRegistry().get("cb2", 1, 50, 1);
        breaker.recordFailure();
        assertThat(breaker.state()).isEqualTo(State.OPEN);
        Thread.sleep(70);
        assertThat(breaker.allowRequest()).isTrue();
        assertThat(breaker.state()).isEqualTo(State.HALF_OPEN);
        breaker.recordSuccess();
        assertThat(breaker.state()).isEqualTo(State.CLOSED);
    }
}
