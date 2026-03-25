package com.aiac.agentic.service;

import com.aiac.agentic.model.AccessLog;
import com.aiac.agentic.model.AccessRequest;
import com.aiac.agentic.model.Decision;
import com.aiac.agentic.repository.AccessLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class AuditLogService {

    private static final Logger log = LoggerFactory.getLogger(AuditLogService.class);

    private final AccessLogRepository accessLogRepository;

    public AuditLogService(AccessLogRepository accessLogRepository) {
        this.accessLogRepository = accessLogRepository;
    }

    public void record(AccessRequest request, Decision decision, String policyProvider, long decisionLatencyMs) {
        AccessLog accessLog = new AccessLog(
                request.getRequestId(),
                request.getAgentId(),
                request.getRole(),
                request.getResource(),
                request.getAction(),
                decision,
                policyProvider,
                Instant.now(),
                decisionLatencyMs
        );

        AccessLog saved = accessLogRepository.save(accessLog);
        log.info("Persisted audit log to DB: id={}, requestId={}, agentId={}, decision={}, provider={}",
                saved.getId(), saved.getRequestId(), saved.getAgentId(), saved.getDecision(), saved.getPolicyProvider());
    }

    public List<AccessLog> getLogs(String agentId, Decision decision) {
        if (agentId != null && !agentId.isBlank()) {
            return accessLogRepository.findByAgentId(agentId);
        }

        if (decision != null) {
            return accessLogRepository.findByDecision(decision);
        }

        return accessLogRepository.findAll();
    }
}
