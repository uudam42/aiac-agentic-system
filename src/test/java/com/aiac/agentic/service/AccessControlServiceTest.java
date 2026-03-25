package com.aiac.agentic.service;

import com.aiac.agentic.config.PolicyProperties;
import com.aiac.agentic.exception.InvalidAccessRequestException;
import com.aiac.agentic.model.AccessRequest;
import com.aiac.agentic.model.AccessResponse;
import com.aiac.agentic.model.Decision;
import com.aiac.agentic.repository.InMemoryAccessLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccessControlServiceTest {

    private final AuditLogService auditLogService = new AuditLogService(new InMemoryAccessLogRepository());

    @Test
    void shouldReturnDecisionAndWriteAuditLogWithLocalProvider() {
        AccessControlService accessControlService = new AccessControlService(
                new LocalPolicyDecisionService(),
                opaService(),
                localProperties(),
                auditLogService
        );

        AccessResponse response = accessControlService.checkAccess(validRequest());

        assertEquals(Decision.ALLOW, response.getDecision());
        assertEquals(1, auditLogService.getAllLogs().size());
    }

    @Test
    void shouldFallbackToLocalWhenOpaFails() {
        AccessControlService accessControlService = new AccessControlService(
                new LocalPolicyDecisionService(),
                opaService(),
                opaProperties(),
                auditLogService
        );

        AccessResponse response = accessControlService.checkAccess(validRequest());

        assertEquals(Decision.ALLOW, response.getDecision());
        assertEquals("Analyst role may read financial_report.", response.getReason());
    }

    @Test
    void shouldRejectUnsupportedEnvironment() {
        AccessRequest request = validRequest();
        request.setEnvironment("staging");

        AccessControlService accessControlService = new AccessControlService(
                new LocalPolicyDecisionService(),
                opaService(),
                localProperties(),
                auditLogService
        );

        assertThrows(InvalidAccessRequestException.class, () -> accessControlService.checkAccess(request));
    }

    @Test
    void shouldRejectUnsupportedSensitivityLevel() {
        AccessRequest request = validRequest();
        request.setSensitivityLevel("critical");

        AccessControlService accessControlService = new AccessControlService(
                new LocalPolicyDecisionService(),
                opaService(),
                localProperties(),
                auditLogService
        );

        assertThrows(InvalidAccessRequestException.class, () -> accessControlService.checkAccess(request));
    }

    private OpaPolicyDecisionService opaService() {
        PolicyProperties properties = new PolicyProperties();
        properties.setProvider("opa");
        properties.getOpa().setUrl("http://localhost:8181/v1/data/aiac/allow");

        return new OpaPolicyDecisionService(new RestTemplateBuilder(), new ObjectMapper(), properties);
    }

    private PolicyProperties localProperties() {
        PolicyProperties properties = new PolicyProperties();
        properties.setProvider("local");
        return properties;
    }

    private PolicyProperties opaProperties() {
        PolicyProperties properties = new PolicyProperties();
        properties.setProvider("opa");
        return properties;
    }

    private AccessRequest validRequest() {
        AccessRequest request = new AccessRequest();
        request.setRequestId("req-100");
        request.setAgentId("agent-007");
        request.setAgentType("assistant");
        request.setRole("analyst");
        request.setResource("financial_report");
        request.setAction("read");
        request.setEnvironment("dev");
        request.setSensitivityLevel("medium");
        return request;
    }
}
