package com.catering.analyticsadmin.model.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FinancialAnalyticsCreateDTO {
    @NotNull(message = "Year is required")
    private Integer year;

    @NotNull(message = "Month is required")
    private Integer month;

    @NotNull(message = "Revenue is required")
    private Double revenue;

    @NotNull(message = "Events count is required")
    private Integer eventsCount;

    public FinancialAnalyticsCreateDTO() {
    }

    public FinancialAnalyticsCreateDTO(Integer year, Integer month, Double revenue, Integer eventsCount) {
        this.year = year;
        this.month = month;
        this.revenue = revenue;
        this.eventsCount = eventsCount;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Double getRevenue() {
        return revenue;
    }

    public void setRevenue(Double revenue) {
        this.revenue = revenue;
    }

    public Integer getEventsCount() {
        return eventsCount;
    }

    public void setEventsCount(Integer eventsCount) {
        this.eventsCount = eventsCount;
    }
}
