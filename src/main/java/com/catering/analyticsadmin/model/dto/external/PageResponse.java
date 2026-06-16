package com.catering.analyticsadmin.model.dto.external;

import java.util.List;

/**
 * Generic page wrapper matching Spring Data's Page JSON structure.
 * Uses nullable wrapper types because downstream services may return null
 * for pagination metadata fields.
 */
public record PageResponse<T>(
        List<T> content,
        Long totalElements,
        Integer totalPages,
        Integer number,
        Integer size,
        Boolean first,
        Boolean last,
        Boolean empty
) {
    public boolean hasNext() {
        if (last != null) {
            return !last;
        }
        if (number != null && totalPages != null) {
            return number + 1 < totalPages;
        }
        return false; // safety: stop when metadata is missing
    }
}
