package io.github.upendramanike.apishield;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequestValidatorTest {

    @Test
    void testValidateNoSqlInjection() throws Exception {
        RequestValidator.validateNoSqlInjection("normal text");
        assertThrows(RequestValidator.ValidationException.class, () -> {
            RequestValidator.validateNoSqlInjection("SELECT * FROM users");
        });
    }

    @Test
    void testValidateLength() throws Exception {
        RequestValidator.validateLength("short", 10);
        assertThrows(RequestValidator.ValidationException.class, () -> {
            RequestValidator.validateLength("this is too long", 5);
        });
    }
}


