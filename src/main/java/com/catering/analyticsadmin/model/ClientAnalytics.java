package com.catering.analyticsadmin.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "client_analytics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @Builder.Default
    @Column(name = "events_count")
    private Integer eventsCount = 0;

    @Builder.Default
    @Column(name = "total_spent")
    private Double totalSpent = 0.0;

    @Builder.Default
    @Column(name = "is_active")
    private Boolean isActive = false;

    @Builder.Default
    @Column(name = "is_returning")
    private Boolean isReturning = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}