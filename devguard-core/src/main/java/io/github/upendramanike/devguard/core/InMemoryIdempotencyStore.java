package io.github.upendramanike.devguard.core;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/** Single-JVM {@link IdempotencyStore} with lazy TTL eviction. */
public class InMemoryIdempotencyStore implements IdempotencyStore {

    private record Entry(Object value, long expiresAtMillis) {}

    private final ConcurrentHashMap<String, Entry> store = new ConcurrentHashMap<>();

    @Override
    public Optional<StoredResult> find(String key) {
        Entry entry = store.get(key);
        if (entry == null) {
            return Optional.empty();
        }
        if (entry.expiresAtMillis() < System.currentTimeMillis()) {
            store.remove(key, entry);
            return Optional.empty();
        }
        return Optional.of(new StoredResult(entry.value()));
    }

    @Override
    public void store(String key, Object result, long ttlSeconds) {
        long expiresAt = System.currentTimeMillis() + ttlSeconds * 1000L;
        store.put(key, new Entry(result, expiresAt));
    }
}
