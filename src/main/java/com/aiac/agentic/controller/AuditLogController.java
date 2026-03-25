package com.aiac.agentic.controller;

import com.aiac.agentic.model.AccessLog;
import com.aiac.agentic.model.ApiResponse;
import com.aiac.agentic.model.Decision;
import com.aiac.agentic.service.AuditLogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping("/logs")
    public ApiResponse<List<AccessLog>> getLogs(@RequestParam(required = false) String agentId,
                                                @RequestParam(required = false) Decision decision) {
        return ApiResponse.success(
                auditLogService.getLogs(agentId, decision),
                "Audit logs fetched successfully"
        );
    }
}
