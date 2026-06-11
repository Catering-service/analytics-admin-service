package com.catering.analyticsadmin.feign;

import com.catering.analyticsadmin.model.dto.external.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Feign client for finance-service.
 * Uses Eureka service discovery — no hardcoded URLs.
 * Returns PageResponse to handle paginated endpoints from finance-service.
 */
@FeignClient(name = "finance-service")
public interface FinanceServiceClient {

    @GetMapping("/api/admin/invoices")
    PageResponse<FinanceInvoiceDTO> getInvoices(
            @RequestParam("page") int page,
            @RequestParam("size") int size
    );

    @GetMapping("/api/admin/payments")
    PageResponse<FinancePaymentDTO> getPayments(
            @RequestParam("page") int page,
            @RequestParam("size") int size
    );

    @GetMapping("/api/admin/preinvoices")
    PageResponse<FinancePreInvoiceDTO> getPreInvoices(
            @RequestParam("page") int page,
            @RequestParam("size") int size
    );
}
