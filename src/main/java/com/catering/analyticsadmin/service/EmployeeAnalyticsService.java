package com.catering.analyticsadmin.service;

import com.catering.analyticsadmin.repository.EmployeeAnalyticsRepository;
import com.catering.analyticsadmin.model.entity.EmployeeAnalytics;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
public class EmployeeAnalyticsService {
    private final EmployeeAnalyticsRepository employeeAnalyticsRepository;

    public EmployeeAnalyticsService(EmployeeAnalyticsRepository employeeAnalyticsRepository) {
        this.employeeAnalyticsRepository = employeeAnalyticsRepository;
    }

    public List<EmployeeAnalytics> getAllEmployeeAnalytics() {
        return employeeAnalyticsRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public EmployeeAnalytics getEmployeeAnalytics(Long employeeId) {
        return employeeAnalyticsRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee analytics not found for employee ID: " + employeeId));
    }


    public EmployeeAnalytics mapToResponse(EmployeeAnalytics entity) {
        EmployeeAnalytics dto = new EmployeeAnalytics();
        dto.setEmployeeId(entity.getEmployeeId());
        dto.setEmployeeName(entity.getEmployeeName());
        dto.setPeriodType(entity.getPeriodType());
        dto.setPeriodLabel(entity.getPeriodLabel());
        dto.setTicketsProcessed(entity.getTicketsProcessed());
        dto.setSalesCompleted(entity.getSalesCompleted());
        dto.setRevenueGenerated(entity.getRevenueGenerated());
        dto.setPerformanceRating(entity.getPerformanceRating());
        dto.setEmployeeRole(entity.getEmployeeRole());
        dto.setCompletedOnTimePct(entity.getCompletedOnTimePct());
        return dto;
    }
}
