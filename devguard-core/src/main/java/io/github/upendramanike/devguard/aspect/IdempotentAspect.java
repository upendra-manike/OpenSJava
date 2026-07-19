package io.github.upendramanike.devguard.aspect;

import io.github.upendramanike.devguard.annotations.Idempotent;
import io.github.upendramanike.devguard.core.DuplicateRequestException;
import io.github.upendramanike.devguard.core.IdempotencyStore;
import io.github.upendramanike.devguard.core.MethodInvocationContext;
import io.github.upendramanike.devguard.core.SpelKeyResolver;
import java.util.Optional;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/** Implements {@link Idempotent}: replays (or rejects) duplicate calls sharing an idempotency key. */
@Aspect
@Order(Ordered.LOWEST_PRECEDENCE - 30)
public class IdempotentAspect {

    private final IdempotencyStore store;
    private final SpelKeyResolver spel;

    public IdempotentAspect(IdempotencyStore store, SpelKeyResolver spel) {
        this.store = store;
        this.spel = spel;
    }

    @Around("@annotation(idempotent)")
    public Object around(ProceedingJoinPoint pjp, Idempotent idempotent) throws Throwable {
        MethodSignature sig = (MethodSignature) pjp.getSignature();
        var ctx = new MethodInvocationContext(sig.getMethod(), pjp.getArgs(), spel.parameterNames());
        String key = Aspects.signature(sig.getMethod()) + ":" + spel.key(idempotent.key(), ctx);

        Optional<IdempotencyStore.StoredResult> existing = store.find(key);
        if (existing.isPresent()) {
            if (idempotent.rejectDuplicates()) {
                throw new DuplicateRequestException("Duplicate request rejected for key " + key);
            }
            return existing.get().value();
        }
        Object result = pjp.proceed();
        store.store(key, result, idempotent.ttlSeconds());
        return result;
    }
}
