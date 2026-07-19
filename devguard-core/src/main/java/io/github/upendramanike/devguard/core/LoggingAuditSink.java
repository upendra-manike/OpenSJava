package io.github.upendramanike.devguard.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** {@link AuditSink} that emits events to SLF4J under the {@code devguard.audit} logger. */
public class LoggingAuditSink implements AuditSink {

    private static final Logger log = LoggerFactory.getLogger("devguard.audit");

    @Override
    public void record(AuditEvent event) {
        if (event.success()) {
            log.info("{}", event);
        } else {
            log.warn("{}", event);
        }
    }
}
