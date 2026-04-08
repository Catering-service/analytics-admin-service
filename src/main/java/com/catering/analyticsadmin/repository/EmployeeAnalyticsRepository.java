package com.catering.analyticsadmin.repository;

import com.catering.analyticsadmin.model.EmployeeAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeAnalyticsRepository extends JpaRepository<EmployeeAnalytics, Long> {
}
