package io.github.upendramanike.asyncflow;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class TaskExecutorTest {

    @Test
    void testExecute() throws Exception {
        TaskExecutor executor = TaskExecutor.create();
        CompletableFuture<String> future = executor.execute(() -> "result");
        assertEquals("result", future.get());
    }

    @Test
    void testExecuteAll() throws Exception {
        TaskExecutor executor = TaskExecutor.create();
        CompletableFuture<Void> all = executor.executeAll(
                () -> "task1",
                () -> "task2",
                () -> "task3"
        );
        all.get(); // Should complete without exception
        assertTrue(all.isDone());
    }
}

