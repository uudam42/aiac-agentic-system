package com.aiac.agentic.service;

import com.aiac.agentic.model.AccessRequest;
import com.aiac.agentic.model.Decision;
import com.aiac.agentic.model.PolicyDecision;
import org.springframework.stereotype.Service;

@Service
public class LocalPolicyDecisionService implements PolicyDecisionService {

    @Override
    public PolicyDecision evaluate(AccessRequest request) {
        String role = request.getRole().trim().toLowerCase();
        String resource = request.getResource().trim().toLowerCase();
        String action = request.getAction().trim().toLowerCase();

        if ("admin".equals(role)) {
            return new PolicyDecision(Decision.ALLOW, "Admin role is allowed to access any resource.");
        }

        if ("analyst".equals(role) && "financial_report".equals(resource) && "read".equals(action)) {
            return new PolicyDecision(Decision.ALLOW, "Analyst role may read financial_report.");
        }

        return new PolicyDecision(Decision.DENY, "Access denied by local policy.");
    }
}
