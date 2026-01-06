package io.github.upendramanike.securityguard;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utility for runtime validation of roles and permissions.
 * Helps ensure that security rules are correctly applied at runtime.
 *
 * <p>Example usage:
 * <pre>{@code
 * RolePermissionChecker checker = RolePermissionChecker.create();
 * if (!checker.hasRole(authentication, "ADMIN")) {
 *     throw new AccessDeniedException("Admin role required");
 * }
 * }</pre>
 */
public class RolePermissionChecker {

    private RolePermissionChecker() {
    }

    /**
     * Creates a new role permission checker.
     *
     * @return a checker instance
     */
    public static RolePermissionChecker create() {
        return new RolePermissionChecker();
    }

    /**
     * Checks if the authentication has the specified role.
     *
     * @param authentication the authentication object
     * @param role the role to check (with or without ROLE_ prefix)
     * @return true if the role is present
     */
    public boolean hasRole(Authentication authentication, String role) {
        if (authentication == null) {
            return false;
        }
        String normalizedRole = role.startsWith("ROLE_") ? role : "ROLE_" + role;
        return authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(authority -> authority.equals(normalizedRole));
    }

    /**
     * Checks if the authentication has any of the specified roles.
     *
     * @param authentication the authentication object
     * @param roles the roles to check
     * @return true if any role is present
     */
    public boolean hasAnyRole(Authentication authentication, String... roles) {
        for (String role : roles) {
            if (hasRole(authentication, role)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets all roles from the authentication.
     *
     * @param authentication the authentication object
     * @return set of role names (without ROLE_ prefix)
     */
    public Set<String> getRoles(Authentication authentication) {
        if (authentication == null) {
            return Set.of();
        }
        return authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .filter(authority -> authority.startsWith("ROLE_"))
            .map(authority -> authority.substring(5)) // Remove "ROLE_" prefix
            .collect(Collectors.toSet());
    }
}

