package com.catering.analyticsadmin.controller;

import com.catering.analyticsadmin.service.AnalyticsAggregationScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Allows manual triggering of analytics data aggregation
 * without waiting for the daily 2 AM schedule.
 */
@RestController
@RequestMapping("/api/analytics")
public class AnalyticsRefreshController {

    private static final Logger log = LoggerFactory.getLogger(AnalyticsRefreshController.class);

    private final AnalyticsAggregationScheduler scheduler;
    private final AtomicBoolean manualRefreshInProgress = new AtomicBoolean(false);

    public AnalyticsRefreshController(AnalyticsAggregationScheduler scheduler) {
        this.scheduler = scheduler;
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh() {
        if (!manualRefreshInProgress.compareAndSet(false, true)) {
            return ResponseEntity.status(409).body(Map.of(
                    "status", "CONFLICT",
                    "message", "Analytics refresh is already in progress. Please wait."
            ));
        }

        runAsync();

        return ResponseEntity.accepted().body(Map.of(
                "status", "ACCEPTED",
                "message", "Analytics refresh triggered. Data will be updated shortly."
        ));
    }

    @Async
    void runAsync() {
        try {
            scheduler.triggerAggregation(true);
        } catch (Exception e) {
            log.error("Async analytics refresh failed", e);
        } finally {
            manualRefreshInProgress.set(false);
        }
    }
}
