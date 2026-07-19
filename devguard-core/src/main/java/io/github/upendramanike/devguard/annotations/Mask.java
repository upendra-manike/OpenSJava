package io.github.upendramanike.devguard.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a field as sensitive so that its value is masked when rendered through
 * {@link io.github.upendramanike.devguard.core.MaskingService}.
 *
 * <p>Example:
 * <pre>{@code
 * class User {
 *     @Mask(type = MaskType.EMAIL) String email;
 *     @Mask(type = MaskType.PHONE) String phone;
 * }
 * }</pre>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Mask {

    /** How the value should be masked. */
    MaskType type() default MaskType.FULL;

    /** Character used to replace hidden portions of the value. */
    char maskChar() default '*';
}
