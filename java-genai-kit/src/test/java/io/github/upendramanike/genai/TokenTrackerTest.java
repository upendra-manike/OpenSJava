package io.github.upendramanike.genai;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenTrackerTest {

    @Test
    void testTokenTracking() {
        TokenTracker tracker = TokenTracker.create();
        tracker.record(100, 50);
        tracker.record(200, 100);
        
        assertEquals(300, tracker.getTotalInputTokens());
        assertEquals(150, tracker.getTotalOutputTokens());
        assertEquals(450, tracker.getTotalTokens());
    }

    @Test
    void testReset() {
        TokenTracker tracker = TokenTracker.create();
        tracker.record(100, 50);
        tracker.reset();
        
        assertEquals(0, tracker.getTotalTokens());
    }
}

