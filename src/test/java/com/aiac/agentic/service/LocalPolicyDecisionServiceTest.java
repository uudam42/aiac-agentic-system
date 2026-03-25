package com.aiac.agentic.service;

import com.aiac.agentic.model.AccessRequest;
import com.aiac.agentic.model.Decision;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LocalPolicyDecisionServiceTest {

    private final LocalPolicyDecisionService service = new LocalPolicyDecisionService();

    @Test
    void shouldAllowAdminAccess() {
        AccessRequest request = baseRequest();
        request.setRole("admin");
        request.setResource("secrets");
        request.setAction("delete");

        assertEquals(Decision.ALLOW, service.evaluate(request).getDecision());
    }

    @Test
    void shouldAllowAnalystReadingFinancialReport() {
        AccessRequest request = baseRequest();
        request.setRole("analyst");
        request.setResource("financial_report");
        request.setAction("read");

        assertEquals(Decision.ALLOW, service.evaluate(request).getDecision());
    }

    @Test
    void shouldDenyOtherRequests() {
        AccessRequest request = baseRequest();
        request.setRole("guest");
        request.setResource("financial_report");
        request.setAction("delete");

        assertEquals(Decision.DENY, service.evaluate(request).getDecision());
    }

    private AccessRequest baseRequest() {
        AccessRequest request = new AccessRequest();
        request.setRequestId("req-001");
        request.setAgentId("agent-1");
        request.setAgentType("assistant");
        request.setRole("guest");
        request.setResource("financial_report");
        request.setAction("read");
        request.setEnvironment("dev");
        request.setSensitivityLevel("low");
        return request;
    }
}
