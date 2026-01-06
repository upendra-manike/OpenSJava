package io.github.upendramanike.safeconfig;

/**
 * Thrown when configuration binding fails, for example because of missing
 * required values or type conversion problems.
 */
public final class ConfigBindingException extends RuntimeException {

    /**
     * Creates a new exception with the given message.
     *
     * @param message the error message
     */
    public ConfigBindingException(String message) {
        super(message);
    }

    /**
     * Creates a new exception with the given message and cause.
     *
     * @param message the error message
     * @param cause the cause of this exception
     */
    public ConfigBindingException(String message, Throwable cause) {
        super(message, cause);
    }
}


