package io.github.upendramanike.featuretoggle;

import java.util.function.Supplier;

/**
 * Typed feature flag for controlling feature rollout and A/B testing.
 * Supports runtime toggling, rollback, and audit logging.
 *
 * <p>Example usage:
 * <pre>{@code
 * FeatureFlag newPaymentFlow = FeatureFlag.create("new-payment-flow", false);
 * if (newPaymentFlow.isEnabled()) {
 *     return newPaymentService.process(payment);
 * } else {
 *     return oldPaymentService.process(payment);
 * }
 * }</pre>
 */
public class FeatureFlag {

    private final String name;
    private volatile boolean enabled;
    private final FeatureFlagAudit audit;

    private FeatureFlag(String name, boolean enabled, FeatureFlagAudit audit) {
        this.name = name;
        this.enabled = enabled;
        this.audit = audit;
    }

    /**
     * Creates a new feature flag with the given name and initial state.
     *
     * @param name the flag name
     * @param enabled the initial enabled state
     * @return a new feature flag
     */
    public static FeatureFlag create(String name, boolean enabled) {
        return new FeatureFlag(name, enabled, FeatureFlagAudit.create());
    }

    /**
     * Creates a new feature flag with audit logging.
     *
     * @param name the flag name
     * @param enabled the initial enabled state
     * @param audit the audit logger
     * @return a new feature flag
     */
    public static FeatureFlag create(String name, boolean enabled, FeatureFlagAudit audit) {
        return new FeatureFlag(name, enabled, audit);
    }

    /**
     * Checks if the feature flag is enabled.
     *
     * @return true if enabled
     */
    public boolean isEnabled() {
        boolean current = enabled;
        if (audit != null) {
            audit.logCheck(name, current);
        }
        return current;
    }

    /**
     * Enables the feature flag.
     */
    public void enable() {
        boolean wasEnabled = this.enabled;
        this.enabled = true;
        if (audit != null && !wasEnabled) {
            audit.logToggle(name, false, true);
        }
    }

    /**
     * Disables the feature flag (rollback).
     */
    public void disable() {
        boolean wasEnabled = this.enabled;
        this.enabled = false;
        if (audit != null && wasEnabled) {
            audit.logToggle(name, true, false);
        }
    }

    /**
     * Executes one of two suppliers based on the flag state.
     *
     * @param enabledSupplier supplier to execute when enabled
     * @param disabledSupplier supplier to execute when disabled
     * @param <T> the return type
     * @return the result of the appropriate supplier
     */
    public <T> T whenEnabled(Supplier<T> enabledSupplier, Supplier<T> disabledSupplier) {
        return isEnabled() ? enabledSupplier.get() : disabledSupplier.get();
    }

    /**
     * Gets the flag name.
     *
     * @return the flag name
     */
    public String getName() {
        return name;
    }
}

