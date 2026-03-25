package com.aiac.agentic.service;

import com.aiac.agentic.config.PolicyProperties;
import com.aiac.agentic.model.AccessRequest;
import com.aiac.agentic.model.Decision;
import com.aiac.agentic.model.OpaDecisionResult;
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
        OpaRequestPayload payload = new OpaRequestPayload(request);
        log.debug("Prepared OPA input payload: {}", toJson(payload));

        if (policyProperties.getOpa().isMockEnabled()) {
            log.info("Using mocked OPA response. No live OPA server is required.");
            return fromOpaResult(mockDecision(request));
        }

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

            return fromOpaResult(body.getResult());
        } catch (RestClientException ex) {
            throw new IllegalStateException("OPA policy evaluation failed: " + ex.getMessage(), ex);
        }
    }

    private OpaDecisionResult mockDecision(AccessRequest request) {
        String role = request.getRole().trim().toLowerCase();
        String resource = request.getResource().trim().toLowerCase();
        String action = request.getAction().trim().toLowerCase();

        if ("admin".equals(role)) {
            return new OpaDecisionResult(true, "Mock OPA: admin role is allowed.");
        }

        if ("analyst".equals(role) && "financial_report".equals(resource) && "read".equals(action)) {
            return new OpaDecisionResult(true, "Mock OPA: analyst may read financial_report.");
        }

        return new OpaDecisionResult(false, "Mock OPA: request denied by policy.");
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
