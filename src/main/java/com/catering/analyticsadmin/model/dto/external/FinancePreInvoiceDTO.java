package com.catering.analyticsadmin.model.dto.external;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Matches PreInvoiceResponseDTO from finance-service: GET /api/admin/preinvoices
 */
public record FinancePreInvoiceDTO(
        Integer id,
        Integer eventId,
        String preInvoiceNumber,
        LocalDateTime issueDate,
        LocalDateTime validUntil,
        BigDecimal totalPrice,
        String status
) {}
