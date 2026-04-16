package com.catering.analyticsadmin.repository;

import com.catering.analyticsadmin.model.entity.FinancialAnalytics;
import com.catering.analyticsadmin.model.enums.PeriodType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FinancialAnalyticsRepository extends JpaRepository<FinancialAnalytics, Long> {
    List<FinancialAnalytics> findByPeriodType(PeriodType periodType);

    List<FinancialAnalytics> findByPeriodLabel(String periodLabel);

    List<FinancialAnalytics> findByPeriodTypeAndPeriodLabel(PeriodType periodType, String periodLabel);
}
