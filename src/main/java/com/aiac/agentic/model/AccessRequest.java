package com.aiac.agentic.model;

import jakarta.validation.constraints.NotBlank;

public class AccessRequest {

    @NotBlank(message = "requestId is required")
    private String requestId;

    @NotBlank(message = "agentId is required")
    private String agentId;

    @NotBlank(message = "agentType is required")
    private String agentType;

    @NotBlank(message = "role is required")
    private String role;

    @NotBlank(message = "resource is required")
    private String resource;

    @NotBlank(message = "action is required")
    private String action;

    @NotBlank(message = "environment is required")
    private String environment;

    @NotBlank(message = "sensitivityLevel is required")
    private String sensitivityLevel;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getAgentType() {
        return agentType;
    }

    public void setAgentType(String agentType) {
        this.agentType = agentType;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getSensitivityLevel() {
        return sensitivityLevel;
    }

    public void setSensitivityLevel(String sensitivityLevel) {
        this.sensitivityLevel = sensitivityLevel;
    }
}
