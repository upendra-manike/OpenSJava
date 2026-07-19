package io.github.upendramanike.devguard.autoconfigure;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;

/** Configuration for DevGuard, bound from the {@code devguard.*} namespace. */
@ConfigurationProperties(prefix = "devguard")
public class DevGuardProperties {

    /** Master switch for all DevGuard aspects. */
    private boolean enabled = true;

    private final FeatureFlags featureFlags = new FeatureFlags();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public FeatureFlags getFeatureFlags() {
        return featureFlags;
    }

    /** Backing store for the default {@code @FeatureFlag} provider. */
    public static class FeatureFlags {

        /** Value returned for flags not explicitly configured. */
        private boolean defaultEnabled = false;

        /** Explicit flag values, e.g. {@code devguard.feature-flags.flags.NEW_PAYMENT=true}. */
        private final Map<String, Boolean> flags = new LinkedHashMap<>();

        public boolean isDefaultEnabled() {
            return defaultEnabled;
        }

        public void setDefaultEnabled(boolean defaultEnabled) {
            this.defaultEnabled = defaultEnabled;
        }

        public Map<String, Boolean> getFlags() {
            return flags;
        }
    }
}
