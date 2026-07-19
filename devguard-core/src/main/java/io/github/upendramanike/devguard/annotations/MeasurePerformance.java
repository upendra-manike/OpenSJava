package io.github.upendramanike.devguard.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Measures execution time and heap allocation of the annotated method and logs a warning when the
 * configured threshold is exceeded.
 *
 * <p>Example:
 * <pre>{@code
 * @MeasurePerformance(warnAboveMillis = 200)
 * public Report generate() { ... }
 * }</pre>
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MeasurePerformance {

    /** Emit a warning when execution takes longer than this many milliseconds (0 = never warn). */
    long warnAboveMillis() default 0;

    /** When {@code true}, approximate heap usage delta is captured and reported. */
    boolean trackMemory() default true;
}
