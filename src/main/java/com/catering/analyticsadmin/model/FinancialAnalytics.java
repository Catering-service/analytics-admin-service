package com.catering.analyticsadmin.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "financial_analytics")
public class FinancialAnalytics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "period_type", nullable = false)
    private PeriodType periodType;

    @Column(name = "period_label", nullable = false)
    private String periodLabel;

    @Column(name = "total_revenue")
    private Double totalRevenue = 0.0;

    @Column(name = "total_expenses")
    private Double totalExpenses = 0.0;

    @Column(name = "net_profit")
    private Double netProfit = 0.0;

    @Column(name = "paid_invoices_count")
    private Integer paidInvoicesCount = 0;

    @Column(name = "unpaid_invoices_count")
    private Integer unpaidInvoicesCount = 0;

    @Column(name = "average_event_value")
    private Double averageEventValue = 0.0;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public FinancialAnalytics() {
    }

    public FinancialAnalytics(PeriodType periodType, String periodLabel,
                              Double totalRevenue, Double totalExpenses,
                              Double netProfit, Integer paidInvoicesCount,
                              Integer unpaidInvoicesCount, Double averageEventValue,
                              LocalDateTime createdAt) {
        this.periodType = periodType;
        this.periodLabel = periodLabel;
        this.totalRevenue = totalRevenue;
        this.totalExpenses = totalExpenses;
        this.netProfit = netProfit;
        this.paidInvoicesCount = paidInvoicesCount;
        this.unpaidInvoicesCount = unpaidInvoicesCount;
        this.averageEventValue = averageEventValue;
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FinancialAnalytics that = (FinancialAnalytics) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "FinancialAnalytics{" +
                "id=" + id +
                ", periodType=" + periodType +
                ", periodLabel='" + periodLabel + '\'' +
                ", totalRevenue=" + totalRevenue +
                ", totalExpenses=" + totalExpenses +
                ", netProfit=" + netProfit +
                ", paidInvoicesCount=" + paidInvoicesCount +
                ", unpaidInvoicesCount=" + unpaidInvoicesCount +
                ", averageEventValue=" + averageEventValue +
                ", createdAt=" + createdAt +
                '}';
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