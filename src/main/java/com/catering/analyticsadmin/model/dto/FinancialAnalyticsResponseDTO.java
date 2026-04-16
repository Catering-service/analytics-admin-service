package com.catering.analyticsadmin.model.dto;

import com.catering.analyticsadmin.model.enums.PeriodType;

import java.time.LocalDateTime;

public class FinancialAnalyticsResponseDTO {

    private Long id;
    private PeriodType periodType;
    private String periodLabel;
    private Double totalRevenue;
    private Double totalExpenses;
    private Double netProfit;
    private Integer paidInvoicesCount;
    private Integer unpaidInvoicesCount;
    private Double averageEventValue;
    private LocalDateTime createdAt;

    public FinancialAnalyticsResponseDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Double getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(Double totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public Double getNetProfit() {
        return netProfit;
    }

    public void setNetProfit(Double netProfit) {
        this.netProfit = netProfit;
    }

    public Integer getPaidInvoicesCount() {
        return paidInvoicesCount;
    }

    public void setPaidInvoicesCount(Integer paidInvoicesCount) {
        this.paidInvoicesCount = paidInvoicesCount;
    }

    public Integer getUnpaidInvoicesCount() {
        return unpaidInvoicesCount;
    }

    public void setUnpaidInvoicesCount(Integer unpaidInvoicesCount) {
        this.unpaidInvoicesCount = unpaidInvoicesCount;
    }

    public Double getAverageEventValue() {
        return averageEventValue;
    }

    public void setAverageEventValue(Double averageEventValue) {
        this.averageEventValue = averageEventValue;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}