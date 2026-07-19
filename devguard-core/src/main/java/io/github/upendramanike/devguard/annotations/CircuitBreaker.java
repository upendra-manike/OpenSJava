package io.github.upendramanike.devguard.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Wraps the annotated method with a circuit breaker that transitions between CLOSED, OPEN, and
 * HALF_OPEN states to stop hammering a failing dependency.
 *
 * <p>Example:
 * <pre>{@code
 * @CircuitBreaker(failureThreshold = 5, openMillis = 10000)
 * public String callDownstream() { ... }
 * }</pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CircuitBreaker {

    /** Optional explicit breaker name; defaults to the method signature when empty. */
    String name() default "";

    /** Consecutive failures that trip the breaker from CLOSED to OPEN. */
    int failureThreshold() default 5;

    /** How long the breaker stays OPEN before allowing a trial call (HALF_OPEN), in milliseconds. */
    long openMillis() default 30000;

    /** Consecutive successes in HALF_OPEN required to return to CLOSED. */
    int halfOpenSuccesses() default 1;
}
