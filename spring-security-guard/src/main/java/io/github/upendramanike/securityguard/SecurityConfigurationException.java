package io.github.upendramanike.securityguard;

/**
 * Thrown when security configuration validation fails.
 */
public class SecurityConfigurationException extends RuntimeException {

    /**
     * Creates a new security configuration exception.
     *
     * @param message the error message
     */
    public SecurityConfigurationException(String message) {
        super(message);
    }

    /**
     * Creates a new security configuration exception.
     *
     * @param message the error message
     * @param cause the cause
     */
    public SecurityConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}

