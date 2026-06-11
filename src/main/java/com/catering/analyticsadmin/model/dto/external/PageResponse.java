package com.catering.analyticsadmin.model.dto.external;

import java.util.List;

/**
 * Generic page wrapper matching Spring Data's Page JSON structure.
 * Used by Feign clients to deserialize paginated responses.
 */
public record PageResponse<T>(
        List<T> content,
        int totalPages,
        long totalElements,
        int number,
        int size
) {
    public boolean hasNext() {
        return number + 1 < totalPages;
    }
}
