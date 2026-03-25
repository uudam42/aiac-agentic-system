package com.aiac.agentic.service;

import com.aiac.agentic.config.PolicyProperties;
import com.aiac.agentic.model.AccessRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;

import static org.junit.jupiter.api.Assertions.assertThrows;

class OpaPolicyDecisionServiceTest {

    @Test
    void shouldThrowWhenOpaIsUnavailable() {
        PolicyProperties properties = new PolicyProperties();
        properties.setProvider("opa");
        properties.getOpa().setUrl("http://localhost:8181/v1/data/aiac/allow");

        OpaPolicyDecisionService service = new OpaPolicyDecisionService(
                new RestTemplateBuilder(),
                new ObjectMapper(),
                properties
        );

        assertThrows(IllegalStateException.class, () -> service.evaluate(validRequest()));
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
