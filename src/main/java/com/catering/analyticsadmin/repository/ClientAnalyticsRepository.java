package com.catering.analyticsadmin.repository;

import com.catering.analyticsadmin.model.ClientAnalytics;
import com.catering.analyticsadmin.model.EmployeeAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientAnalyticsRepository extends JpaRepository<ClientAnalytics, Long> {}
