package io.github.upendramanike.devguard.core;

/**
 * Receives {@link AuditEvent}s produced by {@code @Audit}. The default {@link LoggingAuditSink} writes
 * to SLF4J; supply your own to persist events to a database, message broker, or SIEM.
 */
public interface AuditSink {

    void record(AuditEvent event);
}
