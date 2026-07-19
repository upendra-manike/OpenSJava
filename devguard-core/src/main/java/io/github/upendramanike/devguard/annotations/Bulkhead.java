package io.github.upendramanike.devguard.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Bounds the number of concurrent invocations of the annotated method to prevent thread-pool
 * starvation, using a semaphore.
 *
 * <p>Example:
 * <pre>{@code
 * @Bulkhead(maxConcurrent = 10, maxWaitMillis = 100)
 * public String callExpensiveService() { ... }
 * }</pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bulkhead {

    /** Optional explicit bulkhead name; defaults to the method signature when empty. */
    String name() default "";

    /** Maximum number of concurrent invocations permitted. */
    int maxConcurrent() default 10;

    /** How long to wait for a permit before failing, in milliseconds. */
    long maxWaitMillis() default 0;
}
