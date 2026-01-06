package io.github.upendramanike.safeconfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a configuration property as required.
 * <p>
 * If a value cannot be resolved for a required field during binding,
 * {@link ConfigBindingException} will be thrown.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Required {
}



