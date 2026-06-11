package com.catering.analyticsadmin.model.dto.external;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Matches TicketDTO from user-event-service: GET /api/tickets
 */
public record UserTicketDTO(
        Long id,
        UserEventDTO event,
        String ticketStatus,
        String description,
        LocalDateTime createdAt,
        List<UserEmployeeDTO> employees
) {}
