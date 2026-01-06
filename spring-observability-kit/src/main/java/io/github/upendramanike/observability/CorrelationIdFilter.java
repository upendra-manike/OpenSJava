package io.github.upendramanike.observability;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;

import java.io.IOException;
import java.util.UUID;

/**
 * Servlet filter that automatically generates and propagates correlation IDs
 * for HTTP requests. The correlation ID is:
 * <ul>
 *   <li>Read from the {@code X-Correlation-ID} header if present</li>
 *   <li>Generated as a UUID if not present</li>
 *   <li>Added to MDC for logging</li>
 *   <li>Added to the response header</li>
 * </ul>
 *
 * <p>This enables request tracing across async operations, HTTP clients,
 * and message queues when used with MDC-aware logging frameworks.
 */
public class CorrelationIdFilter implements Filter {

    private static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    private static final String MDC_KEY = "correlationId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            chain.doFilter(request, response);
            return;
        }
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        try {
            String correlationId = extractOrGenerateCorrelationId(httpRequest);
            MDC.put(MDC_KEY, correlationId);
            httpResponse.setHeader(CORRELATION_ID_HEADER, correlationId);
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }

    /**
     * Extracts correlation ID from request header or generates a new one.
     *
     * @param request the HTTP request
     * @return the correlation ID to use
     */
    private String extractOrGenerateCorrelationId(HttpServletRequest request) {
        String existingId = request.getHeader(CORRELATION_ID_HEADER);
        return existingId != null && !existingId.isBlank() 
            ? existingId 
            : UUID.randomUUID().toString();
    }
}

