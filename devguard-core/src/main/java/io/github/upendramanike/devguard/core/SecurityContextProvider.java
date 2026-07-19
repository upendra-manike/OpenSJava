package io.github.upendramanike.devguard.core;

import java.util.Set;

/**
 * Supplies the current caller's identity, roles, and permissions for {@code @Secure} checks. The
 * default {@link NoOpSecurityContextProvider} returns an anonymous principal; integrate Spring
 * Security (or your own auth) by providing a custom bean.
 */
public interface SecurityContextProvider {

    Principal currentPrincipal();

    /** Immutable snapshot of the calling principal. */
    record Principal(String name, Set<String> roles, Set<String> permissions) {

        public boolean hasAnyRole(String... required) {
            for (String r : required) {
                if (roles != null && roles.contains(r)) {
                    return true;
                }
            }
            return false;
        }

        public boolean hasAnyPermission(String... required) {
            for (String p : required) {
                if (permissions != null && permissions.contains(p)) {
                    return true;
                }
            }
            return false;
        }
    }
}
