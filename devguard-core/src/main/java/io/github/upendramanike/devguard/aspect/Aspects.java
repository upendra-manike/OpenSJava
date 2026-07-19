package io.github.upendramanike.devguard.aspect;

import java.lang.reflect.Method;

/** Small shared helpers for the DevGuard aspects. */
final class Aspects {

    private Aspects() {}

    /** Stable identifier for a method, used to name breakers/bulkheads and default cache/rate keys. */
    static String signature(Method method) {
        return method.getDeclaringClass().getName() + "#" + method.getName();
    }

    /** Returns a safe default value for the given return type (for skipped invocations). */
    static Object defaultValue(Class<?> returnType) {
        if (!returnType.isPrimitive() || returnType == void.class) {
            return null;
        }
        if (returnType == boolean.class) {
            return false;
        }
        if (returnType == char.class) {
            return '\0';
        }
        if (returnType == byte.class) {
            return (byte) 0;
        }
        if (returnType == short.class) {
            return (short) 0;
        }
        if (returnType == int.class) {
            return 0;
        }
        if (returnType == long.class) {
            return 0L;
        }
        if (returnType == float.class) {
            return 0f;
        }
        if (returnType == double.class) {
            return 0d;
        }
        return null;
    }
}
