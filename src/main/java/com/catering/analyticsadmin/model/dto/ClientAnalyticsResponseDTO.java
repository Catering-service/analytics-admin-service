package com.catering.analyticsadmin.model.dto;

import com.catering.analyticsadmin.model.enums.PeriodType;

import java.time.Period;

public class ClientAnalyticsResponseDTO {

    private Long id;
    private String clientName;
    private PeriodType periodType;
    private String periodLabel;
    private Integer eventsCount;
    private Double totalSpent;
    private Boolean isActive;
    private Boolean isReturning;

    public ClientAnalyticsResponseDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
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

    public Integer getEventsCount() {
        return eventsCount;
    }

    public void setEventsCount(Integer eventsCount) {
        this.eventsCount = eventsCount;
    }

    public Double getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(Double totalSpent) {
        this.totalSpent = totalSpent;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getReturning() {
        return isReturning;
    }

    public void setReturning(Boolean returning) {
        isReturning = returning;
    }
}
