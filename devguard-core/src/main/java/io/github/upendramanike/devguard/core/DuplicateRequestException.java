package io.github.upendramanike.devguard.core;

/** Thrown when a duplicate call is rejected by {@code @Idempotent(rejectDuplicates = true)}. */
public class DuplicateRequestException extends RuntimeException {

    public DuplicateRequestException(String message) {
        super(message);
    }
}
