package io.github.upendramanike.archguard;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DependencyCheckerTest {

    @Test
    void testAllowedDependency() throws Exception {
        DependencyChecker checker = DependencyChecker.create()
                .allow("com.example.api.*");

        assertTrue(checker.isAllowed("com.example.api.Client"));
        checker.validate("com.example.api.Client");
    }

    @Test
    void testForbiddenDependency() {
        DependencyChecker checker = DependencyChecker.create()
                .forbid("com.example.internal.*");

        assertFalse(checker.isAllowed("com.example.internal.Secret"));
        assertThrows(DependencyChecker.DependencyViolationException.class, () -> {
            checker.validate("com.example.internal.Secret");
        });
    }
}

