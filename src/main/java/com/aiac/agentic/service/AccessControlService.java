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
        PolicyDecision policyDecision = evaluateWithConfiguredProvider(request);
        auditLogService.record(request, policyDecision.getDecision());
        return new AccessResponse(request.getAgentId(), policyDecision.getDecision(), policyDecision.getReason());
    }

    private PolicyDecision evaluateWithConfiguredProvider(AccessRequest request) {
        String provider = policyProperties.getProvider().trim().toLowerCase();

        if ("opa".equals(provider)) {
            log.info("Policy provider selected: opa");
            try {
                return opaPolicyDecisionService.evaluate(request);
            } catch (RuntimeException ex) {
                log.warn("OPA policy evaluation failed. Falling back to local policy. Reason: {}", ex.getMessage());
                return localPolicyDecisionService.evaluate(request);
            }
        }

        log.info("Policy provider selected: local");
        return localPolicyDecisionService.evaluate(request);
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
