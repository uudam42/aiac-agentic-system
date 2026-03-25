package com.aiac.agentic.controller;

import com.aiac.agentic.model.AccessRequest;
import com.aiac.agentic.model.AccessResponse;
import com.aiac.agentic.model.ApiResponse;
import com.aiac.agentic.service.AccessControlService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/access")
public class AccessController {

    private final AccessControlService accessControlService;

    public AccessController(AccessControlService accessControlService) {
        this.accessControlService = accessControlService;
    }

    @PostMapping("/check")
    public ApiResponse<AccessResponse> checkAccess(@Valid @RequestBody AccessRequest request) {
        AccessResponse response = accessControlService.checkAccess(request);
        return ApiResponse.success(response, "Access decision generated successfully");
    }
}
