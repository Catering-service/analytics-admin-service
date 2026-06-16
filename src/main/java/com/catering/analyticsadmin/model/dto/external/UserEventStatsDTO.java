package com.catering.analyticsadmin.model.dto.external;

import java.util.List;

/**
 * Matches EventStatsDTO from user-event-service: GET /api/events/stats
 */
public record UserEventStatsDTO(
        Long totalEvents,
        List<EventTypeCountDTO> eventsByType
) {
    public record EventTypeCountDTO(
            String type,
            Long count
    ) {}
}
