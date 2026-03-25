package com.aiac.agentic.controller;

import com.aiac.agentic.model.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public ApiResponse<Map<String, String>> health() {
        return ApiResponse.success(
                Map.of(
                        "status", "UP",
                        "service", "AIAC-Agentic-System"
                ),
                "Service is running"
        );
    }
}
