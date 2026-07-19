package io.github.upendramanike.devguard.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Logs method entry/exit, arguments, return value, and execution time for the annotated method.
 *
 * <p>Example:
 * <pre>{@code
 * @LogExecution
 * public Order findOrder(Long id) { ... }
 * }</pre>
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogExecution {

    /** When {@code true}, method arguments are logged. */
    boolean logArgs() default true;

    /** When {@code true}, the return value is logged. */
    boolean logResult() default false;

    /** Log level to use ({@code TRACE}, {@code DEBUG}, {@code INFO}, {@code WARN}, {@code ERROR}). */
    String level() default "INFO";
}
