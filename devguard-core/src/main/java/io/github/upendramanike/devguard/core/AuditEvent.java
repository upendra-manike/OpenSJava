package io.github.upendramanike.devguard.core;

import java.time.Instant;
import java.util.Arrays;

/** Immutable record describing a single audited method invocation. */
public record AuditEvent(
        String action,
        String principal,
        Instant timestamp,
        Object[] arguments,
        Object result,
        boolean success,
        String errorMessage,
        long durationMillis) {

    @Override
    public String toString() {
        return "AuditEvent{action=" + action
                + ", principal=" + principal
                + ", timestamp=" + timestamp
                + ", success=" + success
                + ", durationMillis=" + durationMillis
                + (arguments != null ? ", arguments=" + Arrays.deepToString(arguments) : "")
                + (result != null ? ", result=" + result : "")
                + (errorMessage != null ? ", error=" + errorMessage : "")
                + '}';
    }
}
