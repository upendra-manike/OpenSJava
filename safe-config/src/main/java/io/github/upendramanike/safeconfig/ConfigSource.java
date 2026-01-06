package io.github.upendramanike.safeconfig;

/**
 * Simple abstraction over a key/value configuration source.
 */
@FunctionalInterface
public interface ConfigSource {

    /**
     * Resolve a value for the given key.
     *
     * @param key configuration key
     * @return value or {@code null} if missing
     */
    String get(String key);
}



