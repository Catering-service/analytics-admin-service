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
            Administrator admin = new Administrator("sysadmin",
                    "admin@eliteevents.ba",
                    "$2a$10$examplehash",
                    "System",
                    "Admin",
                    "SUPER_ADMIN");

            admin = administratorRepository.save(admin);

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
                    "Sarah Johnson",
                    PeriodType.MONTHLY,
                    "2026-04",
                    156,
                    89,
                    245000.0,
                    4.8,
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

            // --- FINANCIAL ANALYTICS ---
            FinancialAnalytics financialAnalytics = new FinancialAnalytics(
                    PeriodType.MONTHLY,
                    "2026-04",
                    739000.0,
                    420000.0,
                    319000.0,
                    120,
                    15,
                    3344.0,
                    now);
            financialAnalyticsRepository.save(financialAnalytics);

            // --- REVENUE TREND ---
            RevenueTrend revenueTrend1 = new RevenueTrend(2026, 1, 50000.0, 20, now);
            revenueTrendRepository.save(revenueTrend1);

            RevenueTrend revenueTrend2 = new RevenueTrend(2026, 2, 54000.0, 22, now);
            revenueTrendRepository.save(revenueTrend2);

            System.out.println("=== DATA SEEDER FINISHED ===");
        };
    }
}