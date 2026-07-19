package io.github.upendramanike.devguard.aspect;

import io.github.upendramanike.devguard.annotations.Bulkhead;
import io.github.upendramanike.devguard.core.BulkheadFullException;
import io.github.upendramanike.devguard.core.BulkheadRegistry;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;

/** Implements {@link Bulkhead}: bounds concurrent invocations with a semaphore. */
@Aspect
@Order(Ordered.LOWEST_PRECEDENCE - 25)
public class BulkheadAspect {

    private final BulkheadRegistry registry;

    public BulkheadAspect(BulkheadRegistry registry) {
        this.registry = registry;
    }

    @Around("@annotation(bulkhead)")
    public Object around(ProceedingJoinPoint pjp, Bulkhead bulkhead) throws Throwable {
        MethodSignature sig = (MethodSignature) pjp.getSignature();
        String name = StringUtils.hasText(bulkhead.name())
                ? bulkhead.name()
                : Aspects.signature(sig.getMethod());
        Semaphore semaphore = registry.get(name, bulkhead.maxConcurrent());

        boolean acquired;
        try {
            acquired = semaphore.tryAcquire(Math.max(0, bulkhead.maxWaitMillis()), TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BulkheadFullException("Interrupted waiting for bulkhead " + name);
        }
        if (!acquired) {
            throw new BulkheadFullException("Bulkhead full for " + name);
        }
        try {
            return pjp.proceed();
        } finally {
            semaphore.release();
        }
    }
}
