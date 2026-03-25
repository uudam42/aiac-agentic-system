package com.aiac.agentic.service;

import com.aiac.agentic.exception.InvalidAccessRequestException;
import com.aiac.agentic.model.AccessRequest;
import com.aiac.agentic.model.AccessResponse;
import com.aiac.agentic.model.Decision;
import com.aiac.agentic.repository.InMemoryAccessLogRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccessControlServiceTest {

    private final AuditLogService auditLogService = new AuditLogService(new InMemoryAccessLogRepository());
    private final AccessControlService accessControlService = new AccessControlService(new LocalPolicyDecisionService(), auditLogService);

    @Test
    void shouldReturnDecisionAndWriteAuditLog() {
        AccessResponse response = accessControlService.checkAccess(validRequest());

        assertEquals(Decision.ALLOW, response.getDecision());
        assertEquals(1, auditLogService.getAllLogs().size());
    }

    @Test
    void shouldRejectUnsupportedEnvironment() {
        AccessRequest request = validRequest();
        request.setEnvironment("staging");

        assertThrows(InvalidAccessRequestException.class, () -> accessControlService.checkAccess(request));
    }

    @Test
    void shouldRejectUnsupportedSensitivityLevel() {
        AccessRequest request = validRequest();
        request.setSensitivityLevel("critical");

        assertThrows(InvalidAccessRequestException.class, () -> accessControlService.checkAccess(request));
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
