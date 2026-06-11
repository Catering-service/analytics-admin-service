package com.catering.analyticsadmin.model.dto.external;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Matches MenuDTO from user-event-service: GET /api/menus
 */
public record UserMenuDTO(
        Long id,
        UserEventDTO eventDTO,
        LocalDateTime createdAt,
        List<UserDishDTO> menuDishes
) {}
