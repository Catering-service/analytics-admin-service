package com.catering.analyticsadmin.model.dto.external;

/**
 * Matches LocationDTO from user-event-service.
 */
public record UserLocationDTO(
        Long id,
        String name,
        String address,
        Integer capacity
) {}
