package com.aiac.agentic.model;

public class OpaInput {

    private final String agentId;
    private final String role;
    private final String resource;
    private final String action;
    private final String environment;
    private final String sensitivityLevel;

    public OpaInput(String agentId,
                    String role,
                    String resource,
                    String action,
                    String environment,
                    String sensitivityLevel) {
        this.agentId = agentId;
        this.role = role;
        this.resource = resource;
        this.action = action;
        this.environment = environment;
        this.sensitivityLevel = sensitivityLevel;
    }

    public String getAgentId() {
        return agentId;
    }

    public String getRole() {
        return role;
    }

    public String getResource() {
        return resource;
    }

    public String getAction() {
        return action;
    }

    public String getEnvironment() {
        return environment;
    }

    public String getSensitivityLevel() {
        return sensitivityLevel;
    }
}
