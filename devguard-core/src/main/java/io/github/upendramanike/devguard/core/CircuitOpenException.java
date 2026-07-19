package io.github.upendramanike.devguard.core;

/** Thrown when a call is short-circuited because the {@code @CircuitBreaker} is OPEN. */
public class CircuitOpenException extends RuntimeException {

    public CircuitOpenException(String message) {
        super(message);
    }
}
