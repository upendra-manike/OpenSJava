package io.github.upendramanike.devguard.aspect;

import io.github.upendramanike.devguard.annotations.DistributedLock;
import io.github.upendramanike.devguard.core.DistributedLockException;
import io.github.upendramanike.devguard.core.LockProvider;
import io.github.upendramanike.devguard.core.MethodInvocationContext;
import io.github.upendramanike.devguard.core.SpelKeyResolver;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/** Implements {@link DistributedLock}: runs the method while holding a named lock. */
@Aspect
@Order(Ordered.LOWEST_PRECEDENCE - 50)
public class DistributedLockAspect {

    private final LockProvider lockProvider;
    private final SpelKeyResolver spel;

    public DistributedLockAspect(LockProvider lockProvider, SpelKeyResolver spel) {
        this.lockProvider = lockProvider;
        this.spel = spel;
    }

    @Around("@annotation(distributedLock)")
    public Object around(ProceedingJoinPoint pjp, DistributedLock distributedLock) throws Throwable {
        MethodSignature sig = (MethodSignature) pjp.getSignature();
        var ctx = new MethodInvocationContext(sig.getMethod(), pjp.getArgs(), spel.parameterNames());
        String key = distributedLock.prefix() + spel.key(distributedLock.key(), ctx);

        LockProvider.LockHandle handle =
                lockProvider.tryAcquire(key, distributedLock.waitMillis(), distributedLock.leaseMillis());
        if (handle == null) {
            throw new DistributedLockException("Could not acquire lock: " + key);
        }
        try {
            return pjp.proceed();
        } finally {
            handle.release();
        }
    }
}
