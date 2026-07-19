package io.github.upendramanike.devguard.core;

/**
 * Strategy for acquiring named locks. The default {@link InMemoryLockProvider} coordinates threads in
 * a single JVM; supply a Redis/ZooKeeper/Hazelcast-backed implementation to coordinate across
 * instances.
 */
public interface LockProvider {

    /**
     * Attempts to acquire the lock for {@code key}.
     *
     * @param key the fully-qualified lock key
     * @param waitMillis how long to wait for the lock (0 = do not wait)
     * @param leaseMillis how long the lock may be held before auto-release (best effort)
     * @return a handle to release the lock, or {@code null} if it could not be acquired in time
     */
    LockHandle tryAcquire(String key, long waitMillis, long leaseMillis);

    /** Handle returned on successful acquisition; releasing is idempotent. */
    interface LockHandle extends AutoCloseable {
        void release();

        @Override
        default void close() {
            release();
        }
    }
}
