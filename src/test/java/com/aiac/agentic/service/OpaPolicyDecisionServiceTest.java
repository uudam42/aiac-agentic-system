package com.aiac.agentic.service;

import com.aiac.agentic.config.PolicyProperties;
import com.aiac.agentic.model.AccessRequest;
import com.aiac.agentic.model.Decision;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OpaPolicyDecisionServiceTest {

    @Test
    void shouldReturnMockedAllowDecisionWithoutRunningOpa() {
        PolicyProperties properties = new PolicyProperties();
        properties.setProvider("opa");
        properties.getOpa().setMockEnabled(true);

        OpaPolicyDecisionService service = new OpaPolicyDecisionService(
                new RestTemplateBuilder(),
                new ObjectMapper(),
                properties
        );

        assertEquals(Decision.ALLOW, service.evaluate(validRequest()).getDecision());
    }

    private AccessRequest validRequest() {
        AccessRequest request = new AccessRequest();
        request.setRequestId("req-300");
        request.setAgentId("agent-opa");
        request.setAgentType("assistant");
        request.setRole("analyst");
        request.setResource("financial_report");
        request.setAction("read");
        request.setEnvironment("dev");
        request.setSensitivityLevel("medium");
        return request;
    }
}
