package io.github.upendramanike.testkit;

import java.util.function.Consumer;

/**
 * Fluent builder for creating test data objects with sensible defaults.
 * Reduces boilerplate in test setup and makes tests more readable.
 *
 * <p>Example usage:
 * <pre>{@code
 * User user = TestDataBuilder.of(User.class)
 *     .with(u -> u.setName("John"))
 *     .with(u -> u.setEmail("john@example.com"))
 *     .build();
 * }</pre>
 *
 * @param <T> the type of object being built
 */
public class TestDataBuilder<T> {

    private final T instance;

    private TestDataBuilder(T instance) {
        this.instance = instance;
    }

    /**
     * Creates a new builder for the given class.
     *
     * @param clazz the class to build
     * @param <T> the type
     * @return a new builder instance
     */
    public static <T> TestDataBuilder<T> of(Class<T> clazz) {
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();
            return new TestDataBuilder<>(instance);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create instance of " + clazz.getName(), e);
        }
    }

    /**
     * Applies a customization to the object being built.
     *
     * @param customizer the customization to apply
     * @return this builder for method chaining
     */
    public TestDataBuilder<T> with(Consumer<T> customizer) {
        customizer.accept(instance);
        return this;
    }

    /**
     * Builds the final object.
     *
     * @return the built object
     */
    public T build() {
        return instance;
    }
}

