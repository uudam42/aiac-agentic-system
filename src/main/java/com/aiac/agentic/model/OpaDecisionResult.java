package com.aiac.agentic.model;

public class OpaDecisionResult {

    private boolean allow;
    private String reason;

    public OpaDecisionResult() {
    }

    public OpaDecisionResult(boolean allow, String reason) {
        this.allow = allow;
        this.reason = reason;
    }

    public boolean isAllow() {
        return allow;
    }

    public void setAllow(boolean allow) {
        this.allow = allow;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
