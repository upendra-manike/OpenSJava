package io.github.upendramanike.safeconfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a configuration property as secret.
 * <p>
 * Values of fields annotated with this annotation will be masked by
 * {@link SecretMasking#safeToString(Object)} to avoid leaking secrets in logs.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Secret {
}


