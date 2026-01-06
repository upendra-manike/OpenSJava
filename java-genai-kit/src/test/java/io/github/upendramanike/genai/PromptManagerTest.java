package io.github.upendramanike.genai;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PromptManagerTest {

    @Test
    void testRegisterAndGet() {
        PromptManager manager = PromptManager.create();
        manager.register("test-prompt", "1.0", "Hello world");
        
        String content = manager.get("test-prompt", "1.0");
        assertEquals("Hello world", content);
    }

    @Test
    void testGetLatest() {
        PromptManager manager = PromptManager.create();
        manager.register("test-prompt", "1.0", "Version 1");
        manager.register("test-prompt", "2.0", "Version 2");
        
        String latest = manager.getLatest("test-prompt");
        assertEquals("Version 2", latest);
    }
}


