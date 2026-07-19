package io.github.upendramanike.devguard.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Gates the annotated method behind a feature flag resolved by the configured
 * {@link io.github.upendramanike.devguard.core.FeatureFlagProvider}. When the flag is disabled the
 * method is skipped and returns a default value (or throws, depending on configuration).
 *
 * <p>Example:
 * <pre>{@code
 * @FeatureFlag("NEW_PAYMENT")
 * public PaymentResponse payV2() { ... }
 * }</pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FeatureFlag {

    /** Name of the feature flag to check. */
    String value();

    /**
     * When {@code true}, invoking a disabled feature throws
     * {@link io.github.upendramanike.devguard.core.FeatureDisabledException} instead of returning a
     * default value.
     */
    boolean failIfDisabled() default false;
}
