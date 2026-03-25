package com.aiac.agentic.service;

import com.aiac.agentic.exception.InvalidAccessRequestException;
import com.aiac.agentic.model.AccessRequest;
import com.aiac.agentic.model.AccessResponse;
import com.aiac.agentic.model.PolicyDecision;
import org.springframework.stereotype.Service;

@Service
public class AccessControlService {

    private final PolicyDecisionService policyDecisionService;
    private final AuditLogService auditLogService;

    public AccessControlService(PolicyDecisionService policyDecisionService, AuditLogService auditLogService) {
        this.policyDecisionService = policyDecisionService;
        this.auditLogService = auditLogService;
    }

    public AccessResponse checkAccess(AccessRequest request) {
        validateBusinessInputs(request);
        PolicyDecision policyDecision = policyDecisionService.evaluate(request);
        auditLogService.record(request, policyDecision.getDecision());
        return new AccessResponse(request.getAgentId(), policyDecision.getDecision(), policyDecision.getReason());
    }

    private void validateBusinessInputs(AccessRequest request) {
        if (!isSupportedEnvironment(request.getEnvironment())) {
            throw new InvalidAccessRequestException("environment must be one of: dev, test, prod");
        }

        if (!isSupportedSensitivityLevel(request.getSensitivityLevel())) {
            throw new InvalidAccessRequestException("sensitivityLevel must be one of: low, medium, high");
        }
    }

    private boolean isSupportedEnvironment(String environment) {
        String normalized = environment.trim().toLowerCase();
        return "dev".equals(normalized) || "test".equals(normalized) || "prod".equals(normalized);
    }

    private boolean isSupportedSensitivityLevel(String sensitivityLevel) {
        String normalized = sensitivityLevel.trim().toLowerCase();
        return "low".equals(normalized) || "medium".equals(normalized) || "high".equals(normalized);
    }
}
