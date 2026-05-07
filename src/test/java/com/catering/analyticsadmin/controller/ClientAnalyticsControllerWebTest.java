package com.catering.analyticsadmin.controller;

import com.catering.analyticsadmin.model.dto.ClientAnalyticsResponseDTO;
import com.catering.analyticsadmin.model.enums.PeriodType;
import com.catering.analyticsadmin.service.ClientAnalyticsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientAnalyticsController.class)
class ClientAnalyticsControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClientAnalyticsService clientAnalyticsService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getAllReturnsClientAnalyticsList() throws Exception {
        ClientAnalyticsResponseDTO analytics = new ClientAnalyticsResponseDTO();
        analytics.setId(1L);
        analytics.setClientName("Client A");
        analytics.setPeriodType(PeriodType.MONTHLY);
        analytics.setPeriodLabel("2026-05");
        analytics.setEventsCount(15);
        analytics.setTotalSpent(2500.0);
        analytics.setActive(true);
        analytics.setReturning(true);

        when(clientAnalyticsService.getAll()).thenReturn(List.of(analytics));

        mockMvc.perform(get("/api/client-analytics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].clientName").value("Client A"))
                .andExpect(jsonPath("$[0].eventsCount").value(15));
    }

    @Test
    void getByIdReturnsClientAnalytics() throws Exception {
        ClientAnalyticsResponseDTO analytics = new ClientAnalyticsResponseDTO();
        analytics.setId(2L);
        analytics.setClientName("Client B");
        analytics.setPeriodType(PeriodType.WEEKLY);
        analytics.setPeriodLabel("2026-W18");
        analytics.setEventsCount(8);
        analytics.setTotalSpent(1200.0);
        analytics.setActive(true);
        analytics.setReturning(false);

        when(clientAnalyticsService.getById(2L)).thenReturn(analytics);

        mockMvc.perform(get("/api/client-analytics/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientName").value("Client B"))
                .andExpect(jsonPath("$.totalSpent").value(1200.0));
    }

    @Test
    void getByClientIdReturnsList() throws Exception {
        ClientAnalyticsResponseDTO analytics = new ClientAnalyticsResponseDTO();
        analytics.setId(3L);
        analytics.setClientName("Client C");
        analytics.setPeriodType(PeriodType.DAILY);
        analytics.setPeriodLabel("2026-05-07");
        analytics.setEventsCount(5);
        analytics.setTotalSpent(500.0);

        when(clientAnalyticsService.getByClientId(100L)).thenReturn(List.of(analytics));

        mockMvc.perform(get("/api/client-analytics/client/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].clientName").value("Client C"));
    }
}
