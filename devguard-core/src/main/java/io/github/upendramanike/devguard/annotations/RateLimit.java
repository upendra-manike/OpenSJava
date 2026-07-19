package io.github.upendramanike.devguard.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Limits how often the annotated method may be invoked, using a token-bucket algorithm.
 *
 * <p>Example: at most 100 requests per 60 seconds, keyed per user:
 * <pre>{@code
 * @RateLimit(requests = 100, duration = 60, key = "#userId")
 * public void call(String userId) { ... }
 * }</pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {

    /** Maximum number of permitted requests within {@link #duration()}. */
    int requests();

    /** Length of the rolling window, in seconds. */
    long duration() default 1;

    /**
     * Optional SpEL expression evaluated against method arguments to derive a per-caller bucket key.
     * When empty, a single bucket is shared for all invocations of the method.
     */
    String key() default "";
}
