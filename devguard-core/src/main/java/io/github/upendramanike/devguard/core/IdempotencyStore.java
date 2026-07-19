package io.github.upendramanike.devguard.core;

import java.util.Optional;

/**
 * Stores the results of idempotent operations keyed by their idempotency key. The default
 * {@link InMemoryIdempotencyStore} is single-JVM; back it with Redis to deduplicate across instances.
 */
public interface IdempotencyStore {

    /** Returns the stored result for {@code key}, if a previous call completed. */
    Optional<StoredResult> find(String key);

    /** Stores the result of a completed call under {@code key} for {@code ttlSeconds}. */
    void store(String key, Object result, long ttlSeconds);

    /** Wrapper allowing {@code null} results to be distinguished from a cache miss. */
    record StoredResult(Object value) {}
}
