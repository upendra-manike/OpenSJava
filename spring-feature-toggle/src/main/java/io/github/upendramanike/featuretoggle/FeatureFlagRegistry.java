package io.github.upendramanike.featuretoggle;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry for managing multiple feature flags in a Spring Boot application.
 * Provides centralized flag management and lookup.
 *
 * <p>Example usage:
 * <pre>{@code
 * FeatureFlagRegistry registry = FeatureFlagRegistry.create();
 * registry.register("new-payment-flow", false);
 * registry.register("experimental-ui", true);
 *
 * if (registry.isEnabled("new-payment-flow")) {
 *     // Use new payment flow
 * }
 * }</pre>
 */
public class FeatureFlagRegistry {

    private final Map<String, FeatureFlag> flags = new ConcurrentHashMap<>();
    private final FeatureFlagAudit audit;

    private FeatureFlagRegistry(FeatureFlagAudit audit) {
        this.audit = audit;
    }

    /**
     * Creates a new feature flag registry.
     *
     * @return a new registry instance
     */
    public static FeatureFlagRegistry create() {
        return new FeatureFlagRegistry(FeatureFlagAudit.create());
    }

    /**
     * Creates a new feature flag registry with custom audit logging.
     *
     * @param audit the audit logger
     * @return a new registry instance
     */
    public static FeatureFlagRegistry create(FeatureFlagAudit audit) {
        return new FeatureFlagRegistry(audit);
    }

    /**
     * Registers a new feature flag.
     *
     * @param name the flag name
     * @param enabled the initial enabled state
     * @return the registered flag
     */
    public FeatureFlag register(String name, boolean enabled) {
        FeatureFlag flag = FeatureFlag.create(name, enabled, audit);
        flags.put(name, flag);
        return flag;
    }

    /**
     * Gets a feature flag by name.
     *
     * @param name the flag name
     * @return the flag, or null if not found
     */
    public FeatureFlag get(String name) {
        return flags.get(name);
    }

    /**
     * Checks if a feature flag is enabled.
     *
     * @param name the flag name
     * @return true if enabled, false if disabled or not found
     */
    public boolean isEnabled(String name) {
        FeatureFlag flag = flags.get(name);
        return flag != null && flag.isEnabled();
    }

    /**
     * Enables a feature flag.
     *
     * @param name the flag name
     */
    public void enable(String name) {
        FeatureFlag flag = flags.get(name);
        if (flag != null) {
            flag.enable();
        }
    }

    /**
     * Disables a feature flag (rollback).
     *
     * @param name the flag name
     */
    public void disable(String name) {
        FeatureFlag flag = flags.get(name);
        if (flag != null) {
            flag.disable();
        }
    }
}

