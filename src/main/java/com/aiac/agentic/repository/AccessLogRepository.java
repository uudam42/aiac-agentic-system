package com.aiac.agentic.repository;

import com.aiac.agentic.model.AccessLog;

import java.util.List;

public interface AccessLogRepository {
    void save(AccessLog accessLog);
    List<AccessLog> findAll();
}
