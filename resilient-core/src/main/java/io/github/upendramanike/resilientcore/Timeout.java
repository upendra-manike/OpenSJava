package io.github.upendramanike.resilientcore;

import java.time.Duration;
import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * Utility for executing operations with a timeout.
 */
public final class Timeout {

    private Timeout() {
    }

    /**
     * Executes a supplier with a timeout.
     *
     * @param <T> the return type
     * @param supplier the operation to execute
     * @param timeout the maximum time to wait
     * @return the result of the supplier
     * @throws TimeoutException if the operation exceeds the timeout
     */
    public static <T> T execute(Supplier<T> supplier, Duration timeout) throws TimeoutException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<T> future = executor.submit(supplier::get);
            return future.get(timeout.toMillis(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Operation interrupted", e);
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            throw new RuntimeException("Operation failed", cause);
        } catch (java.util.concurrent.TimeoutException e) {
            throw new TimeoutException("Operation timed out after " + timeout);
        } finally {
            executor.shutdownNow();
        }
    }

    /**
     * Executes a runnable with a timeout.
     *
     * @param runnable the operation to execute
     * @param timeout the maximum time to wait
     * @throws TimeoutException if the operation exceeds the timeout
     */
    public static void execute(Runnable runnable, Duration timeout) throws TimeoutException {
        execute(() -> {
            runnable.run();
            return null;
        }, timeout);
    }

    /**
     * Custom timeout exception.
     */
    public static class TimeoutException extends Exception {
        public TimeoutException(String message) {
            super(message);
        }
    }
}

