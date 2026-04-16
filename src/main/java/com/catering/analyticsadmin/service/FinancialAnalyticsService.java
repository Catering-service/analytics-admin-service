package com.catering.analyticsadmin.service;

import com.catering.analyticsadmin.model.dto.AdministratorResponseDTO;
import com.catering.analyticsadmin.model.dto.FinancialAnalyticsResponseDTO;
import com.catering.analyticsadmin.model.enums.PeriodType;
import com.catering.analyticsadmin.repository.FinancialAnalyticsRepository;
import com.catering.analyticsadmin.model.entity.FinancialAnalytics;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinancialAnalyticsService {
    private final FinancialAnalyticsRepository financialAnalyticsRepository;


    public FinancialAnalyticsService(FinancialAnalyticsRepository financialAnalyticsRepository) {
        this.financialAnalyticsRepository = financialAnalyticsRepository;
    }

    public List<FinancialAnalyticsResponseDTO> getAll() {
        return financialAnalyticsRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public FinancialAnalyticsResponseDTO getById(Long id) {
        FinancialAnalytics financialAnalytics = financialAnalyticsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Financial analytics not found"));
        return mapToResponse(financialAnalytics);
    }

    public List<FinancialAnalyticsResponseDTO> getByPeriodType(String periodType) {
        return financialAnalyticsRepository.findByPeriodType(PeriodType.valueOf(periodType))
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<FinancialAnalyticsResponseDTO> getByPeriodLabel(String periodLabel) {
        return financialAnalyticsRepository.findByPeriodLabel(periodLabel)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public FinancialAnalyticsResponseDTO create(FinancialAnalyticsResponseDTO request) {
        FinancialAnalytics entity = new FinancialAnalytics();
        entity.setPeriodType(request.getPeriodType());
        entity.setPeriodLabel(request.getPeriodLabel());
        entity.setTotalRevenue(request.getTotalRevenue());
        entity.setTotalExpenses(request.getTotalExpenses());
        entity.setNetProfit(request.getNetProfit());
        entity.setPaidInvoicesCount(request.getPaidInvoicesCount());
        entity.setUnpaidInvoicesCount(request.getUnpaidInvoicesCount());
        entity.setAverageEventValue(request.getAverageEventValue());
        financialAnalyticsRepository.save(entity);
        return mapToResponse(entity);
    }

    private FinancialAnalyticsResponseDTO mapToResponse(FinancialAnalytics entity) {
        FinancialAnalyticsResponseDTO dto = new FinancialAnalyticsResponseDTO();
        dto.setId(entity.getId());
        dto.setPeriodType(entity.getPeriodType());
        dto.setPeriodLabel(entity.getPeriodLabel());
        dto.setTotalRevenue(entity.getTotalRevenue());
        dto.setTotalExpenses(entity.getTotalExpenses());
        dto.setNetProfit(entity.getNetProfit());
        dto.setPaidInvoicesCount(entity.getPaidInvoicesCount());
        dto.setUnpaidInvoicesCount(entity.getUnpaidInvoicesCount());
        dto.setAverageEventValue(entity.getAverageEventValue());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }
}
