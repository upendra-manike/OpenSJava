package io.github.upendramanike.devguard.core;

import java.util.concurrent.ConcurrentHashMap;

/** Holds named token buckets used by {@code @RateLimit}. */
public class RateLimiterRegistry {

    private final ConcurrentHashMap<String, TokenBucket> buckets = new ConcurrentHashMap<>();

    /**
     * Attempts to consume one permit from the bucket identified by {@code bucketKey}.
     *
     * @param bucketKey unique bucket identity (method + resolved key)
     * @param capacity maximum permits ({@code requests})
     * @param windowSeconds rolling window over which the bucket fully refills
     * @return {@code true} if a permit was granted
     */
    public boolean tryAcquire(String bucketKey, int capacity, long windowSeconds) {
        TokenBucket bucket = buckets.computeIfAbsent(bucketKey, k -> new TokenBucket(capacity, windowSeconds));
        return bucket.tryConsume();
    }

    /** Lazily-refilling token bucket. */
    static final class TokenBucket {

        private final int capacity;
        private final double refillPerNano;
        private double tokens;
        private long lastRefillNanos;

        TokenBucket(int capacity, long windowSeconds) {
            this.capacity = capacity;
            double window = Math.max(1L, windowSeconds);
            this.refillPerNano = capacity / (window * 1_000_000_000d);
            this.tokens = capacity;
            this.lastRefillNanos = System.nanoTime();
        }

        synchronized boolean tryConsume() {
            long now = System.nanoTime();
            double refill = (now - lastRefillNanos) * refillPerNano;
            if (refill > 0) {
                tokens = Math.min(capacity, tokens + refill);
                lastRefillNanos = now;
            }
            if (tokens >= 1d) {
                tokens -= 1d;
                return true;
            }
            return false;
        }
    }
}
