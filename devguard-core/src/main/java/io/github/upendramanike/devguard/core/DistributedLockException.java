package io.github.upendramanike.devguard.core;

/** Thrown when a {@code @DistributedLock} cannot be acquired within the configured wait time. */
public class DistributedLockException extends RuntimeException {

    public DistributedLockException(String message) {
        super(message);
    }
}
