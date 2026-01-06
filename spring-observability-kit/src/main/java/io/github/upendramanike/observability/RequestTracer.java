package io.github.upendramanike.observability;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;

/**
 * Utility for tracing request lifecycle and measuring execution time
 * across async operations. Automatically includes correlation IDs from MDC.
 *
 * <p>Example usage:
 * <pre>{@code
 * RequestTracer tracer = RequestTracer.create();
 * tracer.trace("processOrder", () -> {
 *     // Your business logic
 *     return orderService.process(orderId);
 * });
 * }</pre>
 */
public class RequestTracer {

    private static final Logger logger = LoggerFactory.getLogger(RequestTracer.class);

    private RequestTracer() {
    }

    /**
     * Creates a new request tracer instance.
     *
     * @return a request tracer
     */
    public static RequestTracer create() {
        return new RequestTracer();
    }

    /**
     * Traces the execution of a supplier, logging start, completion, and duration.
     *
     * @param operationName the name of the operation being traced
     * @param operation the operation to execute
     * @param <T> the return type
     * @return the result of the operation
     */
    public <T> T trace(String operationName, Supplier<T> operation) {
        Instant start = Instant.now();
        String correlationId = MDC.get("correlationId");
        
        logger.debug("Starting operation: {} [correlationId: {}]", operationName, correlationId);
        
        try {
            T result = operation.get();
            Duration duration = Duration.between(start, Instant.now());
            logger.info("Completed operation: {} in {}ms [correlationId: {}]", 
                operationName, duration.toMillis(), correlationId);
            return result;
        } catch (Exception e) {
            Duration duration = Duration.between(start, Instant.now());
            logger.error("Failed operation: {} after {}ms [correlationId: {}]", 
                operationName, duration.toMillis(), correlationId, e);
            throw e;
        }
    }

    /**
     * Traces the execution of a runnable, logging start, completion, and duration.
     *
     * @param operationName the name of the operation being traced
     * @param operation the operation to execute
     */
    public void trace(String operationName, Runnable operation) {
        trace(operationName, () -> {
            operation.run();
            return null;
        });
    }
}

