package io.github.upendramanike.devguard.core;

/** Thrown when a {@code @Bulkhead} has no free permit within the configured wait time. */
public class BulkheadFullException extends RuntimeException {

    public BulkheadFullException(String message) {
        super(message);
    }
}
