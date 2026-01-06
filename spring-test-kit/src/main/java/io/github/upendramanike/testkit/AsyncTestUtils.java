package io.github.upendramanike.testkit;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Utilities for deterministic async testing in Spring Boot applications.
 * Helps avoid flaky tests caused by timing issues.
 *
 * <p>Example usage:
 * <pre>{@code
 * CompletableFuture<String> future = asyncOperation();
 * String result = AsyncTestUtils.await(future, Duration.ofSeconds(5));
 * assertEquals("expected", result);
 * }</pre>
 */
public class AsyncTestUtils {

    private AsyncTestUtils() {
    }

    /**
     * Awaits completion of a CompletableFuture with a timeout.
     *
     * @param future the future to await
     * @param timeout the maximum time to wait
     * @param <T> the result type
     * @return the result of the future
     * @throws AssertionError if the future doesn't complete within the timeout
     */
    public static <T> T await(CompletableFuture<T> future, Duration timeout) {
        try {
            return future.get(timeout.toMillis(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            throw new AssertionError("Future did not complete within " + timeout, e);
        }
    }

    /**
     * Executes a supplier and waits for its result with a timeout.
     * Useful for testing async operations that return CompletableFuture.
     *
     * @param supplier the supplier to execute
     * @param timeout the maximum time to wait
     * @param <T> the result type
     * @return the result
     * @throws AssertionError if the operation doesn't complete within the timeout
     */
    public static <T> T await(Supplier<CompletableFuture<T>> supplier, Duration timeout) {
        return await(supplier.get(), timeout);
    }

    /**
     * Waits for a condition to become true, polling at regular intervals.
     *
     * @param condition the condition to check
     * @param timeout the maximum time to wait
     * @param pollInterval the interval between checks
     * @throws AssertionError if the condition doesn't become true within the timeout
     */
    public static void waitFor(Supplier<Boolean> condition, Duration timeout, Duration pollInterval) {
        long endTime = System.currentTimeMillis() + timeout.toMillis();
        while (System.currentTimeMillis() < endTime) {
            if (condition.get()) {
                return;
            }
            try {
                Thread.sleep(pollInterval.toMillis());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new AssertionError("Interrupted while waiting for condition", e);
            }
        }
        throw new AssertionError("Condition did not become true within " + timeout);
    }
}

