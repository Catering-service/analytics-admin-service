package com.catering.analyticsadmin.model.entity;

import com.catering.analyticsadmin.model.enums.PeriodType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "client_analytics")
public class ClientAnalytics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Client ID is required")
    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @NotBlank(message = "Client name is required")
    @Column(name = "client_name", nullable = false)
    private String clientName;

    @NotBlank(message = "Period type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "period_type", nullable = false)
    private PeriodType periodType;

    @NotBlank(message = "Period label is required")
    @Column(name = "period_label", nullable = false)
    private String periodLabel;

    @Min(0)
    @Column(name = "events_count")
    private Integer eventsCount = 0;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "total_spent")
    private Double totalSpent = 0.0;

    @NotNull(message = "Active flag must be set")
    @Column(name = "is_active")
    private Boolean isActive = false;

    @Column(name = "is_returning")
    private Boolean isReturning = false;

    @CreationTimestamp
    @Column(name = "created_at",updatable = false)
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