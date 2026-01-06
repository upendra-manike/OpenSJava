package io.github.upendramanike.asyncflow;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

/**
 * Structured concurrency utilities for async task execution.
 */
public final class TaskExecutor {

    private final Executor executor;

    private TaskExecutor(Executor executor) {
        this.executor = executor;
    }

    /**
     * Creates a new task executor with a default thread pool.
     *
     * @return a new task executor
     */
    public static TaskExecutor create() {
        return new TaskExecutor(Executors.newCachedThreadPool());
    }

    /**
     * Creates a new task executor with a custom executor.
     *
     * @param executor the executor to use
     * @return a new task executor
     */
    public static TaskExecutor create(Executor executor) {
        return new TaskExecutor(executor);
    }

    /**
     * Executes a task asynchronously.
     *
     * @param <T> the return type
     * @param task the task to execute
     * @return a CompletableFuture representing the result
     */
    public <T> CompletableFuture<T> execute(Supplier<T> task) {
        return CompletableFuture.supplyAsync(task, executor);
    }

    /**
     * Executes multiple tasks and waits for all to complete.
     *
     * @param <T> the return type
     * @param tasks the tasks to execute
     * @return a CompletableFuture that completes when all tasks are done
     */
    @SafeVarargs
    public final <T> CompletableFuture<Void> executeAll(Supplier<T>... tasks) {
        CompletableFuture<?>[] futures = new CompletableFuture[tasks.length];
        for (int i = 0; i < tasks.length; i++) {
            futures[i] = execute(tasks[i]);
        }
        return CompletableFuture.allOf(futures);
    }
}

