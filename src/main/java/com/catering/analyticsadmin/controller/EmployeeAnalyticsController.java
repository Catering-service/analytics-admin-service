package com.catering.analyticsadmin.controller;

import com.catering.analyticsadmin.service.EmployeeAnalyticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employee-analytics")
public class EmployeeAnalyticsController {
    private final EmployeeAnalyticsService employeeAnalyticsService;

    public EmployeeAnalyticsController(EmployeeAnalyticsService employeeAnalyticsService) {
        this.employeeAnalyticsService = employeeAnalyticsService;
    }

    @GetMapping
    public Object getAll() {
        return employeeAnalyticsService.getAllEmployeeAnalytics();
    }

    @GetMapping("/{employeeId}")
    public Object getById(@PathVariable Long employeeId) {
        return employeeAnalyticsService.getEmployeeAnalytics(employeeId);
    }

}
