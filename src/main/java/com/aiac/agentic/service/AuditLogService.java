package com.aiac.agentic.service;

import com.aiac.agentic.model.AccessLog;
import com.aiac.agentic.model.AccessRequest;
import com.aiac.agentic.model.Decision;
import com.aiac.agentic.repository.AccessLogRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class AuditLogService {

    private final AccessLogRepository accessLogRepository;

    public AuditLogService(AccessLogRepository accessLogRepository) {
        this.accessLogRepository = accessLogRepository;
    }

    public void record(AccessRequest request, Decision decision) {
        AccessLog log = new AccessLog(
                request.getAgentId(),
                request.getRole(),
                request.getResource(),
                request.getAction(),
                decision,
                Instant.now()
        );
        accessLogRepository.save(log);
    }

    public List<AccessLog> getAllLogs() {
        return accessLogRepository.findAll();
    }
}
