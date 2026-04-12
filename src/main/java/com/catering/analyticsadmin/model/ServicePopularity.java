package com.catering.analyticsadmin.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "service_popularity")
public class ServicePopularity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service_name", nullable = false)
    private String serviceName;

    @Column(name = "service_category")
    private String serviceCategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "period_type", nullable = false)
    private PeriodType periodType;

    @Column(name = "period_label", nullable = false)
    private String periodLabel;

    @Column(name = "selection_count")
    private Integer selectionCount = 0;

    @Column(name = "revenue_generated")
    private Double revenueGenerated = 0.0;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public ServicePopularity() {
    }

    public ServicePopularity(String serviceName, String serviceCategory,
                             PeriodType periodType, String periodLabel,
                             Integer selectionCount, Double revenueGenerated,
                             LocalDateTime createdAt) {
        this.serviceName = serviceName;
        this.serviceCategory = serviceCategory;
        this.periodType = periodType;
        this.periodLabel = periodLabel;
        this.selectionCount = selectionCount;
        this.revenueGenerated = revenueGenerated;
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ServicePopularity that = (ServicePopularity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "ServicePopularity{" +
                "id=" + id +
                ", serviceName='" + serviceName + '\'' +
                ", serviceCategory='" + serviceCategory + '\'' +
                ", periodType=" + periodType +
                ", periodLabel='" + periodLabel + '\'' +
                ", selectionCount=" + selectionCount +
                ", revenueGenerated=" + revenueGenerated +
                ", createdAt=" + createdAt +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceCategory() {
        return serviceCategory;
    }

    public void setServiceCategory(String serviceCategory) {
        this.serviceCategory = serviceCategory;
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

    public Integer getSelectionCount() {
        return selectionCount;
    }

    public void setSelectionCount(Integer selectionCount) {
        this.selectionCount = selectionCount;
    }

    public Double getRevenueGenerated() {
        return revenueGenerated;
    }

    public void setRevenueGenerated(Double revenueGenerated) {
        this.revenueGenerated = revenueGenerated;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}