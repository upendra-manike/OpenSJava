package io.github.upendramanike.devguard.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Caches the return value of the annotated method for a fixed time-to-live using the configured
 * {@link io.github.upendramanike.devguard.core.ResultCache}.
 *
 * <p>Example:
 * <pre>{@code
 * @CacheResult(ttl = 300, key = "#id")
 * public Product findById(Long id) { ... }
 * }</pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheResult {

    /** Time-to-live for cached entries, in seconds. */
    long ttl() default 60;

    /** Logical cache name; defaults to the method signature when empty. */
    String cacheName() default "";

    /**
     * Optional SpEL key expression evaluated against method arguments. When empty a key is derived
     * from the argument values.
     */
    String key() default "";
}
