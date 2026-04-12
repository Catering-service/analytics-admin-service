package com.catering.analyticsadmin.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "revenue_trend")
public class RevenueTrend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "month", nullable = false)
    private Integer month;

    @Column(name = "revenue")
    private Double revenue = 0.0;

    @Column(name = "events_count")
    private Integer eventsCount = 0;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public RevenueTrend() {
    }

    public RevenueTrend(Integer year, Integer month, Double revenue, Integer eventsCount, LocalDateTime createdAt) {
        this.year = year;
        this.month = month;
        this.revenue = revenue;
        this.eventsCount = eventsCount;
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RevenueTrend that = (RevenueTrend) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "RevenueTrend{" +
                "id=" + id +
                ", year=" + year +
                ", month=" + month +
                ", revenue=" + revenue +
                ", eventsCount=" + eventsCount +
                ", createdAt=" + createdAt +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}