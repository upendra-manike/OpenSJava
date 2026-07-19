package io.github.upendramanike.devguard.core;

/** Thrown when the current caller is not authorized to invoke a {@code @Secure} method. */
public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException(String message) {
        super(message);
    }
}
