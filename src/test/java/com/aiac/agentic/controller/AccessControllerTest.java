package com.aiac.agentic.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AccessControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnAllowDecisionForSupportedRequest() throws Exception {
        mockMvc.perform(post("/api/access/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  \"requestId\": \"req-200\",
                                  \"agentId\": \"agent-007\",
                                  \"agentType\": \"assistant\",
                                  \"role\": \"analyst\",
                                  \"resource\": \"financial_report\",
                                  \"action\": \"read\",
                                  \"environment\": \"dev\",
                                  \"sensitivityLevel\": \"medium\"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.agentId").value("agent-007"))
                .andExpect(jsonPath("$.data.decision").value("ALLOW"));
    }

    @Test
    void shouldReturnBadRequestForInvalidEnvironment() throws Exception {
        mockMvc.perform(post("/api/access/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  \"requestId\": \"req-201\",
                                  \"agentId\": \"agent-008\",
                                  \"agentType\": \"assistant\",
                                  \"role\": \"analyst\",
                                  \"resource\": \"financial_report\",
                                  \"action\": \"read\",
                                  \"environment\": \"staging\",
                                  \"sensitivityLevel\": \"medium\"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }
}
