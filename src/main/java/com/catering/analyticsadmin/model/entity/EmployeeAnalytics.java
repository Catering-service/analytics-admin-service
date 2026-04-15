package com.catering.analyticsadmin.model.entity;

import com.catering.analyticsadmin.model.enums.PeriodType;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "employee_analytics")
public class EmployeeAnalytics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "employee_name", nullable = false)
    private String employeeName;

    @Enumerated(EnumType.STRING)
    @Column(name = "period_type", nullable = false)
    private PeriodType periodType;

    @Column(name = "period_label", nullable = false)
    private String periodLabel;

    @Column(name = "tickets_processed")
    private Integer ticketsProcessed = 0;

    @Column(name = "sales_completed")
    private Integer salesCompleted = 0;

    @Column(name = "revenue_generated")
    private Double revenueGenerated = 0.0;

    @Column(name = "performance_rating")
    private Double performanceRating;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public EmployeeAnalytics() {
    }

    public EmployeeAnalytics(Long employeeId, String employeeName,
                             PeriodType periodType, String periodLabel,
                             Integer ticketsProcessed, Integer salesCompleted,
                             Double revenueGenerated, Double performanceRating,
                             LocalDateTime createdAt) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.periodType = periodType;
        this.periodLabel = periodLabel;
        this.ticketsProcessed = ticketsProcessed;
        this.salesCompleted = salesCompleted;
        this.revenueGenerated = revenueGenerated;
        this.performanceRating = performanceRating;
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeAnalytics that = (EmployeeAnalytics) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "EmployeeAnalytics{" +
                "id=" + id +
                ", employeeId=" + employeeId +
                ", employeeName='" + employeeName + '\'' +
                ", periodType=" + periodType +
                ", periodLabel='" + periodLabel + '\'' +
                ", ticketsProcessed=" + ticketsProcessed +
                ", salesCompleted=" + salesCompleted +
                ", revenueGenerated=" + revenueGenerated +
                ", performanceRating=" + performanceRating +
                ", createdAt=" + createdAt +
                '}';
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}