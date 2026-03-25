package com.aiac.agentic.service;

import com.aiac.agentic.config.PolicyProperties;
import com.aiac.agentic.exception.InvalidAccessRequestException;
import com.aiac.agentic.model.AccessRequest;
import com.aiac.agentic.model.AccessResponse;
import com.aiac.agentic.model.PolicyDecision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AccessControlService {

    private static final Logger log = LoggerFactory.getLogger(AccessControlService.class);

    private final LocalPolicyDecisionService localPolicyDecisionService;
    private final OpaPolicyDecisionService opaPolicyDecisionService;
    private final PolicyProperties policyProperties;
    private final AuditLogService auditLogService;

    public AccessControlService(LocalPolicyDecisionService localPolicyDecisionService,
                                OpaPolicyDecisionService opaPolicyDecisionService,
                                PolicyProperties policyProperties,
                                AuditLogService auditLogService) {
        this.localPolicyDecisionService = localPolicyDecisionService;
        this.opaPolicyDecisionService = opaPolicyDecisionService;
        this.policyProperties = policyProperties;
        this.auditLogService = auditLogService;
    }

    public AccessResponse checkAccess(AccessRequest request) {
        validateBusinessInputs(request);
        long startTime = System.currentTimeMillis();
        String providerUsed = "local";
        PolicyDecision policyDecision;

        String configuredProvider = policyProperties.getProvider().trim().toLowerCase();
        if ("opa".equals(configuredProvider)) {
            log.info("Policy provider selected: opa");
            try {
                policyDecision = opaPolicyDecisionService.evaluate(request);
                providerUsed = "opa";
            } catch (RuntimeException ex) {
                log.warn("OPA policy evaluation failed. Falling back to local policy. Reason: {}", ex.getMessage());
                policyDecision = localPolicyDecisionService.evaluate(request);
                providerUsed = "local";
            }
        } else {
            log.info("Policy provider selected: local");
            policyDecision = localPolicyDecisionService.evaluate(request);
        }

        long decisionLatencyMs = System.currentTimeMillis() - startTime;
        auditLogService.record(request, policyDecision.getDecision(), providerUsed, decisionLatencyMs);
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
