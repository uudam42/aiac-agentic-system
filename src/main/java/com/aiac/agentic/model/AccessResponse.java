package com.aiac.agentic.model;

public class AccessResponse {

    private final String agentId;
    private final Decision decision;
    private final String reason;

    public AccessResponse(String agentId, Decision decision, String reason) {
        this.agentId = agentId;
        this.decision = decision;
        this.reason = reason;
    }

    public String getAgentId() {
        return agentId;
    }

    public Decision getDecision() {
        return decision;
    }

    public String getReason() {
        return reason;
    }
}
