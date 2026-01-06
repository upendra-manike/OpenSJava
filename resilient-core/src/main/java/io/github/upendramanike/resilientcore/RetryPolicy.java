package io.github.upendramanike.resilientcore;

import java.time.Duration;
import java.util.function.Supplier;

/**
 * Configurable retry policy for resilient operations.
 * Supports exponential backoff and maximum retry attempts.
 */
public final class RetryPolicy {

    private final int maxAttempts;
    private final Duration initialDelay;
    private final double backoffMultiplier;
    private final Duration maxDelay;
    private final Class<? extends Throwable>[] retryableExceptions;

    @SafeVarargs
    private RetryPolicy(int maxAttempts, Duration initialDelay, double backoffMultiplier,
                       Duration maxDelay, Class<? extends Throwable>... retryableExceptions) {
        this.maxAttempts = maxAttempts;
        this.initialDelay = initialDelay;
        this.backoffMultiplier = backoffMultiplier;
        this.maxDelay = maxDelay;
        this.retryableExceptions = retryableExceptions;
    }

    /**
     * Creates a new retry policy builder.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Executes the supplier with retry logic.
     *
     * @param <T> the return type
     * @param supplier the operation to retry
     * @return the result of the supplier
     * @throws RuntimeException if all retries are exhausted
     */
    public <T> T execute(Supplier<T> supplier) {
        Throwable lastException = null;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                return supplier.get();
            } catch (Throwable e) {
                lastException = e;
                if (!isRetryable(e) || attempt == maxAttempts) {
                    throw new RuntimeException("Operation failed after " + attempt + " attempts", e);
                }
                sleep(calculateDelay(attempt));
            }
        }
        throw new RuntimeException("Operation failed after " + maxAttempts + " attempts", lastException);
    }

    private boolean isRetryable(Throwable e) {
        if (retryableExceptions.length == 0) {
            return true; // Retry all exceptions by default
        }
        for (Class<? extends Throwable> retryable : retryableExceptions) {
            if (retryable.isInstance(e)) {
                return true;
            }
        }
        return false;
    }

    private Duration calculateDelay(int attempt) {
        long delayMillis = (long) (initialDelay.toMillis() * Math.pow(backoffMultiplier, attempt - 1));
        delayMillis = Math.min(delayMillis, maxDelay.toMillis());
        return Duration.ofMillis(delayMillis);
    }

    private void sleep(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Retry interrupted", e);
        }
    }

    /**
     * Builder for creating retry policies.
     */
    public static final class Builder {
        private int maxAttempts = 3;
        private Duration initialDelay = Duration.ofMillis(100);
        private double backoffMultiplier = 2.0;
        private Duration maxDelay = Duration.ofSeconds(30);
        private Class<? extends Throwable>[] retryableExceptions = new Class[0];

        private Builder() {
        }

        /**
         * Sets the maximum number of retry attempts.
         *
         * @param maxAttempts the maximum attempts (must be >= 1)
         * @return this builder
         */
        public Builder maxAttempts(int maxAttempts) {
            if (maxAttempts < 1) {
                throw new IllegalArgumentException("maxAttempts must be >= 1");
            }
            this.maxAttempts = maxAttempts;
            return this;
        }

        /**
         * Sets the initial delay before the first retry.
         *
         * @param initialDelay the initial delay
         * @return this builder
         */
        public Builder initialDelay(Duration initialDelay) {
            this.initialDelay = initialDelay;
            return this;
        }

        /**
         * Sets the backoff multiplier for exponential backoff.
         *
         * @param multiplier the multiplier (must be >= 1.0)
         * @return this builder
         */
        public Builder backoffMultiplier(double multiplier) {
            if (multiplier < 1.0) {
                throw new IllegalArgumentException("backoffMultiplier must be >= 1.0");
            }
            this.backoffMultiplier = multiplier;
            return this;
        }

        /**
         * Sets the maximum delay between retries.
         *
         * @param maxDelay the maximum delay
         * @return this builder
         */
        public Builder maxDelay(Duration maxDelay) {
            this.maxDelay = maxDelay;
            return this;
        }

        /**
         * Sets which exceptions should trigger a retry.
         *
         * @param exceptions the exception types to retry on
         * @return this builder
         */
        @SafeVarargs
        public final Builder retryOn(Class<? extends Throwable>... exceptions) {
            this.retryableExceptions = exceptions;
            return this;
        }

        /**
         * Builds the retry policy.
         *
         * @return a new retry policy
         */
        public RetryPolicy build() {
            return new RetryPolicy(maxAttempts, initialDelay, backoffMultiplier, maxDelay, retryableExceptions);
        }
    }
}

