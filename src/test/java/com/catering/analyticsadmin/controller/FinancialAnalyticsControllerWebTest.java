package com.catering.analyticsadmin.controller;

import com.catering.analyticsadmin.model.dto.FinancialAnalyticsResponseDTO;
import com.catering.analyticsadmin.model.enums.PeriodType;
import com.catering.analyticsadmin.service.FinancialAnalyticsService;
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

@WebMvcTest(FinancialAnalyticsController.class)
class FinancialAnalyticsControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FinancialAnalyticsService financialAnalyticsService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getAllReturnsFinancialAnalyticsList() throws Exception {
        FinancialAnalyticsResponseDTO analytics = new FinancialAnalyticsResponseDTO();
        analytics.setId(1L);
        analytics.setPeriodType(PeriodType.MONTHLY);
        analytics.setPeriodLabel("May 2026");
        analytics.setTotalRevenue(50000.0);
        analytics.setTotalExpenses(30000.0);
        analytics.setNetProfit(20000.0);
        analytics.setPaidInvoicesCount(45);
        analytics.setUnpaidInvoicesCount(5);
        analytics.setAverageEventValue(1111.11);
        analytics.setCreatedAt(LocalDateTime.now());

        when(financialAnalyticsService.getAll()).thenReturn(List.of(analytics));

        mockMvc.perform(get("/api/financial-analytics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].periodLabel").value("May 2026"))
                .andExpect(jsonPath("$[0].totalRevenue").value(50000.0));
    }

    @Test
    void getByIdReturnsFinancialAnalytics() throws Exception {
        FinancialAnalyticsResponseDTO analytics = new FinancialAnalyticsResponseDTO();
        analytics.setId(2L);
        analytics.setPeriodType(PeriodType.WEEKLY);
        analytics.setPeriodLabel("Week 18");
        analytics.setTotalRevenue(12000.0);
        analytics.setTotalExpenses(8000.0);
        analytics.setNetProfit(4000.0);
        analytics.setPaidInvoicesCount(10);
        analytics.setUnpaidInvoicesCount(2);
        analytics.setAverageEventValue(1200.0);

        when(financialAnalyticsService.getById(2L)).thenReturn(analytics);

        mockMvc.perform(get("/api/financial-analytics/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.periodLabel").value("Week 18"))
                .andExpect(jsonPath("$.netProfit").value(4000.0));
    }

    @Test
    void getByPeriodTypeReturnsList() throws Exception {
        FinancialAnalyticsResponseDTO analytics = new FinancialAnalyticsResponseDTO();
        analytics.setId(3L);
        analytics.setPeriodType(PeriodType.MONTHLY);
        analytics.setPeriodLabel("April 2026");
        analytics.setTotalRevenue(48000.0);
        analytics.setTotalExpenses(29000.0);
        analytics.setNetProfit(19000.0);

        when(financialAnalyticsService.getByPeriodType("MONTHLY")).thenReturn(List.of(analytics));

        mockMvc.perform(get("/api/financial-analytics/period-type/MONTHLY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].periodType").value("MONTHLY"));
    }

    @Test
    void getByPeriodLabelReturnsList() throws Exception {
        FinancialAnalyticsResponseDTO analytics = new FinancialAnalyticsResponseDTO();
        analytics.setId(4L);
        analytics.setPeriodType(PeriodType.MONTHLY);
        analytics.setPeriodLabel("2026-03");
        analytics.setTotalRevenue(45000.0);
        analytics.setTotalExpenses(27000.0);
        analytics.setNetProfit(18000.0);

        when(financialAnalyticsService.getByPeriodLabel("2026-03")).thenReturn(List.of(analytics));

        mockMvc.perform(get("/api/financial-analytics/period-label/2026-03"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].periodLabel").value("2026-03"));
    }
}
