package io.github.upendramanike.safeconfig;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

final class ConfigBinderTest {

    @AfterEach
    void clearSystemProperties() {
        System.clearProperty("APP_PORT");
        System.clearProperty("APP_API_KEY");
    }

    static final class AppConfig {
        @ConfigProperty("APP_PORT")
        @Required
        int port;

        @ConfigProperty("APP_API_KEY")
        @Secret
        String apiKey;
    }

    @Test
    void bindsRequiredAndSecretFields() {
        System.setProperty("APP_PORT", "8080");
        System.setProperty("APP_API_KEY", "super-secret");

        ConfigBinder binder = new ConfigBinder(new EnvironmentConfigSource());
        AppConfig cfg = binder.bind(AppConfig.class);

        assertEquals(8080, cfg.port);
        assertEquals("super-secret", cfg.apiKey);

        String safe = SecretMasking.safeToString(cfg);
        // secret value should be masked
        assertEquals("AppConfig{port=8080, apiKey=********}", safe);
    }

    @Test
    void failsOnMissingRequiredField() {
        // Only provide api key, no port
        System.setProperty("APP_API_KEY", "super-secret");

        ConfigBinder binder = new ConfigBinder(new EnvironmentConfigSource());
        assertThrows(ConfigBindingException.class, () -> binder.bind(AppConfig.class));
    }
}


