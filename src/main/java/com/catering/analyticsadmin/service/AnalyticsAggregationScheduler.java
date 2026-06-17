package com.catering.analyticsadmin.service;

import com.catering.analyticsadmin.feign.FinanceServiceClient;
import com.catering.analyticsadmin.feign.UserEventServiceClient;
import com.catering.analyticsadmin.model.dto.external.*;
import com.catering.analyticsadmin.model.entity.*;
import com.catering.analyticsadmin.model.enums.PeriodType;
import com.catering.analyticsadmin.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * Scheduled service that periodically fetches data from other microservices,
 * aggregates it, and stores the results in analytics-admin's database.
 *
 * Runs daily at 2:00 AM. Each aggregation method is idempotent -
 * skips if data for the current period already exists.
 */
@Service
public class AnalyticsAggregationScheduler {

    private static final Logger log = LoggerFactory.getLogger(AnalyticsAggregationScheduler.class);
    private static final int PAGE_SIZE = 1000;
    private static final int MAX_PAGES = 50; // safety cap: 50,000 records max

    private final UserEventServiceClient userEventClient;
    private final FinanceServiceClient financeClient;
    private final FinancialAnalyticsRepository financialRepo;
    private final EmployeeAnalyticsRepository employeeRepo;
    private final ClientAnalyticsRepository clientRepo;
    private final RevenueTrendRepository revenueRepo;
    private final ServicePopularityRepository servicePopularityRepo;

    public AnalyticsAggregationScheduler(
            UserEventServiceClient userEventClient,
            FinanceServiceClient financeClient,
            FinancialAnalyticsRepository financialRepo,
            EmployeeAnalyticsRepository employeeRepo,
            ClientAnalyticsRepository clientRepo,
            RevenueTrendRepository revenueRepo,
            ServicePopularityRepository servicePopularityRepo) {
        this.userEventClient = userEventClient;
        this.financeClient = financeClient;
        this.financialRepo = financialRepo;
        this.employeeRepo = employeeRepo;
        this.clientRepo = clientRepo;
        this.revenueRepo = revenueRepo;
        this.servicePopularityRepo = servicePopularityRepo;
    }

    private final AtomicBoolean aggregationInProgress = new AtomicBoolean(false);

    // ──────────────────────────────────────────────
    // Scheduling & manual trigger
    // ──────────────────────────────────────────────

    @Scheduled(cron = "0 0 2 * * *") // 2:00 AM every day
    public void runDailyAggregation() {
        if (!aggregationInProgress.compareAndSet(false, true)) {
            log.warn("Scheduled aggregation skipped — previous run still in progress");
            return;
        }
        try {
            triggerAggregation(false);
        } finally {
            aggregationInProgress.set(false);
        }
    }

    /**
     * Public entry point for manual refresh.
     * When forceRefresh=true, deletes current-period records first so they are re-created.
     * @return true if aggregation started, false if already in progress
     */
    public boolean triggerAggregation(boolean forceRefresh) {
        if (forceRefresh) {
            String periodLabel = YearMonth.now().toString();
            log.info("Force refresh: clearing existing analytics data for period {}", periodLabel);
            deleteByPeriod(periodLabel);
        }
        log.info("=== Starting analytics aggregation (force={}) ===", forceRefresh);
        try { aggregateEmployeeAnalytics(); } catch (Exception e) { log.error("Employee analytics aggregation failed", e); }
        try { aggregateFinancialAnalytics(); } catch (Exception e) { log.error("Financial analytics aggregation failed", e); }
        try { aggregateClientAnalytics(); } catch (Exception e) { log.error("Client analytics aggregation failed", e); }
        try { aggregateRevenueTrend(); } catch (Exception e) { log.error("Revenue trend aggregation failed", e); }
        try { aggregateServicePopularity(); } catch (Exception e) { log.error("Service popularity aggregation failed", e); }
        log.info("=== Analytics aggregation complete ===");
        return true;
    }

    private void deleteByPeriod(String periodLabel) {
        YearMonth now = YearMonth.now();
        financialRepo.findByPeriodLabel(periodLabel).forEach(financialRepo::delete);
        employeeRepo.findAll().stream()
                .filter(e -> periodLabel.equals(e.getPeriodLabel()))
                .forEach(employeeRepo::delete);
        clientRepo.findAll().stream()
                .filter(c -> periodLabel.equals(c.getPeriodLabel()))
                .forEach(clientRepo::delete);
        revenueRepo.findAll().stream()
                .filter(r -> r.getYear() == now.getYear() && r.getMonth() == now.getMonthValue())
                .forEach(revenueRepo::delete);
        servicePopularityRepo.findAll().stream()
                .filter(s -> periodLabel.equals(s.getPeriodLabel()))
                .forEach(servicePopularityRepo::delete);
    }

    // ──────────────────────────────────────────────
    // Financial Analytics
    // ──────────────────────────────────────────────

    private void aggregateFinancialAnalytics() {
        YearMonth now = YearMonth.now();
        String periodLabel = now.toString(); // "2026-06"
        if (financialRepo.findByPeriodTypeAndPeriodLabel(PeriodType.MONTHLY, periodLabel).isEmpty()) {
            log.info("Aggregating financial analytics for {}", periodLabel);

            List<FinanceInvoiceDTO> invoices = fetchAllInvoices();
            List<FinancePreInvoiceDTO> preInvoices = fetchAllPreInvoices();
            List<FinancePaymentDTO> payments = fetchAllPayments();

            // Filter invoices to current month
            List<FinanceInvoiceDTO> monthInvoices = invoices.stream()
                    .filter(i -> i.issueDate() != null
                            && YearMonth.from(i.issueDate()).equals(now))
                    .toList();

            // PAID invoices → eventId → amount (for deduplication with card payments)
            Map<Long, Double> paidInvoiceByEvent = monthInvoices.stream()
                    .filter(i -> "PAID".equals(i.status()) && i.eventId() != null)
                    .collect(Collectors.toMap(
                            FinanceInvoiceDTO::eventId,
                            i -> i.amount() != null ? i.amount().doubleValue() : 0,
                            Double::sum));

            double invoiceRevenue = paidInvoiceByEvent.values().stream()
                    .mapToDouble(Double::doubleValue).sum();

            // Card payments: filter to current month, exclude those already covered by a PAID invoice
            double cardRevenue = payments.stream()
                    .filter(p -> p.date() != null
                            && YearMonth.from(p.date()).equals(now))
                    .filter(p -> {
                        // Exclude payments linked to an invoice that is already PAID
                        if (p.invoice() != null && "PAID".equals(p.invoice().status())) {
                            return false;
                        }
                        // Exclude payments for events already covered by a PAID invoice
                        Integer rawId = p.preInvoice() != null ? p.preInvoice().eventId() : null;
                        Long eventId = rawId != null ? Long.valueOf(rawId) : null;
                        return eventId == null || !paidInvoiceByEvent.containsKey(eventId);
                    })
                    .mapToDouble(p -> p.price() != null ? p.price().doubleValue() : 0)
                    .sum();

            double totalRevenue = invoiceRevenue + cardRevenue;

            double totalExpenses = preInvoices.stream()
                    .filter(p -> p.issueDate() != null
                            && YearMonth.from(p.issueDate()).equals(now))
                    .mapToDouble(p -> p.totalPrice() != null ? p.totalPrice().doubleValue() : 0)
                    .sum();

            long paidCount = monthInvoices.stream()
                    .filter(i -> "PAID".equals(i.status())).count();
            long unpaidCount = monthInvoices.stream()
                    .filter(i -> "PENDING".equals(i.status())).count();

            // Count unique paid events (invoice-paid + card-paid, deduplicated)
            long paidEventCount = paidInvoiceByEvent.size() + payments.stream()
                    .filter(p -> p.date() != null
                            && YearMonth.from(p.date()).equals(now))
                    .filter(p -> {
                        Integer rawId = p.preInvoice() != null ? p.preInvoice().eventId() : null;
                        Long eventId = rawId != null ? Long.valueOf(rawId) : null;
                        return eventId != null && !paidInvoiceByEvent.containsKey(eventId)
                                && (p.invoice() == null || !"PAID".equals(p.invoice().status()));
                    })
                    .map(p -> Long.valueOf(p.preInvoice().eventId()))
                    .distinct()
                    .count();

            double avgEventValue = paidEventCount > 0 ? totalRevenue / paidEventCount : 0;
            double netProfit = totalRevenue - totalExpenses;

            // Compute average employee rating for this month
            double averageRating = employeeRepo.findAll().stream()
                    .filter(e -> periodLabel.equals(e.getPeriodLabel()) && e.getPerformanceRating() != null && e.getPerformanceRating() > 0)
                    .mapToDouble(EmployeeAnalytics::getPerformanceRating)
                    .average()
                    .orElse(0.0);

            FinancialAnalytics fa = new FinancialAnalytics(
                    PeriodType.MONTHLY,
                    periodLabel,
                    totalRevenue,
                    totalExpenses,
                    netProfit,
                    (int) paidCount,
                    (int) unpaidCount,
                    avgEventValue,
                    Math.round(averageRating * 10.0) / 10.0,
                    LocalDateTime.now()
            );
            financialRepo.save(fa);
            log.info("Financial analytics saved: invoiceRevenue={}, cardRevenue={}, total={}, expenses={}, profit={}",
                    invoiceRevenue, cardRevenue, totalRevenue, totalExpenses, netProfit);
        }
    }

    // ──────────────────────────────────────────────
    // Employee Analytics
    // ──────────────────────────────────────────────

    private void aggregateEmployeeAnalytics() {
        YearMonth now = YearMonth.now();
        String periodLabel = now.toString();
        // Only aggregate if no records exist for this period
        boolean exists = employeeRepo.findAll().stream()
                .anyMatch(e -> periodLabel.equals(e.getPeriodLabel()));
        if (!exists) {
            log.info("Aggregating employee analytics for {}", periodLabel);

            List<UserEmployeeDTO> employees = safeFetch(userEventClient::getAllEmployees);
            List<UserTicketDTO> tickets = safeFetch(userEventClient::getAllTickets);
            List<FinanceInvoiceDTO> invoices = fetchAllInvoices();

            // Build invoice lookup: eventId → invoice amount
            Map<Long, Double> invoiceByEvent = invoices.stream()
                    .filter(i -> i.eventId() != null && "PAID".equals(i.status()))
                    .collect(Collectors.toMap(
                            FinanceInvoiceDTO::eventId,
                            i -> i.amount() != null ? i.amount().doubleValue() : 0,
                            Double::sum
                    ));

            for (UserEmployeeDTO emp : employees) {
                List<UserTicketDTO> empTickets = tickets.stream()
                        .filter(t -> t.employees() != null
                                && t.employees().stream().anyMatch(e -> e.id().equals(emp.id())))
                        .toList();

                int ticketsProcessed = empTickets.size();

                double revenueGenerated = empTickets.stream()
                        .mapToDouble(t -> {
                            Long eventId = t.event() != null ? t.event().id() : null;
                            return eventId != null ? invoiceByEvent.getOrDefault(eventId, 0.0) : 0;
                        })
                        .sum();

                EmployeeAnalytics ea = new EmployeeAnalytics(
                        emp.id(),
                        emp.firstName() + " " + emp.lastName(),
                        PeriodType.MONTHLY,
                        periodLabel,
                        ticketsProcessed,
                        0, // salesCompleted - no sales tracking yet
                        revenueGenerated,
                        fetchEmployeeRating(emp.id()), // performanceRating from real data
                        calculateOnTimePct(empTickets),
                        LocalDateTime.now()
                );
                employeeRepo.save(ea);
            }
            log.info("Employee analytics saved for {} employees", employees.size());
        }
    }

    // ──────────────────────────────────────────────
    // Client Analytics
    // ──────────────────────────────────────────────

    private void aggregateClientAnalytics() {
        YearMonth now = YearMonth.now();
        String periodLabel = now.toString();
        boolean exists = clientRepo.findAll().stream()
                .anyMatch(c -> periodLabel.equals(c.getPeriodLabel()));
        if (!exists) {
            log.info("Aggregating client analytics for {}", periodLabel);

            List<UserClientDTO> clients = safeFetch(userEventClient::getAllClients);
            List<UserEventDTO> events = safeFetch(userEventClient::getAllEvents);
            List<FinanceInvoiceDTO> invoices = fetchAllInvoices();

            Map<Long, Double> invoiceByEvent = invoices.stream()
                    .filter(i -> i.eventId() != null && "PAID".equals(i.status()))
                    .collect(Collectors.toMap(
                            FinanceInvoiceDTO::eventId,
                            i -> i.amount() != null ? i.amount().doubleValue() : 0,
                            Double::sum
                    ));

            for (UserClientDTO client : clients) {
                List<UserEventDTO> clientEvents = events.stream()
                        .filter(e -> e.client() != null && e.client().id().equals(client.id()))
                        .toList();

                int eventsCount = clientEvents.size();
                double totalSpent = clientEvents.stream()
                        .mapToDouble(e -> invoiceByEvent.getOrDefault(e.id(), 0.0))
                        .sum();

                boolean isActive = eventsCount > 0;
                boolean isReturning = eventsCount > 1;

                ClientAnalytics ca = new ClientAnalytics(
                        client.id(),
                        client.firstName() + " " + client.lastName(),
                        PeriodType.MONTHLY,
                        periodLabel,
                        eventsCount,
                        totalSpent,
                        isActive,
                        isReturning,
                        LocalDateTime.now()
                );
                clientRepo.save(ca);
            }
            log.info("Client analytics saved for {} clients", clients.size());
        }
    }

    // ──────────────────────────────────────────────
    // Revenue Trend
    // ──────────────────────────────────────────────

    private void aggregateRevenueTrend() {
        YearMonth now = YearMonth.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        // Check if this month already has a trend entry
        // Since RevenueTrendRepository has no query methods, we check manually
        boolean exists = revenueRepo.findAll().stream()
                .anyMatch(r -> r.getYear() == year && r.getMonth() == month);
        if (!exists) {
            log.info("Aggregating revenue trend for {}", now);

            List<UserEventDTO> events = safeFetch(userEventClient::getAllEvents);
            List<FinanceInvoiceDTO> invoices = fetchAllInvoices();

            Map<Long, Double> invoiceByEvent = invoices.stream()
                    .filter(i -> i.eventId() != null && "PAID".equals(i.status()))
                    .collect(Collectors.toMap(
                            FinanceInvoiceDTO::eventId,
                            i -> i.amount() != null ? i.amount().doubleValue() : 0,
                            Double::sum
                    ));

            // Count events in current month and sum their revenue
            int eventsCount = 0;
            double revenue = 0;
            for (UserEventDTO e : events) {
                if (e.date() != null && YearMonth.from(e.date()).equals(now)) {
                    eventsCount++;
                    revenue += invoiceByEvent.getOrDefault(e.id(), 0.0);
                }
            }

            RevenueTrend rt = new RevenueTrend(
                    year, month, revenue, eventsCount, LocalDateTime.now()
            );
            revenueRepo.save(rt);
            log.info("Revenue trend saved: {} events, revenue={}", eventsCount, revenue);
        }
    }

    // ──────────────────────────────────────────────
    // Service Popularity (from menus + dishes)
    // ──────────────────────────────────────────────

    private void aggregateServicePopularity() {
        YearMonth now = YearMonth.now();
        String periodLabel = now.toString();
        // Check using all records - simple approach
        boolean exists = servicePopularityRepo.findAll().stream()
                .anyMatch(s -> periodLabel.equals(s.getPeriodLabel()));
        if (!exists) {
            log.info("Aggregating service popularity for {}", periodLabel);

            List<UserMenuDTO> menus = safeFetch(userEventClient::getAllMenus);

            // Count dish occurrences across all menus
            Map<String, DishAgg> dishMap = new HashMap<>();
            for (UserMenuDTO menu : menus) {
                if (menu.menuDishes() != null) {
                    for (UserDishDTO dish : menu.menuDishes()) {
                        String dishName = dish.name();
                        if (dishName == null || dishName.isBlank()) continue;
                        dishMap.compute(dishName, (k, v) -> {
                            if (v == null) {
                                return new DishAgg(dish.dishType(), dish.price(), 1);
                            }
                            return new DishAgg(v.category, v.price, v.count + 1);
                        });
                    }
                }
            }

            for (Map.Entry<String, DishAgg> entry : dishMap.entrySet()) {
                DishAgg agg = entry.getValue();
                double revenue = agg.price != null ? agg.price * agg.count : 0;
                ServicePopularity sp = new ServicePopularity(
                        entry.getKey(),
                        agg.category,
                        PeriodType.MONTHLY,
                        periodLabel,
                        agg.count,
                        revenue,
                        LocalDateTime.now()
                );
                servicePopularityRepo.save(sp);
            }
            log.info("Service popularity saved for {} dishes", dishMap.size());
        }
    }

    // ──────────────────────────────────────────────
    // Finance pagination helpers
    // ──────────────────────────────────────────────

    private List<FinanceInvoiceDTO> fetchAllInvoices() {
        List<FinanceInvoiceDTO> all = new ArrayList<>();
        for (int page = 0; page < MAX_PAGES; page++) {
            try {
                PageResponse<FinanceInvoiceDTO> response = financeClient.getInvoices(page, PAGE_SIZE);
                if (response == null) break;
                if (response.content() != null) {
                    all.addAll(response.content());
                }
                if (!response.hasNext()) break;
            } catch (Exception e) {
                log.error("Failed to fetch invoices page {}: {}", page, e.getMessage(), e);
                throw new RuntimeException("Failed to fetch invoices — aggregation aborted to avoid incomplete data", e);
            }
        }
        log.info("Fetched {} invoices across pages", all.size());
        return all;
    }

    private List<FinancePreInvoiceDTO> fetchAllPreInvoices() {
        List<FinancePreInvoiceDTO> all = new ArrayList<>();
        for (int page = 0; page < MAX_PAGES; page++) {
            try {
                PageResponse<FinancePreInvoiceDTO> response = financeClient.getPreInvoices(page, PAGE_SIZE);
                if (response == null) break;
                if (response.content() != null) {
                    all.addAll(response.content());
                }
                if (!response.hasNext()) break;
            } catch (Exception e) {
                log.error("Failed to fetch pre-invoices page {}: {}", page, e.getMessage(), e);
                throw new RuntimeException("Failed to fetch pre-invoices — aggregation aborted to avoid incomplete data", e);
            }
        }
        log.info("Fetched {} pre-invoices across pages", all.size());
        return all;
    }

    private List<FinancePaymentDTO> fetchAllPayments() {
        List<FinancePaymentDTO> all = new ArrayList<>();
        for (int page = 0; page < MAX_PAGES; page++) {
            try {
                PageResponse<FinancePaymentDTO> response = financeClient.getPayments(page, PAGE_SIZE);
                if (response == null) break;
                if (response.content() != null) {
                    all.addAll(response.content());
                }
                if (!response.hasNext()) break;
            } catch (Exception e) {
                log.error("Failed to fetch payments page {}: {}", page, e.getMessage(), e);
                throw new RuntimeException("Failed to fetch payments — aggregation aborted to avoid incomplete data", e);
            }
        }
        log.info("Fetched {} payments across pages", all.size());
        return all;
    }

    // ──────────────────────────────────────────────
    // Safe fetch - returns empty list on failure
    // ──────────────────────────────────────────────

    @FunctionalInterface
    private interface SafeSupplier<T> {
        T get() throws Exception;
    }

    private <T> List<T> safeFetch(SafeSupplier<List<T>> supplier) {
        try {
            List<T> result = supplier.get();
            return result != null ? result : List.of();
        } catch (Exception e) {
            log.error("Failed to fetch data from downstream service: {}", e.getMessage(), e);
            return List.of();
        }
    }

    private double fetchEmployeeRating(Long employeeId) {
        try {
            Map<String, Object> avg = userEventClient.getEmployeeAverageRating(employeeId);
            Object rating = avg.get("averageRating");
            return rating instanceof Number ? ((Number) rating).doubleValue() : 0.0;
        } catch (Exception e) {
            log.debug("Could not fetch rating for employee {}: {}", employeeId, e.getMessage());
            return 0.0;
        }
    }

    /**
     * Calculates on-time percentage for an employee's tickets.
     * Defaults to 100% unless a ticket is still OPEN after the event date has passed.
     * A ticket that is OPEN past the scheduled event date means the work wasn't completed on time.
     */
    private double calculateOnTimePct(List<UserTicketDTO> tickets) {
        if (tickets.isEmpty()) return 100.0;
        LocalDateTime now = LocalDateTime.now();
        long lateCount = tickets.stream()
                .filter(t -> "OPEN".equals(t.ticketStatus())
                        && t.event() != null
                        && t.event().date() != null
                        && t.event().date().isBefore(now))
                .count();
        long onTime = tickets.size() - lateCount;
        return Math.round((onTime * 100.0 / tickets.size()) * 10.0) / 10.0;
    }

    // ──────────────────────────────────────────────
    // Helper record for dish aggregation
    // ──────────────────────────────────────────────

    private record DishAgg(String category, Double price, int count) {}
}
