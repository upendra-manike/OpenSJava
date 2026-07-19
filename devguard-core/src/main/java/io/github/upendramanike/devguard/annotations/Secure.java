package io.github.upendramanike.devguard.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Authorizes the annotated method against the current caller's roles/permissions supplied by the
 * configured {@link io.github.upendramanike.devguard.core.SecurityContextProvider}.
 *
 * <p>Example:
 * <pre>{@code
 * @Secure(roles = {"ADMIN", "MANAGER"})
 * public void deleteUser(Long id) { ... }
 * }</pre>
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Secure {

    /** Roles allowed to invoke the method; the caller must have at least one. */
    String[] roles() default {};

    /** Permissions allowed to invoke the method; the caller must have at least one. */
    String[] permissions() default {};
}
