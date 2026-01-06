package io.github.upendramanike.testkit;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Lightweight test slice annotation for testing specific layers without
 * loading the full Spring Boot application context.
 *
 * <p>Reduces test execution time by only loading necessary components.
 *
 * <p>Example usage:
 * <pre>{@code
 * @TestSlice(components = {UserService.class, UserRepository.class})
 * class UserServiceTest {
 *     // Fast, focused tests
 * }
 * }</pre>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
public @interface TestSlice {

    /**
     * The components to include in the test context.
     *
     * @return array of component classes
     */
    Class<?>[] components() default {};
}

