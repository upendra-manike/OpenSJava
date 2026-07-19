package io.github.upendramanike.devguard.core;

import java.util.Set;

/** Default {@link SecurityContextProvider} returning an anonymous principal with no authorities. */
public class NoOpSecurityContextProvider implements SecurityContextProvider {

    private static final Principal ANONYMOUS = new Principal("anonymous", Set.of(), Set.of());

    @Override
    public Principal currentPrincipal() {
        return ANONYMOUS;
    }
}
