package com.catering.analyticsadmin.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "revenue_trend")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RevenueTrend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "month", nullable = false)
    private Integer month;

    @Builder.Default
    @Column(name = "revenue")
    private Double revenue = 0.0;

    @Builder.Default
    @Column(name = "events_count")
    private Integer eventsCount = 0;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}