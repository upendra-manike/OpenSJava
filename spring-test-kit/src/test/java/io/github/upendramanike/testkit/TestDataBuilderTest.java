package io.github.upendramanike.testkit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestDataBuilderTest {

    static class User {
        private String name;
        private String email;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    @Test
    void shouldBuildObjectWithCustomizations() {
        User user = TestDataBuilder.of(User.class)
            .with(u -> u.setName("John"))
            .with(u -> u.setEmail("john@example.com"))
            .build();

        assertEquals("John", user.getName());
        assertEquals("john@example.com", user.getEmail());
    }

    @Test
    void shouldBuildObjectWithDefaults() {
        User user = TestDataBuilder.of(User.class).build();
        assertNotNull(user);
    }
}

