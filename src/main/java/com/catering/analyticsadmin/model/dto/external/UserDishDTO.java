package com.catering.analyticsadmin.model.dto.external;

/**
 * Matches DishDTO from user-event-service: GET /api/menus → menuDishes[]
 */
public record UserDishDTO(
        Long id,
        String name,
        String ingredients,
        String allergens,
        Double price,
        String dishType
) {}
