package io.github.upendramanike.safeconfig;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Objects;

/**
 * Reflection-based binder that populates a configuration class from a {@link ConfigSource}.
 * <p>
 * Only non-static fields annotated with {@link ConfigProperty} are considered.
 * Primitive fields are supported for {@code int}, {@code long}, {@code boolean} and {@code double}.
 */
public final class ConfigBinder {

    private final ConfigSource source;

    /**
     * Creates a new binder that reads from the given configuration source.
     *
     * @param source the configuration source to read from
     */
    public ConfigBinder(ConfigSource source) {
        this.source = Objects.requireNonNull(source, "source");
    }

    /**
     * Bind a configuration object of the given type.
     *
     * @param <T> the configuration type
     * @param type configuration class with a public no-arg constructor
     * @return a new instance of the configuration class with fields populated from the source
     */
    public <T> T bind(Class<T> type) {
        Objects.requireNonNull(type, "type");
        T instance = newInstance(type);

        for (Field field : type.getDeclaredFields()) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            ConfigProperty property = field.getAnnotation(ConfigProperty.class);
            if (property == null) {
                continue;
            }

            String key = property.value();
            String raw = source.get(key);

            boolean isRequired = field.isAnnotationPresent(Required.class);
            if (raw == null) {
                if (isRequired) {
                    throw new ConfigBindingException("Missing required config key: " + key +
                            " for field " + type.getSimpleName() + "." + field.getName());
                }
                continue;
            }

            Object converted = convert(raw, field.getType(), key);

            boolean accessible = field.canAccess(instance);
            if (!accessible) {
                field.setAccessible(true);
            }
            try {
                field.set(instance, converted);
            } catch (IllegalAccessException e) {
                throw new ConfigBindingException("Failed to set field " + field.getName() +
                        " on " + type.getName(), e);
            } finally {
                if (!accessible) {
                    field.setAccessible(false);
                }
            }
        }

        return instance;
    }

    private static <T> T newInstance(Class<T> type) {
        try {
            Constructor<T> ctor = type.getDeclaredConstructor();
            if (!ctor.canAccess(null)) {
                ctor.setAccessible(true);
            }
            return ctor.newInstance();
        } catch (Exception e) {
            throw new ConfigBindingException("Config class must have an accessible no-arg constructor: " +
                    type.getName(), e);
        }
    }

    private static Object convert(String raw, Class<?> targetType, String key) {
        try {
            if (targetType == String.class) {
                return raw;
            } else if (targetType == int.class || targetType == Integer.class) {
                return Integer.parseInt(raw);
            } else if (targetType == long.class || targetType == Long.class) {
                return Long.parseLong(raw);
            } else if (targetType == boolean.class || targetType == Boolean.class) {
                return Boolean.parseBoolean(raw);
            } else if (targetType == double.class || targetType == Double.class) {
                return Double.parseDouble(raw);
            }
        } catch (Exception e) {
            throw new ConfigBindingException("Failed to convert value for key '" + key +
                    "' to type " + targetType.getSimpleName() + ": '" + raw + "'", e);
        }

        throw new ConfigBindingException(
                "Unsupported field type " + targetType.getName() +
                        " for config key '" + key + "'");
    }
}


