package io.github.upendramanike.asyncflow;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Token for cancelling async operations.
 */
public final class CancellationToken {

    private final AtomicBoolean cancelled = new AtomicBoolean(false);

    /**
     * Checks if cancellation has been requested.
     *
     * @return true if cancelled
     */
    public boolean isCancelled() {
        return cancelled.get();
    }

    /**
     * Requests cancellation.
     */
    public void cancel() {
        cancelled.set(true);
    }

    /**
     * Throws CancellationException if cancellation has been requested.
     *
     * @throws CancellationException if cancelled
     */
    public void checkCancellation() throws CancellationException {
        if (isCancelled()) {
            throw new CancellationException("Operation was cancelled");
        }
    }

    /**
     * Exception thrown when an operation is cancelled.
     */
    public static class CancellationException extends RuntimeException {
        public CancellationException(String message) {
            super(message);
        }
    }
}

