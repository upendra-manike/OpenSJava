package io.github.upendramanike.devguard.aspect;

import io.github.upendramanike.devguard.annotations.Retry;
import io.github.upendramanike.devguard.core.RetryExhaustedException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/** Implements {@link Retry}: retries failed invocations with exponential backoff. */
@Aspect
@Order(Ordered.LOWEST_PRECEDENCE - 10)
public class RetryAspect {

    private static final Logger log = LoggerFactory.getLogger(RetryAspect.class);

    @Around("@annotation(retry)")
    public Object around(ProceedingJoinPoint pjp, Retry retry) throws Throwable {
        int attempts = Math.max(1, retry.attempts());
        long delay = Math.max(0, retry.delay());
        String name = ((MethodSignature) pjp.getSignature()).getMethod().getName();
        Throwable last = null;

        for (int attempt = 1; attempt <= attempts; attempt++) {
            try {
                return pjp.proceed();
            } catch (Throwable ex) {
                last = ex;
                if (isAbort(retry, ex) || !isRetryable(retry, ex)) {
                    throw ex;
                }
                if (attempt == attempts) {
                    break;
                }
                log.warn("Retry {}/{} for {} after {}: {}", attempt, attempts, name, delay + "ms", ex.toString());
                sleep(delay);
                delay = nextDelay(delay, retry);
            }
        }
        throw new RetryExhaustedException(
                "Retry exhausted after " + attempts + " attempts for " + name, last);
    }

    private long nextDelay(long delay, Retry retry) {
        long next = (long) (delay * Math.max(1.0, retry.backoff()));
        if (retry.maxDelay() > 0) {
            next = Math.min(next, retry.maxDelay());
        }
        return next;
    }

    private boolean isRetryable(Retry retry, Throwable ex) {
        for (Class<? extends Throwable> type : retry.retryOn()) {
            if (type.isInstance(ex)) {
                return true;
            }
        }
        return false;
    }

    private boolean isAbort(Retry retry, Throwable ex) {
        for (Class<? extends Throwable> type : retry.abortOn()) {
            if (type.isInstance(ex)) {
                return true;
            }
        }
        return false;
    }

    private void sleep(long millis) {
        if (millis <= 0) {
            return;
        }
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RetryExhaustedException("Retry interrupted", e);
        }
    }
}
