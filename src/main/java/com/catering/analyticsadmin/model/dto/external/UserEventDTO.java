package com.catering.analyticsadmin.model.dto.external;

import java.time.LocalDateTime;

/**
 * Matches EventDTO from user-event-service: GET /api/events
 */
public record UserEventDTO(
        Long id,
        String eventType,
        UserLocationDTO location,
        LocalDateTime date,
        String eventStatus,
        UserClientDTO client,
        Integer guestCount
) {}
