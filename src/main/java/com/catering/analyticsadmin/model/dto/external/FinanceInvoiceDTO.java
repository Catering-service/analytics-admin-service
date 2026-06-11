package com.catering.analyticsadmin.model.dto.external;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Matches InvoiceResponseDTO from finance-service: GET /api/admin/invoices
 */
public record FinanceInvoiceDTO(
        Long id,
        Long eventId,
        String userEmail,
        String eventName,
        String preInvoiceNumber,
        String invoiceNumber,
        LocalDateTime issueDate,
        LocalDateTime paymentDue,
        BigDecimal amount,
        String status,
        LocalDateTime paidAt
) {}
