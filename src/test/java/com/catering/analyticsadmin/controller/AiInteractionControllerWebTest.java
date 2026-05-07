package com.catering.analyticsadmin.controller;

import com.catering.analyticsadmin.model.dto.AiInteractionCreateDTO;
import com.catering.analyticsadmin.model.dto.AiInteractionResponseDTO;
import com.catering.analyticsadmin.service.AiInteractionService;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AiInteractionController.class)
class AiInteractionControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AiInteractionService aiInteractionService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getByIdReturnsInteraction() throws Exception {
        AiInteractionResponseDTO response = new AiInteractionResponseDTO();
        response.setId(1L);
        response.setClientId(5L);
        response.setSessionId("session-abc");
        response.setQuestion("What is the balance?");
        response.setAnswer("42");
        response.setTimestamp(LocalDateTime.now());

        when(aiInteractionService.getById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/ai-interactions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessionId").value("session-abc"))
                .andExpect(jsonPath("$.question").value("What is the balance?"));
    }

    @Test
    void createReturnsCreatedResponse() throws Exception {
        AiInteractionCreateDTO request = new AiInteractionCreateDTO();
        request.setClientId(7L);
        request.setSessionId("session-xyz");
        request.setQuestion("How many items?");
        request.setAnswer("10");

        AiInteractionResponseDTO response = new AiInteractionResponseDTO();
        response.setId(8L);
        response.setClientId(7L);
        response.setSessionId("session-xyz");
        response.setQuestion("How many items?");
        response.setAnswer("10");
        response.setTimestamp(LocalDateTime.now());

        when(aiInteractionService.create(any(AiInteractionCreateDTO.class))).thenReturn(response);

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/ai-interactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(8))
                .andExpect(jsonPath("$.sessionId").value("session-xyz"));
    }
}
