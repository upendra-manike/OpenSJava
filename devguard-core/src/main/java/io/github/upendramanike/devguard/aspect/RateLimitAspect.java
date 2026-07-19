package io.github.upendramanike.devguard.aspect;

import io.github.upendramanike.devguard.annotations.RateLimit;
import io.github.upendramanike.devguard.core.MethodInvocationContext;
import io.github.upendramanike.devguard.core.RateLimitExceededException;
import io.github.upendramanike.devguard.core.RateLimiterRegistry;
import io.github.upendramanike.devguard.core.SpelKeyResolver;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;

/** Implements {@link RateLimit} using a token bucket per resolved key. */
@Aspect
@Order(Ordered.LOWEST_PRECEDENCE - 40)
public class RateLimitAspect {

    private final RateLimiterRegistry registry;
    private final SpelKeyResolver spel;

    public RateLimitAspect(RateLimiterRegistry registry, SpelKeyResolver spel) {
        this.registry = registry;
        this.spel = spel;
    }

    @Around("@annotation(rateLimit)")
    public Object around(ProceedingJoinPoint pjp, RateLimit rateLimit) throws Throwable {
        MethodSignature sig = (MethodSignature) pjp.getSignature();
        String sub = "*";
        if (StringUtils.hasText(rateLimit.key())) {
            var ctx = new MethodInvocationContext(sig.getMethod(), pjp.getArgs(), spel.parameterNames());
            sub = spel.key(rateLimit.key(), ctx);
        }
        String bucketKey = Aspects.signature(sig.getMethod()) + ":" + sub;
        if (!registry.tryAcquire(bucketKey, rateLimit.requests(), rateLimit.duration())) {
            throw new RateLimitExceededException(
                    "Rate limit exceeded for " + sig.getMethod().getName() + " [" + sub + "]");
        }
        return pjp.proceed();
    }
}
