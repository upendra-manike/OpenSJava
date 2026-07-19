package io.github.upendramanike.devguard.core;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/** Single-JVM {@link LockProvider} backed by {@link ReentrantLock} per key. */
public class InMemoryLockProvider implements LockProvider {

    private final ConcurrentHashMap<String, ReentrantLock> locks = new ConcurrentHashMap<>();

    @Override
    public LockHandle tryAcquire(String key, long waitMillis, long leaseMillis) {
        ReentrantLock lock = locks.computeIfAbsent(key, k -> new ReentrantLock());
        boolean acquired;
        try {
            acquired = lock.tryLock(Math.max(0, waitMillis), TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
        if (!acquired) {
            return null;
        }
        return new LockHandle() {
            private boolean released;

            @Override
            public void release() {
                if (!released) {
                    released = true;
                    lock.unlock();
                }
            }
        };
    }
}
