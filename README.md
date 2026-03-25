# AIAC-Agentic System

AIAC-Agentic System is a policy-driven access control backend for AI agents. It is designed as a security engineering project rather than a generic CRUD application, with a focus on **Zero Trust**, **Policy-as-Code**, **AI Governance**, and **Auditability**.

## Project Goal

The goal is to build a backend system that can evaluate an AI agent's access request **before** the agent performs a resource access, tool call, sensitive action, or task-chain step. The platform should be able to:

- identify the requesting agent
- analyze request context
- evaluate policy
- allow or deny execution
- record an audit trail for every decision

This repository currently implements **Phase 2-ready groundwork on top of the Phase 1 Spring Boot MVP**.

## Current Stage

### Current Backend State

The backend now delivers the Phase 1 minimal decision loop while also preparing the codebase for Phase 2 policy abstraction:

1. receive an access request
2. validate core request fields and contextual inputs
3. delegate the decision to a policy interface
4. return `ALLOW` or `DENY`
5. write an audit log entry
6. expose the log history through an API

The implementation intentionally avoids early complexity such as databases, authentication frameworks, message queues, and frontend systems.

## Tech Stack

- Java 17
- Spring Boot 3
- Maven
- RESTful API
- JSON
- In-memory repository for audit logging

## High-Level Architecture

Current architecture:

`Client -> Controller -> AccessControlService -> (LocalPolicyDecisionService | OpaPolicyDecisionService) -> AuditLogService -> Audit Log Repository`

Planned evolution:

- **Phase 2:** pluggable policy decision service + richer request context
- **Phase 3:** OPA/Rego integration
- **Phase 4:** database-backed audit persistence
- **Phase 5:** Go enforcement gateway
- **Phase 6:** React dashboard

## Package Structure

```text
src/main/java/com/aiac/agentic/
├── AiacApplication.java
├── controller/
├── service/
├── model/
├── repository/
├── config/
└── exception/
```

## API Endpoints

### 1) Health Check

**GET** `/api/health`

Response example:

```json
{
  "success": true,
  "data": {
    "status": "UP",
    "service": "AIAC-Agentic-System"
  },
  "message": "Service is running"
}
```

### 2) Access Decision Check

**POST** `/api/access/check`

Request body:

```json
{
  "requestId": "req-001",
  "agentId": "agent-007",
  "agentType": "assistant",
  "role": "analyst",
  "resource": "financial_report",
  "action": "read",
  "environment": "dev",
  "sensitivityLevel": "medium"
}
```

Response example:

```json
{
  "success": true,
  "data": {
    "agentId": "agent-007",
    "decision": "ALLOW",
    "reason": "Analyst role may read financial_report."
  },
  "message": "Access decision generated successfully"
}
```

### 3) Audit Logs

**GET** `/api/logs`

Response example:

```json
{
  "success": true,
  "data": [
    {
      "agentId": "agent-007",
      "role": "analyst",
      "resource": "financial_report",
      "action": "read",
      "decision": "ALLOW",
      "timestamp": "2026-03-25T06:00:00Z"
    }
  ],
  "message": "Audit logs fetched successfully"
}
```

## Policy Provider Structure

The backend now supports two policy provider paths:

- `local` -> uses `LocalPolicyDecisionService`
- `opa` -> uses `OpaPolicyDecisionService`

The currently active provider is selected through configuration:

```properties
policy.provider=local
policy.opa.url=http://localhost:8181/v1/data/aiac/access/allow
policy.opa.mock-enabled=true
```

### Local policy rules

The local rules remain intentionally simple:

- `admin` -> may access any resource/action -> **ALLOW**
- `analyst` + `financial_report` + `read` -> **ALLOW**
- all other cases -> **DENY**

### OPA preparation

`OpaPolicyDecisionService` now:

- builds an OPA-style JSON payload from `AccessRequest`
- prepares an HTTP POST call to the configured OPA endpoint
- returns a mocked OPA decision when `policy.opa.mock-enabled=true`
- falls back to local policy when OPA evaluation fails

This keeps the system runnable even when no OPA server is present.

## Run the Project

### Prerequisites

- Java 17+
- Maven 3.9+

### Start locally

```bash
mvn spring-boot:run
```

The service starts on port **8080**.

### Build

```bash
mvn clean package
```

### Test

```bash
mvn test
```

## curl Examples

### Health

```bash
curl http://localhost:8080/api/health
```

### Allowed request

```bash
curl -X POST http://localhost:8080/api/access/check \
  -H "Content-Type: application/json" \
  -d '{
    "requestId": "req-001",
    "agentId": "agent-007",
    "agentType": "assistant",
    "role": "analyst",
    "resource": "financial_report",
    "action": "read",
    "environment": "dev",
    "sensitivityLevel": "medium"
  }'
```

### Denied request

```bash
curl -X POST http://localhost:8080/api/access/check \
  -H "Content-Type: application/json" \
  -d '{
    "requestId": "req-002",
    "agentId": "guest-001",
    "agentType": "automation-bot",
    "role": "guest",
    "resource": "financial_report",
    "action": "delete",
    "environment": "dev",
    "sensitivityLevel": "high"
  }'
```

### Fetch logs

```bash
curl http://localhost:8080/api/logs
```

## Design Notes

- controllers are thin and only handle HTTP concerns
- decision logic stays in the service layer
- audit logging is automatic on each access decision
- repository is in-memory for MVP simplicity
- response format is unified with `success`, `data`, and `message`
- validation and exception handling are centralized

## Roadmap

### Phase 1
- Spring Boot MVP
- local mock policy rules
- in-memory audit logging
- basic REST APIs

### Phase 2
- richer request context (`requestId`, `agentType`, `environment`, `sensitivityLevel`, `toolName`, `taskType`)
- pluggable policy architecture
- stronger validation and domain-specific error handling
- improved response contracts

### Phase 3
- switch `policy.provider=opa`
- point `policy.opa.url` to a live OPA endpoint
- replace mocked OPA behavior with real HTTP-based policy evaluation
- send access context as OPA `input`
- parse OPA decisions into backend responses
- keep fallback handling when OPA is unavailable

### Phase 4
- persist audit logs with H2 or PostgreSQL
- add filter/query support
- enrich logs with policy version, matched rule, latency, source IP, and timestamps

### Phase 5
- add Go enforcement gateway as the execution and interception layer
- evolve toward `Client -> Go Gateway -> Java Backend -> OPA -> DB`

### Phase 6
- add React admin console
- display access events, ALLOW/DENY distributions, and decision details
- support submitting test access requests visually

## Why This Project Matters

This project is meant to demonstrate that AI systems should not directly execute sensitive actions without a governance layer. Even in MVP form, the backend shows the most important security loop:

**request -> decision -> enforcement basis -> audit trail**

That loop is the foundation for a more complete AI access governance platform.
