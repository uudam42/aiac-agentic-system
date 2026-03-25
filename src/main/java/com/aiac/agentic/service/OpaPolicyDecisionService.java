package com.aiac.agentic.service;

import com.aiac.agentic.config.PolicyProperties;
import com.aiac.agentic.model.AccessRequest;
import com.aiac.agentic.model.Decision;
import com.aiac.agentic.model.OpaDecisionResult;
import com.aiac.agentic.model.OpaInput;
import com.aiac.agentic.model.OpaRequestPayload;
import com.aiac.agentic.model.OpaResponsePayload;
import com.aiac.agentic.model.PolicyDecision;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class OpaPolicyDecisionService implements PolicyDecisionService {

    private static final Logger log = LoggerFactory.getLogger(OpaPolicyDecisionService.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final PolicyProperties policyProperties;

    public OpaPolicyDecisionService(RestTemplateBuilder restTemplateBuilder,
                                    ObjectMapper objectMapper,
                                    PolicyProperties policyProperties) {
        this.restTemplate = restTemplateBuilder.build();
        this.objectMapper = objectMapper;
        this.policyProperties = policyProperties;
    }

    @Override
    public PolicyDecision evaluate(AccessRequest request) {
        OpaRequestPayload payload = new OpaRequestPayload(toOpaInput(request));
        log.info("Sending policy request to OPA endpoint: {}", policyProperties.getOpa().getUrl());
        log.debug("OPA request payload: {}", toJson(payload));

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<OpaRequestPayload> entity = new HttpEntity<>(payload, headers);

            ResponseEntity<OpaResponsePayload> response = restTemplate.postForEntity(
                    policyProperties.getOpa().getUrl(),
                    entity,
                    OpaResponsePayload.class
            );

            OpaResponsePayload body = response.getBody();
            if (body == null || body.getResult() == null) {
                throw new RestClientException("OPA response body is empty or missing result");
            }

            OpaDecisionResult decisionResult = toDecisionResult(body.getResult());
            log.info("Received OPA decision: {}", decisionResult.isAllow() ? "ALLOW" : "DENY");
            return fromOpaResult(decisionResult);
        } catch (RestClientException ex) {
            throw new IllegalStateException("OPA policy evaluation failed: " + ex.getMessage(), ex);
        }
    }

    private OpaInput toOpaInput(AccessRequest request) {
        return new OpaInput(
                request.getAgentId(),
                request.getRole(),
                request.getResource(),
                request.getAction(),
                request.getEnvironment(),
                request.getSensitivityLevel()
        );
    }

    private OpaDecisionResult toDecisionResult(Boolean allow) {
        if (Boolean.TRUE.equals(allow)) {
            return new OpaDecisionResult(true, "Decision returned by OPA policy.");
        }
        return new OpaDecisionResult(false, "Decision denied by OPA policy.");
    }

    private PolicyDecision fromOpaResult(OpaDecisionResult result) {
        return new PolicyDecision(result.isAllow() ? Decision.ALLOW : Decision.DENY, result.getReason());
    }

    private String toJson(OpaRequestPayload payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException ex) {
            return "<unserializable-opa-payload>";
        }
    }
}
