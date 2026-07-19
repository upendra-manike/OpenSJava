package io.github.upendramanike.devguard.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Ensures the annotated method runs under a named lock so that only one holder executes it at a time
 * across threads (and, with a distributed {@link io.github.upendramanike.devguard.core.LockProvider},
 * across JVMs / instances).
 *
 * <p>Example:
 * <pre>{@code
 * @DistributedLock(key = "#orderId")
 * public void processOrder(Long orderId) { ... }
 * }</pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedLock {

    /** SpEL expression evaluated against method arguments to derive the lock key. */
    String key();

    /** Prefix prepended to the resolved key, useful for namespacing. */
    String prefix() default "devguard:lock:";

    /** How long to wait to acquire the lock before failing, in milliseconds. */
    long waitMillis() default 0;

    /** How long the lock is held before it auto-expires (lease time), in milliseconds. */
    long leaseMillis() default 30000;
}
