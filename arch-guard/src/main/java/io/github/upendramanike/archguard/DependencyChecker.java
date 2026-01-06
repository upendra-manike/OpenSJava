package io.github.upendramanike.archguard;

import java.util.HashSet;
import java.util.Set;

/**
 * Checks for dependency violations in code architecture.
 */
public final class DependencyChecker {

    private final Set<String> allowedDependencies = new HashSet<>();
    private final Set<String> forbiddenDependencies = new HashSet<>();

    private DependencyChecker() {
    }

    /**
     * Creates a new dependency checker.
     *
     * @return a new checker
     */
    public static DependencyChecker create() {
        return new DependencyChecker();
    }

    /**
     * Adds an allowed dependency pattern.
     *
     * @param pattern the package pattern (e.g., "com.example.api.*")
     * @return this checker
     */
    public DependencyChecker allow(String pattern) {
        allowedDependencies.add(pattern);
        return this;
    }

    /**
     * Adds a forbidden dependency pattern.
     *
     * @param pattern the package pattern (e.g., "com.example.internal.*")
     * @return this checker
     */
    public DependencyChecker forbid(String pattern) {
        forbiddenDependencies.add(pattern);
        return this;
    }

    /**
     * Checks if a dependency is allowed.
     *
     * @param dependency the dependency to check (e.g., "com.example.api.Client")
     * @return true if allowed, false otherwise
     */
    public boolean isAllowed(String dependency) {
        // Check forbidden first
        for (String forbidden : forbiddenDependencies) {
            if (matches(dependency, forbidden)) {
                return false;
            }
        }

        // If no allowed patterns, everything is allowed
        if (allowedDependencies.isEmpty()) {
            return true;
        }

        // Check if matches any allowed pattern
        for (String allowed : allowedDependencies) {
            if (matches(dependency, allowed)) {
                return true;
            }
        }

        return false;
    }

    private boolean matches(String dependency, String pattern) {
        if (pattern.endsWith(".*")) {
            String prefix = pattern.substring(0, pattern.length() - 2);
            return dependency.startsWith(prefix);
        }
        return dependency.equals(pattern);
    }

    /**
     * Validates a dependency and throws an exception if forbidden.
     *
     * @param dependency the dependency to validate
     * @throws DependencyViolationException if dependency is not allowed
     */
    public void validate(String dependency) throws DependencyViolationException {
        if (!isAllowed(dependency)) {
            throw new DependencyViolationException(
                    "Dependency violation: " + dependency + " is not allowed");
        }
    }

    /**
     * Exception thrown when a dependency violation is detected.
     */
    public static class DependencyViolationException extends RuntimeException {
        public DependencyViolationException(String message) {
            super(message);
        }
    }
}

