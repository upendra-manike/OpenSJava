package io.github.upendramanike.resilientcore;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class RetryPolicyTest {

    @Test
    void testSuccessfulExecution() {
        RetryPolicy policy = RetryPolicy.builder()
                .maxAttempts(3)
                .build();

        String result = policy.execute(() -> "success");
        assertEquals("success", result);
    }

    @Test
    void testRetryOnFailure() {
        RetryPolicy policy = RetryPolicy.builder()
                .maxAttempts(3)
                .initialDelay(Duration.ofMillis(10))
                .build();

        int[] attempts = {0};
        String result = policy.execute(() -> {
            attempts[0]++;
            if (attempts[0] < 3) {
                throw new RuntimeException("fail");
            }
            return "success";
        });

        assertEquals("success", result);
        assertEquals(3, attempts[0]);
    }

    @Test
    void testMaxAttemptsExceeded() {
        RetryPolicy policy = RetryPolicy.builder()
                .maxAttempts(2)
                .initialDelay(Duration.ofMillis(10))
                .build();

        assertThrows(RuntimeException.class, () -> {
            policy.execute(() -> {
                throw new RuntimeException("always fail");
            });
        });
    }
}


