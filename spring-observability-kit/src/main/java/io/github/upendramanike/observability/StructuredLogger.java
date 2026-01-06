package io.github.upendramanike.observability;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Map;

/**
 * Structured logging utility that automatically includes correlation IDs
 * and other context in log messages. Designed for JSON log aggregation systems.
 *
 * <p>Example usage:
 * <pre>{@code
 * StructuredLogger logger = StructuredLogger.forClass(MyService.class);
 * logger.info("User action", Map.of("userId", "123", "action", "login"));
 * }</pre>
 *
 * <p>Output (when using JSON layout):
 * <pre>{@code
 * {
 *   "level": "INFO",
 *   "message": "User action",
 *   "correlationId": "abc-123",
 *   "userId": "123",
 *   "action": "login"
 * }
 * }</pre>
 */
public class StructuredLogger {

    private final Logger delegate;

    private StructuredLogger(Class<?> clazz) {
        this.delegate = LoggerFactory.getLogger(clazz);
    }

    /**
     * Creates a structured logger for the given class.
     *
     * @param clazz the class to create a logger for
     * @return a structured logger instance
     */
    public static StructuredLogger forClass(Class<?> clazz) {
        return new StructuredLogger(clazz);
    }

    /**
     * Logs an info message with structured context.
     *
     * @param message the log message
     * @param context additional context fields to include
     */
    public void info(String message, Map<String, Object> context) {
        withContext(context, () -> delegate.info(message));
    }

    /**
     * Logs a warning message with structured context.
     *
     * @param message the log message
     * @param context additional context fields to include
     */
    public void warn(String message, Map<String, Object> context) {
        withContext(context, () -> delegate.warn(message));
    }

    /**
     * Logs an error message with structured context.
     *
     * @param message the log message
     * @param context additional context fields to include
     */
    public void error(String message, Map<String, Object> context) {
        withContext(context, () -> delegate.error(message));
    }

    /**
     * Logs an error message with exception and structured context.
     *
     * @param message the log message
     * @param throwable the exception
     * @param context additional context fields to include
     */
    public void error(String message, Throwable throwable, Map<String, Object> context) {
        withContext(context, () -> delegate.error(message, throwable));
    }

    private void withContext(Map<String, Object> context, Runnable logAction) {
        if (context != null && !context.isEmpty()) {
            context.forEach((key, value) -> MDC.put(key, String.valueOf(value)));
        }
        try {
            logAction.run();
        } finally {
            if (context != null) {
                context.keySet().forEach(MDC::remove);
            }
        }
    }
}

