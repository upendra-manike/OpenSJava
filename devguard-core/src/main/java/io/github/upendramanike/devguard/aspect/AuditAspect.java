package io.github.upendramanike.devguard.aspect;

import io.github.upendramanike.devguard.annotations.Audit;
import io.github.upendramanike.devguard.core.AuditEvent;
import io.github.upendramanike.devguard.core.AuditSink;
import io.github.upendramanike.devguard.core.SecurityContextProvider;
import java.time.Instant;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/** Implements {@link Audit}: emits an {@link AuditEvent} for each invocation. */
@Aspect
@Order(Ordered.LOWEST_PRECEDENCE)
public class AuditAspect {

    private final AuditSink auditSink;
    private final SecurityContextProvider securityContextProvider;

    public AuditAspect(AuditSink auditSink, SecurityContextProvider securityContextProvider) {
        this.auditSink = auditSink;
        this.securityContextProvider = securityContextProvider;
    }

    @Around("@annotation(audit)")
    public Object around(ProceedingJoinPoint pjp, Audit audit) throws Throwable {
        long start = System.currentTimeMillis();
        String principal = principalName();
        Object[] args = audit.includeArgs() ? pjp.getArgs() : null;
        try {
            Object result = pjp.proceed();
            auditSink.record(new AuditEvent(
                    audit.action(),
                    principal,
                    Instant.now(),
                    args,
                    audit.includeResult() ? result : null,
                    true,
                    null,
                    System.currentTimeMillis() - start));
            return result;
        } catch (Throwable ex) {
            auditSink.record(new AuditEvent(
                    audit.action(),
                    principal,
                    Instant.now(),
                    args,
                    null,
                    false,
                    ex.toString(),
                    System.currentTimeMillis() - start));
            throw ex;
        }
    }

    private String principalName() {
        try {
            SecurityContextProvider.Principal principal = securityContextProvider.currentPrincipal();
            return principal != null ? principal.name() : "anonymous";
        } catch (RuntimeException e) {
            return "anonymous";
        }
    }
}
