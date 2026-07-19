package io.github.upendramanike.devguard.core;

import java.util.concurrent.ConcurrentHashMap;

/** Single-JVM {@link ResultCache} with lazy TTL eviction. */
public class InMemoryResultCache implements ResultCache {

    private record Entry(Object value, long expiresAtMillis) {}

    private final ConcurrentHashMap<String, Entry> store = new ConcurrentHashMap<>();

    @Override
    public Object get(String cacheName, String key) {
        String composite = cacheName + "::" + key;
        Entry entry = store.get(composite);
        if (entry == null) {
            return null;
        }
        if (entry.expiresAtMillis() < System.currentTimeMillis()) {
            store.remove(composite, entry);
            return null;
        }
        return entry.value();
    }

    @Override
    public void put(String cacheName, String key, Object value, long ttlSeconds) {
        if (value == null) {
            return;
        }
        String composite = cacheName + "::" + key;
        store.put(composite, new Entry(value, System.currentTimeMillis() + ttlSeconds * 1000L));
    }
}
