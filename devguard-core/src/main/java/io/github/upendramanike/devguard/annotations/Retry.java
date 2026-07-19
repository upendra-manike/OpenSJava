package io.github.upendramanike.devguard.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Retries the annotated method on failure with configurable attempts, delay, and exponential backoff.
 *
 * <p>Example:
 * <pre>{@code
 * @Retry(attempts = 3, delay = 1000, backoff = 2.0)
 * public PaymentResponse pay() { ... }
 * }</pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Retry {

    /** Maximum number of attempts (including the first). Must be at least 1. */
    int attempts() default 3;

    /** Initial delay between attempts, in milliseconds. */
    long delay() default 500;

    /** Multiplier applied to the delay after each failed attempt (1.0 = fixed delay). */
    double backoff() default 2.0;

    /** Maximum delay between attempts, in milliseconds (0 = no cap). */
    long maxDelay() default 0;

    /** Exception types that trigger a retry. Defaults to any {@link Exception}. */
    Class<? extends Throwable>[] retryOn() default {Exception.class};

    /** Exception types that must never be retried (takes precedence over {@link #retryOn()}). */
    Class<? extends Throwable>[] abortOn() default {};
}
