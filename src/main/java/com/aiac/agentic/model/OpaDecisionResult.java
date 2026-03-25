package com.aiac.agentic.model;

public class OpaDecisionResult {

    private final boolean allow;
    private final String reason;

    public OpaDecisionResult(boolean allow, String reason) {
        this.allow = allow;
        this.reason = reason;
    }

    public boolean isAllow() {
        return allow;
    }

    public String getReason() {
        return reason;
    }
}
