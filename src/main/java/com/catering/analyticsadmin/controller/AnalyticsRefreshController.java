package com.catering.analyticsadmin.controller;

import com.catering.analyticsadmin.service.AnalyticsAggregationScheduler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Allows manual triggering of analytics data aggregation
 * without waiting for the daily 2 AM schedule.
 */
@RestController
@RequestMapping("/api/analytics")
public class AnalyticsRefreshController {

    private final AnalyticsAggregationScheduler scheduler;

    public AnalyticsRefreshController(AnalyticsAggregationScheduler scheduler) {
        this.scheduler = scheduler;
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh() {
        // Run async so the HTTP response doesn't wait for all services
        CompletableFuture.runAsync(() -> scheduler.triggerAggregation(true));
        return ResponseEntity.ok(Map.of(
                "status", "ACCEPTED",
                "message", "Analytics refresh triggered. Data will be updated shortly."
        ));
    }
}
