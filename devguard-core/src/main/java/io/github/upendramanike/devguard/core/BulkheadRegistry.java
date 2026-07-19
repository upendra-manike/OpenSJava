package io.github.upendramanike.devguard.core;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

/** Holds named {@link Semaphore}s used to bound method concurrency for {@code @Bulkhead}. */
public class BulkheadRegistry {

    private final ConcurrentHashMap<String, Semaphore> semaphores = new ConcurrentHashMap<>();

    public Semaphore get(String name, int maxConcurrent) {
        return semaphores.computeIfAbsent(name, n -> new Semaphore(maxConcurrent, true));
    }
}
