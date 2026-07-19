package io.github.upendramanike.devguard.aspect;

import io.github.upendramanike.devguard.annotations.Secure;
import io.github.upendramanike.devguard.core.AccessDeniedException;
import io.github.upendramanike.devguard.core.SecurityContextProvider;
import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/** Implements {@link Secure}: authorizes callers by role/permission before invocation. */
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE + 20)
public class SecureAspect {

    private final SecurityContextProvider provider;

    public SecureAspect(SecurityContextProvider provider) {
        this.provider = provider;
    }

    @Around("execution(* *(..)) && (@annotation(io.github.upendramanike.devguard.annotations.Secure)"
            + " || @within(io.github.upendramanike.devguard.annotations.Secure))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        Secure secure = resolve(method);
        if (secure == null) {
            return pjp.proceed();
        }
        if (secure.roles().length == 0 && secure.permissions().length == 0) {
            return pjp.proceed();
        }
        SecurityContextProvider.Principal principal = provider.currentPrincipal();
        boolean allowed = principal != null
                && (principal.hasAnyRole(secure.roles()) || principal.hasAnyPermission(secure.permissions()));
        if (!allowed) {
            throw new AccessDeniedException(
                    "Access denied to " + method.getName() + " for principal "
                            + (principal != null ? principal.name() : "null"));
        }
        return pjp.proceed();
    }

    private Secure resolve(Method method) {
        Secure secure = method.getAnnotation(Secure.class);
        return secure != null ? secure : method.getDeclaringClass().getAnnotation(Secure.class);
    }
}
