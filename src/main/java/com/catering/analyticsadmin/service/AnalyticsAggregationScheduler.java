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
import java.util.stream.Collectors;

/**
 * Scheduled service that periodically fetches data from other microservices,
 * aggregates it, and stores the results in analytics-admin's database.
 *
 * Runs daily at 2:00 AM. Each aggregation method is idempotent —
 * skips if data for the current period already exists.
 */
@Service
public class AnalyticsAggregationScheduler {

    private static final Logger log = LoggerFactory.getLogger(AnalyticsAggregationScheduler.class);
    private static final int PAGE_SIZE = 1000;

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

    // ──────────────────────────────────────────────
    // Scheduling & manual trigger
    // ──────────────────────────────────────────────

    @Scheduled(cron = "0 0 2 * * *") // 2:00 AM every day
    public void runDailyAggregation() {
        triggerAggregation(false);
    }

    /**
     * Public entry point for manual refresh.
     * When forceRefresh=true, deletes current-period records first so they are re-created.
     */
    public void triggerAggregation(boolean forceRefresh) {
        if (forceRefresh) {
            String periodLabel = YearMonth.now().toString();
            log.info("Force refresh: clearing existing analytics data for period {}", periodLabel);
            deleteByPeriod(periodLabel);
        }
        log.info("=== Starting analytics aggregation (force={}) ===", forceRefresh);
        try { aggregateFinancialAnalytics(); } catch (Exception e) { log.error("Financial analytics aggregation failed", e); }
        try { aggregateEmployeeAnalytics(); } catch (Exception e) { log.error("Employee analytics aggregation failed", e); }
        try { aggregateClientAnalytics(); } catch (Exception e) { log.error("Client analytics aggregation failed", e); }
        try { aggregateRevenueTrend(); } catch (Exception e) { log.error("Revenue trend aggregation failed", e); }
        try { aggregateServicePopularity(); } catch (Exception e) { log.error("Service popularity aggregation failed", e); }
        log.info("=== Analytics aggregation complete ===");
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

            // Filter to current month
            List<FinanceInvoiceDTO> monthInvoices = invoices.stream()
                    .filter(i -> i.issueDate() != null
                            && YearMonth.from(i.issueDate()).equals(now))
                    .toList();

            double totalRevenue = monthInvoices.stream()
                    .filter(i -> "PAID".equals(i.status()))
                    .mapToDouble(i -> i.amount() != null ? i.amount().doubleValue() : 0)
                    .sum();

            double totalExpenses = preInvoices.stream()
                    .filter(p -> p.issueDate() != null
                            && YearMonth.from(p.issueDate()).equals(now))
                    .mapToDouble(p -> p.totalPrice() != null ? p.totalPrice().doubleValue() : 0)
                    .sum();

            long paidCount = monthInvoices.stream()
                    .filter(i -> "PAID".equals(i.status())).count();
            long unpaidCount = monthInvoices.stream()
                    .filter(i -> "PENDING".equals(i.status())).count();

            double avgEventValue = paidCount > 0 ? totalRevenue / paidCount : 0;
            double netProfit = totalRevenue - totalExpenses;

            FinancialAnalytics fa = new FinancialAnalytics(
                    PeriodType.MONTHLY,
                    periodLabel,
                    totalRevenue,
                    totalExpenses,
                    netProfit,
                    (int) paidCount,
                    (int) unpaidCount,
                    avgEventValue,
                    0.0, // averageRating — not yet implemented
                    LocalDateTime.now()
            );
            financialRepo.save(fa);
            log.info("Financial analytics saved: revenue={}, expenses={}, profit={}", totalRevenue, totalExpenses, netProfit);
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
                        0, // salesCompleted — no sales tracking yet
                        revenueGenerated,
                        fetchEmployeeRating(emp.id()), // performanceRating from real data
                        0.0, // completedOnTimePct — not yet implemented
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
            log.info("Aggregating revenue trend for {}-{:02d}", year, month);

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
        // Check using all records — simple approach
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
                        dishMap.compute(dish.name(), (k, v) -> {
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
        int page = 0;
        PageResponse<FinanceInvoiceDTO> response;
        do {
            response = financeClient.getInvoices(page, PAGE_SIZE);
            if (response != null && response.content() != null) {
                all.addAll(response.content());
            }
            page++;
        } while (response != null && response.hasNext());
        log.debug("Fetched {} invoices", all.size());
        return all;
    }

    private List<FinancePreInvoiceDTO> fetchAllPreInvoices() {
        List<FinancePreInvoiceDTO> all = new ArrayList<>();
        int page = 0;
        PageResponse<FinancePreInvoiceDTO> response;
        do {
            response = financeClient.getPreInvoices(page, PAGE_SIZE);
            if (response != null && response.content() != null) {
                all.addAll(response.content());
            }
            page++;
        } while (response != null && response.hasNext());
        return all;
    }

    // ──────────────────────────────────────────────
    // Safe fetch — returns empty list on failure
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
            log.warn("Failed to fetch data: {}", e.getMessage());
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

    // ──────────────────────────────────────────────
    // Helper record for dish aggregation
    // ──────────────────────────────────────────────

    private record DishAgg(String category, Double price, int count) {}
}
