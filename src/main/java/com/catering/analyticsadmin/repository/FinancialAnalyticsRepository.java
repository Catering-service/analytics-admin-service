package com.catering.analyticsadmin.repository;

import com.catering.analyticsadmin.model.FinancialAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinancialAnalyticsRepository extends JpaRepository<FinancialAnalytics, Long> {
}
