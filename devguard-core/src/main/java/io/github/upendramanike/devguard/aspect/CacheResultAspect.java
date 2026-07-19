package io.github.upendramanike.devguard.aspect;

import io.github.upendramanike.devguard.annotations.CacheResult;
import io.github.upendramanike.devguard.core.MethodInvocationContext;
import io.github.upendramanike.devguard.core.ResultCache;
import io.github.upendramanike.devguard.core.SpelKeyResolver;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;

/** Implements {@link CacheResult}: caches return values for a fixed TTL. */
@Aspect
@Order(Ordered.LOWEST_PRECEDENCE - 15)
public class CacheResultAspect {

    private final ResultCache cache;
    private final SpelKeyResolver spel;

    public CacheResultAspect(ResultCache cache, SpelKeyResolver spel) {
        this.cache = cache;
        this.spel = spel;
    }

    @Around("@annotation(cacheResult)")
    public Object around(ProceedingJoinPoint pjp, CacheResult cacheResult) throws Throwable {
        MethodSignature sig = (MethodSignature) pjp.getSignature();
        String cacheName = StringUtils.hasText(cacheResult.cacheName())
                ? cacheResult.cacheName()
                : Aspects.signature(sig.getMethod());
        var ctx = new MethodInvocationContext(sig.getMethod(), pjp.getArgs(), spel.parameterNames());
        String key = spel.key(cacheResult.key(), ctx);

        Object cached = cache.get(cacheName, key);
        if (cached != null) {
            return cached;
        }
        Object result = pjp.proceed();
        cache.put(cacheName, key, result, cacheResult.ttl());
        return result;
    }
}
