package com.catering.analyticsadmin.seed;

import com.catering.analyticsadmin.model.entity.*;
import com.catering.analyticsadmin.model.enums.AdminRole;
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
            AiInteractionRepository aiInteractionRepository
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

            // --- AI INTERACTIONS (demo) ---
            AiInteraction aiInteraction = new AiInteraction(
                    101L,
                    "sess-001",
                    "Who offers catering?",
                    "Event Partner d.o.o.",
                    now);
            aiInteractionRepository.save(aiInteraction);

            // NOTE: Analytics data (employee, client, financial, revenue, service popularity)
            // is NOT seeded — it is aggregated from real system data by the
            // AnalyticsAggregationScheduler (runs daily at 2 AM, or manually via /api/analytics/refresh).

            System.out.println("=== DATA SEEDER FINISHED ===");
        };
    }
}