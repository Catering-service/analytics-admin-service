package com.catering.analyticsadmin.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "service_popularity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @Builder.Default
    @Column(name = "selection_count")
    private Integer selectionCount = 0;

    @Builder.Default
    @Column(name = "revenue_generated")
    private Double revenueGenerated = 0.0;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}