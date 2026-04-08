package com.catering.analyticsadmin.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "employee_analytics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @Builder.Default
    @Column(name = "tickets_processed")
    private Integer ticketsProcessed = 0;

    @Builder.Default
    @Column(name = "sales_completed")
    private Integer salesCompleted = 0;

    @Builder.Default
    @Column(name = "revenue_generated")
    private Double revenueGenerated = 0.0;

    @Column(name = "performance_rating")
    private Double performanceRating;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}