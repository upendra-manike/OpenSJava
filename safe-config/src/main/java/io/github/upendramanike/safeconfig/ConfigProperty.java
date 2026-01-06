package io.github.upendramanike.safeconfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares how a field in a configuration class should be bound from key/value sources.
 * <p>
 * Example:
 * <pre>{@code
 *   public final class AppConfig {
 *     @ConfigProperty("APP_PORT")
 *     @Required
 *     int port;
 *   }
 * }</pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfigProperty {

    /**
     * Name of the key in the underlying configuration source (env, system props, map...).
     *
     * @return the configuration key name
     */
    String value();
}


