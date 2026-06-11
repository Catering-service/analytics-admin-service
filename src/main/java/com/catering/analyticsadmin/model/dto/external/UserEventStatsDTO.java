package com.catering.analyticsadmin.model.dto.external;

import java.util.List;

/**
 * Matches EventStatsDTO from user-event-service: GET /api/events/stats
 */
public record UserEventStatsDTO(
        long totalEvents,
        List<EventTypeCountDTO> eventsByType
) {
    public record EventTypeCountDTO(
            String type,
            long count
    ) {}
}
