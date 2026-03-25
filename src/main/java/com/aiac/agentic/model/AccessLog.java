package com.aiac.agentic.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "access_logs")
public class AccessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String requestId;
    private String agentId;
    private String role;
    private String resource;
    private String action;

    @Enumerated(EnumType.STRING)
    private Decision decision;

    private String policyProvider;
    private Instant timestamp;
    private long decisionLatencyMs;

    public AccessLog() {
    }

    public AccessLog(String requestId,
                     String agentId,
                     String role,
                     String resource,
                     String action,
                     Decision decision,
                     String policyProvider,
                     Instant timestamp,
                     long decisionLatencyMs) {
        this.requestId = requestId;
        this.agentId = agentId;
        this.role = role;
        this.resource = resource;
        this.action = action;
        this.decision = decision;
        this.policyProvider = policyProvider;
        this.timestamp = timestamp;
        this.decisionLatencyMs = decisionLatencyMs;
    }

    public Long getId() {
        return id;
    }

    public String getRequestId() {
        return requestId;
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

    public String getPolicyProvider() {
        return policyProvider;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public long getDecisionLatencyMs() {
        return decisionLatencyMs;
    }
}
