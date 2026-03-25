package com.aiac.agentic.model;

import java.time.Instant;

public class AccessLog {

    private final String agentId;
    private final String role;
    private final String resource;
    private final String action;
    private final Decision decision;
    private final Instant timestamp;

    public AccessLog(String agentId, String role, String resource, String action, Decision decision, Instant timestamp) {
        this.agentId = agentId;
        this.role = role;
        this.resource = resource;
        this.action = action;
        this.decision = decision;
        this.timestamp = timestamp;
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

    public Decision getDecision() {
        return decision;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
