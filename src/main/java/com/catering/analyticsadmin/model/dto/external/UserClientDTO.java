package com.catering.analyticsadmin.model.dto.external;

import java.util.List;

/**
 * Matches ClientDTO from user-event-service: GET /api/clients
 */
public record UserClientDTO(
        Long id,
        Long authServiceId,
        String firstName,
        String lastName,
        String email,
        String contact,
        String address,
        Integer loyaltyPoints,
        String loyaltyCategoryName,
        List<String> dietaryRestrictions
) {}
