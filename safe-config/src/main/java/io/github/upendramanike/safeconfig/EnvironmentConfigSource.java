package io.github.upendramanike.safeconfig;

/**
 * A {@link ConfigSource} that reads from environment variables first,
 * then falls back to system properties.
 */
public final class EnvironmentConfigSource implements ConfigSource {

    @Override
    public String get(String key) {
        String value = System.getenv(key);
        if (value != null) {
            return value;
        }
        return System.getProperty(key);
    }
}



