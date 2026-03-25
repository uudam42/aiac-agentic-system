package com.aiac.agentic.service;

import com.aiac.agentic.model.AccessRequest;
import com.aiac.agentic.model.PolicyDecision;

public interface PolicyDecisionService {
    PolicyDecision evaluate(AccessRequest request);
}
