package com.aiac.agentic.controller;

import com.aiac.agentic.model.AccessLog;
import com.aiac.agentic.model.ApiResponse;
import com.aiac.agentic.service.AuditLogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ApiResponse<List<AccessLog>> getLogs() {
        return ApiResponse.success(auditLogService.getAllLogs(), "Audit logs fetched successfully");
    }
}
