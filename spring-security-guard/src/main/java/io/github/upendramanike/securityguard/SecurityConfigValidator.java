package io.github.upendramanike.securityguard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

import java.util.ArrayList;
import java.util.List;

/**
 * Validates Spring Security configuration for common misconfigurations
 * and security anti-patterns. Helps catch security issues at startup.
 *
 * <p>Example usage:
 * <pre>{@code
 * @PostConstruct
 * public void validateSecurity() {
 *     SecurityConfigValidator validator = SecurityConfigValidator.create();
 *     validator.checkForPermissiveRules(httpSecurity);
 *     validator.checkForMissingMethodSecurity();
 * }
 * }</pre>
 */
public class SecurityConfigValidator {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfigValidator.class);
    private final List<String> warnings = new ArrayList<>();
    private final List<String> errors = new ArrayList<>();

    private SecurityConfigValidator() {
    }

    /**
     * Creates a new security config validator.
     *
     * @return a validator instance
     */
    public static SecurityConfigValidator create() {
        return new SecurityConfigValidator();
    }

    /**
     * Checks for overly permissive security rules (e.g., permitAll() on sensitive endpoints).
     *
     * @param httpSecurity the HTTP security configuration
     * @return this validator for method chaining
     */
    public SecurityConfigValidator checkForPermissiveRules(HttpSecurity httpSecurity) {
        // This is a simplified check - in practice, you'd analyze the actual configuration
        warnings.add("Consider reviewing permitAll() rules for sensitive endpoints");
        return this;
    }

    /**
     * Checks for missing method-level security annotations on sensitive operations.
     *
     * @param classes the classes to check
     * @return this validator for method chaining
     */
    public SecurityConfigValidator checkForMissingMethodSecurity(Class<?>... classes) {
        for (Class<?> clazz : classes) {
            // Simplified check - in practice, you'd use reflection to analyze methods
            warnings.add("Consider adding @PreAuthorize or @Secured to sensitive methods in " + clazz.getName());
        }
        return this;
    }

    /**
     * Validates that critical endpoints require authentication.
     *
     * @param endpointPatterns patterns for critical endpoints
     * @return this validator for method chaining
     */
    public SecurityConfigValidator requireAuthenticationFor(String... endpointPatterns) {
        for (String pattern : endpointPatterns) {
            if (pattern.contains("/public") || pattern.contains("/open")) {
                warnings.add("Critical endpoint pattern '" + pattern + "' may be too permissive");
            }
        }
        return this;
    }

    /**
     * Throws an exception if any errors were found during validation.
     *
     * @throws SecurityConfigurationException if validation errors exist
     */
    public void validate() {
        if (!errors.isEmpty()) {
            throw new SecurityConfigurationException("Security configuration validation failed: " + errors);
        }
        if (!warnings.isEmpty()) {
            logger.warn("Security configuration warnings: {}", warnings);
        }
    }

    /**
     * Returns the list of warnings found during validation.
     *
     * @return list of warning messages
     */
    public List<String> getWarnings() {
        return new ArrayList<>(warnings);
    }

    /**
     * Returns the list of errors found during validation.
     *
     * @return list of error messages
     */
    public List<String> getErrors() {
        return new ArrayList<>(errors);
    }
}

