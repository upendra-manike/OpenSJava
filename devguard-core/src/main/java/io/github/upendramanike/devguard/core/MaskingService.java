package io.github.upendramanike.devguard.core;

import io.github.upendramanike.devguard.annotations.Mask;
import io.github.upendramanike.devguard.annotations.MaskType;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/** Masks sensitive values, either a single value or every {@link Mask}-annotated field of an object. */
public class MaskingService {

    /** Returns a map of {@code fieldName -> value} with {@link Mask}-annotated fields obfuscated. */
    public Map<String, Object> maskFields(Object target) {
        Map<String, Object> result = new LinkedHashMap<>();
        if (target == null) {
            return result;
        }
        for (Field field : target.getClass().getDeclaredFields()) {
            if (field.isSynthetic()) {
                continue;
            }
            field.setAccessible(true);
            Object value;
            try {
                value = field.get(target);
            } catch (IllegalAccessException e) {
                continue;
            }
            Mask mask = field.getAnnotation(Mask.class);
            if (mask != null && value != null) {
                result.put(field.getName(), mask(value.toString(), mask.type(), mask.maskChar()));
            } else {
                result.put(field.getName(), value);
            }
        }
        return result;
    }

    /** Masks a single value according to the given strategy. */
    public String mask(String value, MaskType type, char maskChar) {
        if (value == null || value.isEmpty()) {
            return value;
        }
        return switch (type) {
            case EMAIL -> maskEmail(value, maskChar);
            case PHONE -> maskPhone(value, maskChar);
            case CREDIT_CARD, LAST_FOUR -> maskLastFour(value, maskChar);
            case FULL -> repeat(maskChar, value.length());
        };
    }

    private String maskEmail(String value, char maskChar) {
        int at = value.indexOf('@');
        if (at <= 0) {
            return repeat(maskChar, value.length());
        }
        String local = value.substring(0, at);
        String domain = value.substring(at);
        int keep = Math.min(2, local.length());
        return local.substring(0, keep) + repeat(maskChar, Math.max(1, local.length() - keep)) + domain;
    }

    private String maskPhone(String value, char maskChar) {
        if (value.length() <= 4) {
            return repeat(maskChar, value.length());
        }
        String start = value.substring(0, 2);
        String end = value.substring(value.length() - 2);
        return start + repeat(maskChar, value.length() - 4) + end;
    }

    private String maskLastFour(String value, char maskChar) {
        if (value.length() <= 4) {
            return value;
        }
        return repeat(maskChar, value.length() - 4) + value.substring(value.length() - 4);
    }

    private static String repeat(char c, int count) {
        return String.valueOf(c).repeat(Math.max(0, count));
    }
}
