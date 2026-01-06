package io.github.upendramanike.observability;

import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CorrelationIdFilterTest {

    @Test
    void shouldGenerateCorrelationIdWhenNotPresent() throws Exception {
        CorrelationIdFilter filter = new CorrelationIdFilter();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        
        when(request.getHeader("X-Correlation-ID")).thenReturn(null);
        
        doAnswer(invocation -> {
            // Check MDC while filter is active (before finally block clears it)
            assertNotNull(MDC.get("correlationId"));
            return null;
        }).when(chain).doFilter(any(), any());
        
        filter.doFilter(request, response, chain);
        
        verify(response).setHeader(eq("X-Correlation-ID"), anyString());
        verify(chain).doFilter(request, response);
    }

    @Test
    void shouldUseExistingCorrelationId() throws Exception {
        CorrelationIdFilter filter = new CorrelationIdFilter();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        
        String existingId = "existing-correlation-id";
        when(request.getHeader("X-Correlation-ID")).thenReturn(existingId);
        
        doAnswer(invocation -> {
            // Check MDC while filter is active (before finally block clears it)
            assertEquals(existingId, MDC.get("correlationId"));
            return null;
        }).when(chain).doFilter(any(), any());
        
        filter.doFilter(request, response, chain);
        
        verify(response).setHeader("X-Correlation-ID", existingId);
        verify(chain).doFilter(request, response);
    }
}

