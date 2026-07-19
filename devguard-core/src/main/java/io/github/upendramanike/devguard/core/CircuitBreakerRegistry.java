package io.github.upendramanike.devguard.core;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/** Holds and manages named {@link Breaker} instances shared across invocations. */
public class CircuitBreakerRegistry {

    public enum State {
        CLOSED,
        OPEN,
        HALF_OPEN
    }

    private final ConcurrentHashMap<String, Breaker> breakers = new ConcurrentHashMap<>();

    public Breaker get(String name, int failureThreshold, long openMillis, int halfOpenSuccesses) {
        return breakers.computeIfAbsent(
                name, n -> new Breaker(failureThreshold, openMillis, halfOpenSuccesses));
    }

    /** A single circuit breaker with a CLOSED/OPEN/HALF_OPEN state machine. */
    public static final class Breaker {

        private final int failureThreshold;
        private final long openMillis;
        private final int halfOpenSuccesses;

        private final AtomicReference<State> state = new AtomicReference<>(State.CLOSED);
        private final AtomicInteger consecutiveFailures = new AtomicInteger();
        private final AtomicInteger consecutiveSuccesses = new AtomicInteger();
        private final AtomicLong openedAtMillis = new AtomicLong();

        Breaker(int failureThreshold, long openMillis, int halfOpenSuccesses) {
            this.failureThreshold = failureThreshold;
            this.openMillis = openMillis;
            this.halfOpenSuccesses = Math.max(1, halfOpenSuccesses);
        }

        public State state() {
            return state.get();
        }

        /** @return {@code true} if a call is currently permitted through the breaker. */
        public synchronized boolean allowRequest() {
            State current = state.get();
            if (current == State.OPEN) {
                if (System.currentTimeMillis() - openedAtMillis.get() >= openMillis) {
                    state.set(State.HALF_OPEN);
                    consecutiveSuccesses.set(0);
                    return true;
                }
                return false;
            }
            return true;
        }

        public synchronized void recordSuccess() {
            if (state.get() == State.HALF_OPEN) {
                if (consecutiveSuccesses.incrementAndGet() >= halfOpenSuccesses) {
                    reset();
                }
            } else {
                consecutiveFailures.set(0);
            }
        }

        public synchronized void recordFailure() {
            if (state.get() == State.HALF_OPEN) {
                trip();
                return;
            }
            if (consecutiveFailures.incrementAndGet() >= failureThreshold) {
                trip();
            }
        }

        private void trip() {
            state.set(State.OPEN);
            openedAtMillis.set(System.currentTimeMillis());
            consecutiveSuccesses.set(0);
        }

        private void reset() {
            state.set(State.CLOSED);
            consecutiveFailures.set(0);
            consecutiveSuccesses.set(0);
        }
    }
}
