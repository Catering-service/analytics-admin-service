package com.catering.analyticsadmin.controller;

import com.catering.analyticsadmin.model.dto.FinancialAnalyticsResponseDTO;
import com.catering.analyticsadmin.service.FinancialAnalyticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/financial-analytics")
public class FinancialAnalyticsController {
    private final FinancialAnalyticsService financialAnalyticsService;

    public FinancialAnalyticsController(FinancialAnalyticsService financialAnalyticsService) {
        this.financialAnalyticsService = financialAnalyticsService;
    }

    @GetMapping
    public List<FinancialAnalyticsResponseDTO> getAll() {
        return financialAnalyticsService.getAll();
    }

    @GetMapping("/{id}")
    public FinancialAnalyticsResponseDTO getById(@PathVariable Long id) {
        return financialAnalyticsService.getById(id);
    }

    @GetMapping("/period-type/{periodType}")
    public List<FinancialAnalyticsResponseDTO> getByPeriodType(@PathVariable String periodType) {
        return financialAnalyticsService.getByPeriodType(periodType);
    }

    @GetMapping("/period-label/{periodLabel}")
    public List<FinancialAnalyticsResponseDTO> getByPeriodLabel(@PathVariable String periodLabel) {
        return financialAnalyticsService.getByPeriodLabel(periodLabel);
    }



}
