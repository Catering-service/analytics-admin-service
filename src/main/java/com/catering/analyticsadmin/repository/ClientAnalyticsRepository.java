package com.catering.analyticsadmin.repository;

import com.catering.analyticsadmin.model.entity.ClientAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientAnalyticsRepository extends JpaRepository<ClientAnalytics, Long> {
    List<ClientAnalytics> findByClientId(Long clientId);
}
