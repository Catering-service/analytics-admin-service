package com.catering.analyticsadmin.model.dto;

import com.catering.analyticsadmin.model.enums.PeriodType;

public class EmployeeAnalyticsResponseDTO {
    private Long id;
    private Long employeeId;
    private String employeeName;
    private PeriodType periodType;
    private String periodLabel;
    private Integer ticketsProcessed;
    private Integer salesCompleted;
    private Double revenueGenerated;
    private Double performanceRating;

    public EmployeeAnalyticsResponseDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public PeriodType getPeriodType() {
        return periodType;
    }

    public void setPeriodType(PeriodType periodType) {
        this.periodType = periodType;
    }

    public String getPeriodLabel() {
        return periodLabel;
    }

    public void setPeriodLabel(String periodLabel) {
        this.periodLabel = periodLabel;
    }

    public Integer getTicketsProcessed() {
        return ticketsProcessed;
    }

    public void setTicketsProcessed(Integer ticketsProcessed) {
        this.ticketsProcessed = ticketsProcessed;
    }

    public Integer getSalesCompleted() {
        return salesCompleted;
    }

    public void setSalesCompleted(Integer salesCompleted) {
        this.salesCompleted = salesCompleted;
    }

    public Double getRevenueGenerated() {
        return revenueGenerated;
    }

    public void setRevenueGenerated(Double revenueGenerated) {
        this.revenueGenerated = revenueGenerated;
    }

    public Double getPerformanceRating() {
        return performanceRating;
    }

    public void setPerformanceRating(Double performanceRating) {
        this.performanceRating = performanceRating;
    }
}
