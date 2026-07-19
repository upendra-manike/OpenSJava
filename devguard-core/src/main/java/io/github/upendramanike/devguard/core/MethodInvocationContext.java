package io.github.upendramanike.devguard.core;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.core.ParameterNameDiscoverer;

/**
 * Captures a method invocation so SpEL expressions can reference arguments by name ({@code #userId}),
 * or positionally via {@code a0}/{@code p0}, plus {@code args}.
 */
public record MethodInvocationContext(Method method, Object[] arguments, ParameterNameDiscoverer discoverer) {

    /** Builds the map of variables exposed to SpEL evaluation. */
    public Map<String, Object> argumentMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        String[] names = discoverer.getParameterNames(method);
        if (names != null) {
            for (int i = 0; i < names.length && i < arguments.length; i++) {
                map.put(names[i], arguments[i]);
            }
        }
        for (int i = 0; i < arguments.length; i++) {
            map.putIfAbsent("a" + i, arguments[i]);
            map.putIfAbsent("p" + i, arguments[i]);
        }
        map.put("args", arguments);
        return map;
    }

    /** A stable key derived from the argument values, used when no explicit key expression is given. */
    public String argumentDigest() {
        return Arrays.deepToString(arguments);
    }
}
