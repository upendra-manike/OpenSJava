package io.github.upendramanike.safeconfig;

import java.lang.reflect.Field;

/**
 * Utility for generating {@code toString}-like representations of config objects
 * with fields annotated by {@link Secret} masked out.
 */
public final class SecretMasking {

    private static final String MASK = "********";

    private SecretMasking() {
    }

    /**
     * Create a safe string representation of the given configuration object.
     * <p>
     * Example output: {@code AppConfig{port=8080, apiKey=********}}
     *
     * @param config the configuration object to convert to string
     * @return a string representation with secret fields masked
     */
    public static String safeToString(Object config) {
        if (config == null) {
            return "null";
        }
        Class<?> type = config.getClass();
        StringBuilder sb = new StringBuilder();
        sb.append(type.getSimpleName()).append('{');

        Field[] fields = type.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            if (sb.charAt(sb.length() - 1) != '{') {
                sb.append(", ");
            }

            boolean isSecret = field.isAnnotationPresent(Secret.class);
            boolean accessible = field.canAccess(config);
            if (!accessible) {
                field.setAccessible(true);
            }
            Object value;
            try {
                value = field.get(config);
            } catch (IllegalAccessException e) {
                value = "<inaccessible>";
            } finally {
                if (!accessible) {
                    field.setAccessible(false);
                }
            }

            sb.append(field.getName()).append('=');
            if (isSecret && value != null) {
                sb.append(MASK);
            } else {
                sb.append(String.valueOf(value));
            }
        }

        sb.append('}');
        return sb.toString();
    }
}


