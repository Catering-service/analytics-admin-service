package com.catering.analyticsadmin.model.dto.external;

import java.time.LocalDateTime;

/**
 * Matches EmployeeDTO from user-event-service: GET /api/employees
 */
public record UserEmployeeDTO(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        LocalDateTime hireDate
) {}
