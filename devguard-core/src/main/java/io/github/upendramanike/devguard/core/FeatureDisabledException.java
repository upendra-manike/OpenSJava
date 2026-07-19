package io.github.upendramanike.devguard.core;

/** Thrown when a disabled feature is invoked under {@code @FeatureFlag(failIfDisabled = true)}. */
public class FeatureDisabledException extends RuntimeException {

    public FeatureDisabledException(String message) {
        super(message);
    }
}
