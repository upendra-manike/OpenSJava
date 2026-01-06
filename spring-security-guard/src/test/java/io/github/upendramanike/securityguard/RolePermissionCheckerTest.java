package io.github.upendramanike.securityguard;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RolePermissionCheckerTest {

    @Test
    void shouldCheckRoleWithPrefix() {
        RolePermissionChecker checker = RolePermissionChecker.create();
        Authentication auth = mock(Authentication.class);
        GrantedAuthority authority = mock(GrantedAuthority.class);
        
        @SuppressWarnings("unchecked")
        Collection<GrantedAuthority> authorities = Collections.singletonList(authority);
        doReturn(authorities).when(auth).getAuthorities();
        when(authority.getAuthority()).thenReturn("ROLE_ADMIN");
        
        assertTrue(checker.hasRole(auth, "ADMIN"));
        assertTrue(checker.hasRole(auth, "ROLE_ADMIN"));
    }

    @Test
    void shouldReturnFalseForMissingRole() {
        RolePermissionChecker checker = RolePermissionChecker.create();
        Authentication auth = mock(Authentication.class);
        GrantedAuthority authority = mock(GrantedAuthority.class);
        
        @SuppressWarnings("unchecked")
        Collection<GrantedAuthority> authorities = Collections.singletonList(authority);
        doReturn(authorities).when(auth).getAuthorities();
        when(authority.getAuthority()).thenReturn("ROLE_USER");
        
        assertFalse(checker.hasRole(auth, "ADMIN"));
    }
}

