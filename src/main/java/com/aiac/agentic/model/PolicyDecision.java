package com.aiac.agentic.model;

public class PolicyDecision {

    private final Decision decision;
    private final String reason;

    public PolicyDecision(Decision decision, String reason) {
        this.decision = decision;
        this.reason = reason;
    }

    public Decision getDecision() {
        return decision;
    }

    public String getReason() {
        return reason;
    }
}
