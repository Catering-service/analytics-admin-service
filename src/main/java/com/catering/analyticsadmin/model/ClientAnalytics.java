package com.catering.analyticsadmin.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "client_analytics")
public class ClientAnalytics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @Column(name = "client_name", nullable = false)
    private String clientName;

    @Enumerated(EnumType.STRING)
    @Column(name = "period_type", nullable = false)
    private PeriodType periodType;

    @Column(name = "period_label", nullable = false)
    private String periodLabel;

    @Column(name = "events_count")
    private Integer eventsCount = 0;

    @Column(name = "total_spent")
    private Double totalSpent = 0.0;

    @Column(name = "is_active")
    private Boolean isActive = false;

    @Column(name = "is_returning")
    private Boolean isReturning = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public ClientAnalytics() {
    }

    public ClientAnalytics(Long clientId, String clientName,
                           PeriodType periodType, String periodLabel,
                           Integer eventsCount, Double totalSpent,
                           Boolean isActive, Boolean isReturning,
                           LocalDateTime createdAt) {
        this.clientId = clientId;
        this.clientName = clientName;
        this.periodType = periodType;
        this.periodLabel = periodLabel;
        this.eventsCount = eventsCount;
        this.totalSpent = totalSpent;
        this.isActive = isActive;
        this.isReturning = isReturning;
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ClientAnalytics that = (ClientAnalytics) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "ClientAnalytics{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", clientName='" + clientName + '\'' +
                ", periodType=" + periodType +
                ", periodLabel='" + periodLabel + '\'' +
                ", eventsCount=" + eventsCount +
                ", totalSpent=" + totalSpent +
                ", isActive=" + isActive +
                ", isReturning=" + isReturning +
                ", createdAt=" + createdAt +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}