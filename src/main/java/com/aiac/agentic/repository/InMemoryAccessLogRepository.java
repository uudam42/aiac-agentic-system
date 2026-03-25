package com.aiac.agentic.repository;

import com.aiac.agentic.model.AccessLog;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class InMemoryAccessLogRepository implements AccessLogRepository {

    private final CopyOnWriteArrayList<AccessLog> logs = new CopyOnWriteArrayList<>();

    @Override
    public void save(AccessLog accessLog) {
        logs.add(accessLog);
    }

    @Override
    public List<AccessLog> findAll() {
        return new ArrayList<>(logs);
    }
}
