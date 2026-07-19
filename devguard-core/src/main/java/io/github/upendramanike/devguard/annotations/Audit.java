package io.github.upendramanike.devguard.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Emits an audit event when the annotated method is invoked, capturing the action, actor, timing,
 * arguments, and outcome via the configured {@link io.github.upendramanike.devguard.core.AuditSink}.
 *
 * <p>Example:
 * <pre>{@code
 * @Audit(action = "UPDATE_CUSTOMER")
 * public void updateCustomer(Customer c) { ... }
 * }</pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Audit {

    /** Logical name of the audited action. */
    String action();

    /** When {@code true}, method arguments are included in the audit event. */
    boolean includeArgs() default true;

    /** When {@code true}, the return value is included in the audit event. */
    boolean includeResult() default false;
}
