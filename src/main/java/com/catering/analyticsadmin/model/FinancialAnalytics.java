package com.catering.analyticsadmin.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "financial_analytics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialAnalytics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "period_type", nullable = false)
    private PeriodType periodType;

    @Column(name = "period_label", nullable = false)
    private String periodLabel;

    @Builder.Default
    @Column(name = "total_revenue")
    private Double totalRevenue = 0.0;

    @Builder.Default
    @Column(name = "total_expenses")
    private Double totalExpenses = 0.0;

    @Builder.Default
    @Column(name = "net_profit")
    private Double netProfit = 0.0;

    @Builder.Default
    @Column(name = "paid_invoices_count")
    private Integer paidInvoicesCount = 0;

    @Builder.Default
    @Column(name = "unpaid_invoices_count")
    private Integer unpaidInvoicesCount = 0;

    @Builder.Default
    @Column(name = "average_event_value")
    private Double averageEventValue = 0.0;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}