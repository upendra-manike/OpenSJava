package io.github.upendramanike.apishield;

import java.util.regex.Pattern;

/**
 * Validates API requests to prevent common security issues.
 */
public final class RequestValidator {

    private static final Pattern SQL_INJECTION_PATTERN = Pattern.compile(
            "(?i)(union|select|insert|update|delete|drop|create|alter|exec|execute|script)");
    private static final Pattern XSS_PATTERN = Pattern.compile(
            "(?i)(<script|javascript:|onerror=|onload=)");

    private RequestValidator() {
    }

    /**
     * Validates that a string doesn't contain SQL injection patterns.
     *
     * @param input the input to validate
     * @throws ValidationException if SQL injection pattern is detected
     */
    public static void validateNoSqlInjection(String input) throws ValidationException {
        if (input != null && SQL_INJECTION_PATTERN.matcher(input).find()) {
            throw new ValidationException("Potential SQL injection detected");
        }
    }

    /**
     * Validates that a string doesn't contain XSS patterns.
     *
     * @param input the input to validate
     * @throws ValidationException if XSS pattern is detected
     */
    public static void validateNoXss(String input) throws ValidationException {
        if (input != null && XSS_PATTERN.matcher(input).find()) {
            throw new ValidationException("Potential XSS detected");
        }
    }

    /**
     * Validates input length.
     *
     * @param input the input to validate
     * @param maxLength the maximum allowed length
     * @throws ValidationException if input exceeds max length
     */
    public static void validateLength(String input, int maxLength) throws ValidationException {
        if (input != null && input.length() > maxLength) {
            throw new ValidationException("Input exceeds maximum length of " + maxLength);
        }
    }

    /**
     * Exception thrown when validation fails.
     */
    public static class ValidationException extends Exception {
        public ValidationException(String message) {
            super(message);
        }
    }
}


