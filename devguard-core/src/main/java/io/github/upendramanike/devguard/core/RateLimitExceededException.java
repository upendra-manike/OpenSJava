package io.github.upendramanike.devguard.core;

/** Thrown when a call is rejected because the {@code @RateLimit} budget has been exhausted. */
public class RateLimitExceededException extends RuntimeException {

    public RateLimitExceededException(String message) {
        super(message);
    }
}
