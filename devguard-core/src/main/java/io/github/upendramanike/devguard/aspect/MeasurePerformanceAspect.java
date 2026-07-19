package io.github.upendramanike.devguard.aspect;

import io.github.upendramanike.devguard.annotations.MeasurePerformance;
import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/** Implements {@link MeasurePerformance}: reports execution time and heap allocation. */
@Aspect
@Order(Ordered.LOWEST_PRECEDENCE - 6)
public class MeasurePerformanceAspect {

    private static final Logger log = LoggerFactory.getLogger("devguard.performance");

    @Around("execution(* *(..)) && (@annotation(io.github.upendramanike.devguard.annotations.MeasurePerformance)"
            + " || @within(io.github.upendramanike.devguard.annotations.MeasurePerformance))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        MeasurePerformance config = resolve(method);
        if (config == null) {
            return pjp.proceed();
        }
        String name = Aspects.signature(method);
        long usedBefore = config.trackMemory() ? usedHeap() : 0;
        long start = System.nanoTime();
        try {
            return pjp.proceed();
        } finally {
            long millis = (System.nanoTime() - start) / 1_000_000;
            long allocatedKb = config.trackMemory() ? Math.max(0, (usedHeap() - usedBefore) / 1024) : -1;
            boolean slow = config.warnAboveMillis() > 0 && millis > config.warnAboveMillis();
            if (slow) {
                log.warn("[perf] {} took {} ms (heapDelta={} KB) - exceeds {} ms threshold",
                        name, millis, allocatedKb, config.warnAboveMillis());
            } else {
                log.info("[perf] {} took {} ms (heapDelta={} KB)", name, millis, allocatedKb);
            }
        }
    }

    private MeasurePerformance resolve(Method method) {
        MeasurePerformance config = method.getAnnotation(MeasurePerformance.class);
        return config != null ? config : method.getDeclaringClass().getAnnotation(MeasurePerformance.class);
    }

    private long usedHeap() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }
}
