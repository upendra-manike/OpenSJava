package io.github.upendramanike.devguard.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Guarantees that a method with the same idempotency key executes at most once within a time window.
 * The result of the first successful call is stored and replayed for duplicate calls.
 *
 * <p>Example:
 * <pre>{@code
 * @Idempotent(key = "#request.transactionId", ttlSeconds = 3600)
 * public PaymentResponse submit(PaymentRequest request) { ... }
 * }</pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Idempotent {

    /** SpEL expression evaluated against method arguments to derive the idempotency key. */
    String key();

    /** How long a completed result is remembered, in seconds. */
    long ttlSeconds() default 86400;

    /**
     * When {@code true}, a duplicate in-flight or completed call throws
     * {@link io.github.upendramanike.devguard.core.DuplicateRequestException} instead of replaying
     * the stored result.
     */
    boolean rejectDuplicates() default false;
}
