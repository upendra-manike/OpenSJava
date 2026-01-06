package io.github.upendramanike.featuretoggle;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FeatureFlagTest {

    @Test
    void shouldBeDisabledByDefault() {
        FeatureFlag flag = FeatureFlag.create("test-flag", false);
        assertFalse(flag.isEnabled());
    }

    @Test
    void shouldBeEnabledWhenCreatedAsEnabled() {
        FeatureFlag flag = FeatureFlag.create("test-flag", true);
        assertTrue(flag.isEnabled());
    }

    @Test
    void shouldToggleEnabledState() {
        FeatureFlag flag = FeatureFlag.create("test-flag", false);
        flag.enable();
        assertTrue(flag.isEnabled());
        
        flag.disable();
        assertFalse(flag.isEnabled());
    }

    @Test
    void shouldExecuteCorrectSupplier() {
        FeatureFlag flag = FeatureFlag.create("test-flag", true);
        
        String result = flag.whenEnabled(
            () -> "enabled",
            () -> "disabled"
        );
        
        assertEquals("enabled", result);
    }
}

