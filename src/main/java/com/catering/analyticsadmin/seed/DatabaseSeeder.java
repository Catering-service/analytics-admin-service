package com.catering.analyticsadmin.seed;

import com.catering.analyticsadmin.model.entity.*;
import com.catering.analyticsadmin.model.enums.AdminRole;
import com.catering.analyticsadmin.model.enums.PeriodType;
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
            Administrator admin = new Administrator("sysadmin",
                    "admin@catering.ba",
                    "$2a$10$27mqjrCsX0N7sj/zbVSxqOORAO6W8NIHhpu1ecoiqK03CZdv7Bys2",
                    "System",
                    "Admin",
                    AdminRole.BASIC_ADMIN);
            admin.setLastLoginAt(LocalDateTime.now().minusDays(3).minusHours(3).minusMinutes(17));

            admin = administratorRepository.save(admin);

            Administrator superadmin = new Administrator(
                    "superadmin",
                    "superadmin@catering.ba",
                    "$2a$10$27mqjrCsX0N7sj/zbVSxqOORAO6W8NIHhpu1ecoiqK03CZdv7Bys2",
                    "Super",
                    "Admin",
                    AdminRole.SUPERADMIN
            );
            superadmin.setLastLoginAt(LocalDateTime.now().minusDays(4));
            superadmin = administratorRepository.save(superadmin);


            // --- OFFER TYPES ---
            OfferType decoration = new OfferType("Decoration");
            decoration = offerTypeRepository.save(decoration);

            OfferType music = new OfferType("Music Service");
            music = offerTypeRepository.save(music);

            OfferType catering = new OfferType("Catering");
            catering = offerTypeRepository.save(catering);

            // --- PARTNERS ---
            Partner partner1 = new Partner(
                    "Event Partner d.o.o.",
                    "info@eventpartner.ba",
                    "Amra Kovac",
                    "+38761111222",
                    "https://eventpartner.ba");
            partner1 = partnerRepository.save(partner1);

            Partner partner2 = new Partner(
                    "Dekor Studio",
                    "kontakt@dekor.ba",
                    "Haris Music",
                    "+38762222333",
                    "https://dekor.ba");
            partner2 = partnerRepository.save(partner2);

            // --- PARTNER OFFERS ---
            PartnerOffer partnerOffer1 = new PartnerOffer(
                    partner1,
                    music,
                    "Live music and sound support",
                    1200.0);
            partner1.addOffer(partnerOffer1);
            music.addPartnerOffer(partnerOffer1);
            partnerOfferRepository.save(partnerOffer1);

            PartnerOffer partnerOffer2 = new PartnerOffer(
                    partner2,
                    decoration,
                    "Wedding decoration services",
                    900.0);
            partner2.addOffer(partnerOffer2);
            decoration.addPartnerOffer(partnerOffer2);
            partnerOfferRepository.save(partnerOffer2);

            PartnerOffer partnerOffer3 = new PartnerOffer(
                    partner1,
                    catering,
                    "Full catering service",
                    3000.0);
            partner1.addOffer(partnerOffer3);
            catering.addPartnerOffer(partnerOffer3);
            partnerOfferRepository.save(partnerOffer3);

            // --- ADMIN LOGS ---
            AdminLog adminLog = new AdminLog(admin, "CREATE_PARTNER", now, "Initial partner created.");
            adminLogRepository.save(adminLog);

            // --- AI INTERACTIONS ---
            AiInteraction aiInteraction = new AiInteraction(
                    101L,
                    "sess-001",
                    "Who offers catering?",
                    "Event Partner d.o.o.",
                    now);
            aiInteractionRepository.save(aiInteraction);

            // --- EMPLOYEE ANALYTICS ---
            EmployeeAnalytics employee1 = new EmployeeAnalytics(
                    1L,
                    "Sara Jozić",
                    PeriodType.MONTHLY,
                    "2026-04",
                    156,
                    89,
                    245000.0,
                    4.8,
                    95.0,
                    now);
            employeeAnalyticsRepository.save(employee1);

            EmployeeAnalytics employee2 = new EmployeeAnalytics(
                    2L,
                    "Michael Chen",
                    PeriodType.MONTHLY,
                    "2026-04",
                    142,
                    76,
                    198000.0,
                    4.6,
                    88.0,
                    now);
            employeeAnalyticsRepository.save(employee2);

            // --- CLIENT ANALYTICS ---
            ClientAnalytics client1 = new ClientAnalytics(
                    101L,
                    "Client A",
                    PeriodType.MONTHLY,
                    "2026-04",
                    5,
                    12000.0,
                    true,
                    true,
                    now);
            clientAnalyticsRepository.save(client1);

            ClientAnalytics client2 = new ClientAnalytics(
                    102L,
                    "Client B",
                    PeriodType.MONTHLY,
                    "2026-04",
                    1,
                    3200.0,
                    true,
                    false,
                    now);
            clientAnalyticsRepository.save(client2);

            // --- SERVICE POPULARITY ---
            ServicePopularity service1 = new ServicePopularity(
                    "Wedding Catering",
                    "Catering",
                    PeriodType.MONTHLY,
                    "2026-04",
                    145,
                    320000.0,
                    now);
            servicePopularityRepository.save(service1);

            ServicePopularity service2 = new ServicePopularity(
                    "Decoration Package",
                    "Decoration",
                    PeriodType.MONTHLY,
                    "2026-04",
                    76,
                    95000.0,
                    now);
            servicePopularityRepository.save(service2);

            // --- FINANCIAL ANALYTICS (monthly, January–May 2026) ---
            FinancialAnalytics faJan = new FinancialAnalytics(
                    PeriodType.MONTHLY, "2026-01",
                    520000.0, 310000.0, 210000.0, 85, 10, 2800.0, 4.5, now);
            financialAnalyticsRepository.save(faJan);

            FinancialAnalytics faFeb = new FinancialAnalytics(
                    PeriodType.MONTHLY, "2026-02",
                    480000.0, 290000.0, 190000.0, 72, 8, 3100.0, 4.6, now);
            financialAnalyticsRepository.save(faFeb);

            FinancialAnalytics faMar = new FinancialAnalytics(
                    PeriodType.MONTHLY, "2026-03",
                    610000.0, 350000.0, 260000.0, 98, 12, 3400.0, 4.8, now);
            financialAnalyticsRepository.save(faMar);

            FinancialAnalytics faApr = new FinancialAnalytics(
                    PeriodType.MONTHLY, "2026-04",
                    739000.0, 420000.0, 319000.0, 120, 15, 3344.0, 4.7, now);
            financialAnalyticsRepository.save(faApr);

            FinancialAnalytics faMay = new FinancialAnalytics(
                    PeriodType.MONTHLY, "2026-05",
                    680000.0, 390000.0, 290000.0, 105, 11, 3600.0, 4.4, now);
            financialAnalyticsRepository.save(faMay);

            // --- REVENUE TREND (January–May 2026) ---
            RevenueTrend revenueTrend1 = new RevenueTrend(2026, 1, 520000.0, 95, now);
            revenueTrendRepository.save(revenueTrend1);
            RevenueTrend revenueTrend2 = new RevenueTrend(2026, 2, 480000.0, 80, now);
            revenueTrendRepository.save(revenueTrend2);
            RevenueTrend revenueTrend3 = new RevenueTrend(2026, 3, 610000.0, 110, now);
            revenueTrendRepository.save(revenueTrend3);
            RevenueTrend revenueTrend4 = new RevenueTrend(2026, 4, 739000.0, 135, now);
            revenueTrendRepository.save(revenueTrend4);
            RevenueTrend revenueTrend5 = new RevenueTrend(2026, 5, 680000.0, 116, now);
            revenueTrendRepository.save(revenueTrend5);

            System.out.println("=== DATA SEEDER FINISHED ===");
        };
    }
}