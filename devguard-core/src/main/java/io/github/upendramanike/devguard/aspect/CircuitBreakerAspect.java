package io.github.upendramanike.devguard.aspect;

import io.github.upendramanike.devguard.annotations.CircuitBreaker;
import io.github.upendramanike.devguard.core.CircuitBreakerRegistry;
import io.github.upendramanike.devguard.core.CircuitOpenException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;

/** Implements {@link CircuitBreaker} with a CLOSED/OPEN/HALF_OPEN state machine. */
@Aspect
@Order(Ordered.LOWEST_PRECEDENCE - 20)
public class CircuitBreakerAspect {

    private final CircuitBreakerRegistry registry;

    public CircuitBreakerAspect(CircuitBreakerRegistry registry) {
        this.registry = registry;
    }

    @Around("@annotation(circuitBreaker)")
    public Object around(ProceedingJoinPoint pjp, CircuitBreaker circuitBreaker) throws Throwable {
        MethodSignature sig = (MethodSignature) pjp.getSignature();
        String name = StringUtils.hasText(circuitBreaker.name())
                ? circuitBreaker.name()
                : Aspects.signature(sig.getMethod());
        CircuitBreakerRegistry.Breaker breaker = registry.get(
                name,
                circuitBreaker.failureThreshold(),
                circuitBreaker.openMillis(),
                circuitBreaker.halfOpenSuccesses());

        if (!breaker.allowRequest()) {
            throw new CircuitOpenException("Circuit breaker OPEN for " + name);
        }
        try {
            Object result = pjp.proceed();
            breaker.recordSuccess();
            return result;
        } catch (Throwable ex) {
            breaker.recordFailure();
            throw ex;
        }
    }
}
