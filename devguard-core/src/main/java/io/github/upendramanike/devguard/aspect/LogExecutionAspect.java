package io.github.upendramanike.devguard.aspect;

import io.github.upendramanike.devguard.annotations.LogExecution;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/** Implements {@link LogExecution}: logs entry, arguments, result, and duration. */
@Aspect
@Order(Ordered.LOWEST_PRECEDENCE - 5)
public class LogExecutionAspect {

    @Around("execution(* *(..)) && (@annotation(io.github.upendramanike.devguard.annotations.LogExecution)"
            + " || @within(io.github.upendramanike.devguard.annotations.LogExecution))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        LogExecution config = resolve(method);
        if (config == null) {
            return pjp.proceed();
        }
        Logger log = LoggerFactory.getLogger(method.getDeclaringClass());
        Level level = parseLevel(config.level());
        String name = method.getName();
        String args = config.logArgs() ? Arrays.deepToString(pjp.getArgs()) : "";

        log.atLevel(level).log("-> {}({})", name, args);
        long start = System.nanoTime();
        try {
            Object result = pjp.proceed();
            long millis = (System.nanoTime() - start) / 1_000_000;
            if (config.logResult()) {
                log.atLevel(level).log("<- {} ({} ms) = {}", name, millis, result);
            } else {
                log.atLevel(level).log("<- {} ({} ms)", name, millis);
            }
            return result;
        } catch (Throwable ex) {
            long millis = (System.nanoTime() - start) / 1_000_000;
            log.warn("<- {} failed after {} ms: {}", name, millis, ex.toString());
            throw ex;
        }
    }

    private LogExecution resolve(Method method) {
        LogExecution config = method.getAnnotation(LogExecution.class);
        return config != null ? config : method.getDeclaringClass().getAnnotation(LogExecution.class);
    }

    private Level parseLevel(String level) {
        try {
            return Level.valueOf(level.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Level.INFO;
        }
    }
}
