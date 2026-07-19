package io.github.upendramanike.devguard.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link FeatureFlagProvider} backed by an in-memory map (typically populated from configuration).
 * Flags can also be toggled at runtime via {@link #setFlag(String, boolean)}.
 */
public class PropertiesFeatureFlagProvider implements FeatureFlagProvider {

    private final Map<String, Boolean> flags = new ConcurrentHashMap<>();
    private final boolean defaultValue;

    public PropertiesFeatureFlagProvider(Map<String, Boolean> initialFlags, boolean defaultValue) {
        if (initialFlags != null) {
            initialFlags.forEach((k, v) -> flags.put(k, v != null && v));
        }
        this.defaultValue = defaultValue;
    }

    @Override
    public boolean isEnabled(String flag) {
        return flags.getOrDefault(flag, defaultValue);
    }

    /** Enables or disables a flag at runtime. */
    public void setFlag(String flag, boolean enabled) {
        flags.put(flag, enabled);
    }
}
