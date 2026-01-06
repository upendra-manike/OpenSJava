package io.github.upendramanike.genai;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Tracks token usage for LLM API calls.
 */
public final class TokenTracker {

    private final AtomicLong inputTokens = new AtomicLong(0);
    private final AtomicLong outputTokens = new AtomicLong(0);

    private TokenTracker() {
    }

    /**
     * Creates a new token tracker.
     *
     * @return a new token tracker
     */
    public static TokenTracker create() {
        return new TokenTracker();
    }

    /**
     * Records token usage for an API call.
     *
     * @param inputTokens the number of input tokens
     * @param outputTokens the number of output tokens
     */
    public void record(long inputTokens, long outputTokens) {
        this.inputTokens.addAndGet(inputTokens);
        this.outputTokens.addAndGet(outputTokens);
    }

    /**
     * Gets the total input tokens consumed.
     *
     * @return total input tokens
     */
    public long getTotalInputTokens() {
        return inputTokens.get();
    }

    /**
     * Gets the total output tokens consumed.
     *
     * @return total output tokens
     */
    public long getTotalOutputTokens() {
        return outputTokens.get();
    }

    /**
     * Gets the total tokens consumed (input + output).
     *
     * @return total tokens
     */
    public long getTotalTokens() {
        return inputTokens.get() + outputTokens.get();
    }

    /**
     * Resets the token counts.
     */
    public void reset() {
        inputTokens.set(0);
        outputTokens.set(0);
    }
}

