package com.catering.analyticsadmin.seed;

import com.catering.analyticsadmin.model.*;
import com.catering.analyticsadmin.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class DatabaseSeeder {

    @Bean
    CommandLineRunner seedData(
            AdministratorRepository administratorRepository,
            PartnerRepository partnerRepository,
            OfferTypeRepository offerTypeRepository,
            PartnerOfferRepository partnerOfferRepository,
            AdminLogRepository adminLogRepository,
            AiInteractionRepository aiInteractionRepository,
            EmployeeAnalyticsRepository employeeAnalyticsRepository,
            ClientAnalyticsRepository clientAnalyticsRepository,
            ServicePopularityRepository servicePopularityRepository,
            FinancialAnalyticsRepository financialAnalyticsRepository,
            RevenueTrendRepository revenueTrendRepository
    ) {
        return args -> {

            System.out.println("=== DATA SEEDER STARTED ===");

            if (administratorRepository.count() > 0) {
                System.out.println("=== SEED SKIPPED ===");
                return;
            }

            LocalDateTime now = LocalDateTime.now();

            // --- ADMIN ---
            Administrator admin = administratorRepository.save(
                    Administrator.builder()
                            .username("sysadmin")
                            .email("admin@eliteevents.ba")
                            .passwordHash("$2a$10$examplehash")
                            .firstName("System")
                            .lastName("Admin")
                            .role("SUPER_ADMIN")
                            .active(true)
                            .createdAt(now)
                            .build()
            );

            // --- OFFER TYPES ---
            OfferType decoration = offerTypeRepository.save(
                    OfferType.builder().name("Decoration").build()
            );

            OfferType music = offerTypeRepository.save(
                    OfferType.builder().name("Music Service").build()
            );

            OfferType catering = offerTypeRepository.save(
                    OfferType.builder().name("Catering").build()
            );

            // --- PARTNERS ---
            Partner partner1 = partnerRepository.save(
                    Partner.builder()
                            .name("Event Partner d.o.o.")
                            .email("info@eventpartner.ba")
                            .contactPerson("Amra Kovac")
                            .contactPhone("+38761111222")
                            .websiteUrl("https://eventpartner.ba")
                            .build()
            );

            Partner partner2 = partnerRepository.save(
                    Partner.builder()
                            .name("Dekor Studio")
                            .email("kontakt@dekor.ba")
                            .contactPerson("Haris Music")
                            .contactPhone("+38762222333")
                            .websiteUrl("https://dekor.ba")
                            .build()
            );

            // --- PARTNER OFFERS ---
            partnerOfferRepository.save(
                    PartnerOffer.builder()
                            .partner(partner1)
                            .offerType(music)
                            .description("Live music and sound support")
                            .price(1200.0)
                            .build()
            );

            partnerOfferRepository.save(
                    PartnerOffer.builder()
                            .partner(partner2)
                            .offerType(decoration)
                            .description("Wedding decoration services")
                            .price(900.0)
                            .build()
            );

            partnerOfferRepository.save(
                    PartnerOffer.builder()
                            .partner(partner1)
                            .offerType(catering)
                            .description("Full catering service")
                            .price(3000.0)
                            .build()
            );

            // --- ADMIN LOGS ---
            adminLogRepository.save(
                    AdminLog.builder()
                            .administrator(admin)
                            .action("CREATE_PARTNER")
                            .timestamp(now)
                            .details("Initial partner created")
                            .build()
            );

            // --- AI INTERACTIONS ---
            aiInteractionRepository.save(
                    AiInteraction.builder()
                            .clientId(101L)
                            .sessionId("sess-001")
                            .question("Who offers catering?")
                            .answer("Event Partner d.o.o.")
                            .timestamp(now)
                            .build()
            );

            // --- EMPLOYEE ANALYTICS ---
            employeeAnalyticsRepository.save(
                    EmployeeAnalytics.builder()
                            .employeeId(1L)
                            .employeeName("Sarah Johnson")
                            .periodType(PeriodType.MONTHLY)
                            .periodLabel("2026-04")
                            .ticketsProcessed(156)
                            .salesCompleted(89)
                            .revenueGenerated(245000.0)
                            .performanceRating(4.8)
                            .createdAt(now)
                            .build()
            );

            employeeAnalyticsRepository.save(
                    EmployeeAnalytics.builder()
                            .employeeId(2L)
                            .employeeName("Michael Chen")
                            .periodType(PeriodType.MONTHLY)
                            .periodLabel("2026-04")
                            .ticketsProcessed(142)
                            .salesCompleted(76)
                            .revenueGenerated(198000.0)
                            .performanceRating(4.6)
                            .createdAt(now)
                            .build()
            );

            // --- CLIENT ANALYTICS ---
            clientAnalyticsRepository.save(
                    ClientAnalytics.builder()
                            .clientId(101L)
                            .clientName("Client A")
                            .periodType(PeriodType.MONTHLY)
                            .periodLabel("2026-04")
                            .eventsCount(5)
                            .totalSpent(12000.0)
                            .isActive(true)
                            .isReturning(true)
                            .createdAt(now)
                            .build()
            );

            clientAnalyticsRepository.save(
                    ClientAnalytics.builder()
                            .clientId(102L)
                            .clientName("Client B")
                            .periodType(PeriodType.MONTHLY)
                            .periodLabel("2026-04")
                            .eventsCount(1)
                            .totalSpent(3200.0)
                            .isActive(true)
                            .isReturning(false)
                            .createdAt(now)
                            .build()
            );

            // --- SERVICE POPULARITY ---
            servicePopularityRepository.save(
                    ServicePopularity.builder()
                            .serviceName("Wedding Catering")
                            .serviceCategory("Catering")
                            .periodType(PeriodType.MONTHLY)
                            .periodLabel("2026-04")
                            .selectionCount(145)
                            .revenueGenerated(320000.0)
                            .createdAt(now)
                            .build()
            );

            servicePopularityRepository.save(
                    ServicePopularity.builder()
                            .serviceName("Decoration Package")
                            .serviceCategory("Decoration")
                            .periodType(PeriodType.MONTHLY)
                            .periodLabel("2026-04")
                            .selectionCount(76)
                            .revenueGenerated(95000.0)
                            .createdAt(now)
                            .build()
            );

            // --- FINANCIAL ANALYTICS ---
            financialAnalyticsRepository.save(
                    FinancialAnalytics.builder()
                            .periodType(PeriodType.MONTHLY)
                            .periodLabel("2026-04")
                            .totalRevenue(739000.0)
                            .totalExpenses(420000.0)
                            .netProfit(319000.0)
                            .paidInvoicesCount(120)
                            .unpaidInvoicesCount(15)
                            .averageEventValue(3344.0)
                            .createdAt(now)
                            .build()
            );

            // --- REVENUE TREND ---
            revenueTrendRepository.save(
                    RevenueTrend.builder()
                            .year(2026)
                            .month(1)
                            .revenue(50000.0)
                            .eventsCount(20)
                            .createdAt(now)
                            .build()
            );

            revenueTrendRepository.save(
                    RevenueTrend.builder()
                            .year(2026)
                            .month(2)
                            .revenue(54000.0)
                            .eventsCount(22)
                            .createdAt(now)
                            .build()
            );

            System.out.println("=== DATA SEEDER FINISHED ===");
        };
    }
}