package com.aiac.agentic.repository;

import com.aiac.agentic.model.AccessLog;
import com.aiac.agentic.model.Decision;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {
    List<AccessLog> findByAgentId(String agentId);
    List<AccessLog> findByDecision(Decision decision);
}
