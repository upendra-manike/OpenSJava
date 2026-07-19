package io.github.upendramanike.devguard.core;

/**
 * Simple TTL cache used by {@code @CacheResult}. The default {@link InMemoryResultCache} is single-JVM;
 * supply a Redis-backed implementation for a shared cache.
 */
public interface ResultCache {

    /** Returns the cached value, or {@code null} on a miss. */
    Object get(String cacheName, String key);

    /** Stores a non-null value for {@code ttlSeconds}. */
    void put(String cacheName, String key, Object value, long ttlSeconds);
}
