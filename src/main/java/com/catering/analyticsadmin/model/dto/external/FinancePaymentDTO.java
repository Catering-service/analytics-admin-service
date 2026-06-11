package com.catering.analyticsadmin.model.dto.external;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Matches PaymentResponseDTO from finance-service: GET /api/admin/payments
 * Mirrors the nested entity structure returned by finance-service.
 */
public record FinancePaymentDTO(
        Integer id,
        PreInvoiceRef preInvoice,
        PaymentTypeRef paymentType,
        String paymentExecutor,
        LocalDateTime date,
        BigDecimal price,
        InvoiceRef invoice
) {
    public record PreInvoiceRef(Integer id, Integer eventId, String preInvoiceNumber) {}
    public record PaymentTypeRef(Integer id, String name) {}
    public record InvoiceRef(Integer id, String invoiceNumber, String status) {}
}
