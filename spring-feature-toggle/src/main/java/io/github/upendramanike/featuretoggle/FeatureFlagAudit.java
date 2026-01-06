package io.github.upendramanike.featuretoggle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Audit logger for feature flag usage and toggles.
 * Logs all flag checks and state changes for compliance and debugging.
 */
public class FeatureFlagAudit {

    private static final Logger logger = LoggerFactory.getLogger(FeatureFlagAudit.class);

    private FeatureFlagAudit() {
    }

    /**
     * Creates a new feature flag audit logger.
     *
     * @return an audit logger instance
     */
    public static FeatureFlagAudit create() {
        return new FeatureFlagAudit();
    }

    /**
     * Logs a feature flag check.
     *
     * @param flagName the flag name
     * @param enabled the current enabled state
     */
    public void logCheck(String flagName, boolean enabled) {
        logger.debug("Feature flag check: {} = {}", flagName, enabled);
    }

    /**
     * Logs a feature flag toggle.
     *
     * @param flagName the flag name
     * @param fromState the previous state
     * @param toState the new state
     */
    public void logToggle(String flagName, boolean fromState, boolean toState) {
        logger.info("Feature flag toggled: {} from {} to {}", flagName, fromState, toState);
    }
}

