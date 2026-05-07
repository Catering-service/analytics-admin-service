package com.catering.analyticsadmin.controller;

import com.catering.analyticsadmin.model.entity.EmployeeAnalytics;
import com.catering.analyticsadmin.model.enums.PeriodType;
import com.catering.analyticsadmin.service.EmployeeAnalyticsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeAnalyticsController.class)
class EmployeeAnalyticsControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeAnalyticsService employeeAnalyticsService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getAllReturnsEmployeeAnalyticsList() throws Exception {
        EmployeeAnalytics analytics = new EmployeeAnalytics(100L, "John Doe", PeriodType.MONTHLY, "2026-05", 20, 8, 4000.0, 4.5, LocalDateTime.now());
        analytics.setId(1L);

        when(employeeAnalyticsService.getAllEmployeeAnalytics()).thenReturn(List.of(analytics));

        mockMvc.perform(get("/api/employee-analytics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].employeeName").value("John Doe"))
                .andExpect(jsonPath("$[0].ticketsProcessed").value(20));
    }

    @Test
    void getByIdReturnsEmployeeAnalytics() throws Exception {
        EmployeeAnalytics analytics = new EmployeeAnalytics(101L, "Jane Smith", PeriodType.WEEKLY, "2026-W18", 12, 5, 2500.0, 4.8, LocalDateTime.now());
        analytics.setId(2L);

        when(employeeAnalyticsService.getEmployeeAnalytics(2L)).thenReturn(analytics);

        mockMvc.perform(get("/api/employee-analytics/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeName").value("Jane Smith"))
                .andExpect(jsonPath("$.salesCompleted").value(5))
                .andExpect(jsonPath("$.revenueGenerated").value(2500.0));
    }
}
