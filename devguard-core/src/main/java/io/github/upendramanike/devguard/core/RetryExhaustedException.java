package io.github.upendramanike.devguard.core;

/** Thrown when all retry attempts configured by {@code @Retry} have been exhausted. */
public class RetryExhaustedException extends RuntimeException {

    public RetryExhaustedException(String message, Throwable cause) {
        super(message, cause);
    }
}
