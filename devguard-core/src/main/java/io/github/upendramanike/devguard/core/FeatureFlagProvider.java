package io.github.upendramanike.devguard.core;

/**
 * Resolves whether a named feature flag is enabled. The default {@link PropertiesFeatureFlagProvider}
 * reads flags from configuration; supply your own to integrate LaunchDarkly, Unleash, a database, etc.
 */
public interface FeatureFlagProvider {

    boolean isEnabled(String flag);
}
