package io.github.upendramanike.resilientcore;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

/**
 * Circuit breaker pattern implementation for resilient operations.
 * Prevents cascading failures by opening the circuit after a threshold of failures.
 */
public final class CircuitBreaker {

    private final int failureThreshold;
    private final Duration timeout;
    private final AtomicInteger failureCount = new AtomicInteger(0);
    private final AtomicLong lastFailureTime = new AtomicLong(0);
    private volatile State state = State.CLOSED;

    private CircuitBreaker(int failureThreshold, Duration timeout) {
        this.failureThreshold = failureThreshold;
        this.timeout = timeout;
    }

    /**
     * Creates a new circuit breaker.
     *
     * @param failureThreshold number of failures before opening the circuit
     * @param timeout duration to wait before attempting to close the circuit
     * @return a new circuit breaker
     */
    public static CircuitBreaker create(int failureThreshold, Duration timeout) {
        return new CircuitBreaker(failureThreshold, timeout);
    }

    /**
     * Executes the supplier through the circuit breaker.
     *
     * @param <T> the return type
     * @param supplier the operation to execute
     * @return the result of the supplier
     * @throws CircuitOpenException if the circuit is open
     * @throws RuntimeException if the operation fails
     */
    public <T> T execute(Supplier<T> supplier) {
        if (state == State.OPEN) {
            if (shouldAttemptReset()) {
                state = State.HALF_OPEN;
            } else {
                throw new CircuitOpenException("Circuit breaker is OPEN");
            }
        }

        try {
            T result = supplier.get();
            onSuccess();
            return result;
        } catch (Exception e) {
            onFailure();
            throw new RuntimeException("Operation failed", e);
        }
    }

    private boolean shouldAttemptReset() {
        long now = System.currentTimeMillis();
        long lastFailure = lastFailureTime.get();
        return (now - lastFailure) >= timeout.toMillis();
    }

    private void onSuccess() {
        failureCount.set(0);
        state = State.CLOSED;
    }

    private void onFailure() {
        int failures = failureCount.incrementAndGet();
        lastFailureTime.set(System.currentTimeMillis());
        if (failures >= failureThreshold) {
            state = State.OPEN;
        }
    }

    /**
     * Gets the current state of the circuit breaker.
     *
     * @return the current state
     */
    public State getState() {
        return state;
    }

    /**
     * Circuit breaker states.
     */
    public enum State {
        CLOSED,   // Normal operation
        OPEN,     // Circuit is open, rejecting requests
        HALF_OPEN // Testing if service has recovered
    }

    /**
     * Exception thrown when circuit breaker is open.
     */
    public static class CircuitOpenException extends RuntimeException {
        public CircuitOpenException(String message) {
            super(message);
        }
    }
}

