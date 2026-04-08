package com.catering.analyticsadmin.seed;

import com.catering.analyticsadmin.model.AdminLog;
import com.catering.analyticsadmin.model.AiInteraction;
import com.catering.analyticsadmin.model.OfferType;
import com.catering.analyticsadmin.model.Partner;
import com.catering.analyticsadmin.model.PartnerOffer;
import com.catering.analyticsadmin.repository.AdminLogRepository;
import com.catering.analyticsadmin.repository.AiInteractionRepository;
import com.catering.analyticsadmin.repository.OfferTypeRepository;
import com.catering.analyticsadmin.repository.PartnerOfferRepository;
import com.catering.analyticsadmin.repository.PartnerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class DatabaseSeeder {

    @Bean
    CommandLineRunner seedData(
            PartnerRepository partnerRepository,
            OfferTypeRepository offerTypeRepository,
            PartnerOfferRepository partnerOfferRepository,
            AdminLogRepository adminLogRepository,
            AiInteractionRepository aiInteractionRepository
    ) {
        return args -> {
            if (partnerRepository.count() > 0) {
                return;
            }

            OfferType decoration = offerTypeRepository.save(
                    OfferType.builder().name("Decoration").build()
            );

            OfferType music = offerTypeRepository.save(
                    OfferType.builder().name("Music Service").build()
            );

            Partner partner = partnerRepository.save(
                    Partner.builder()
                            .name("Event Partner d.o.o.")
                            .email("info@eventpartner.ba")
                            .contactPerson("Amra Kovac")
                            .contactPhone("+38761111222")
                            .websiteUrl("https://eventpartner.ba")
                            .build()
            );

            partnerOfferRepository.save(
                    PartnerOffer.builder()
                            .partner(partner)
                            .offerType(music)
                            .description("Live music and sound support.")
                            .price(1200.0)
                            .build()
            );

            adminLogRepository.save(
                    AdminLog.builder()
                            .adminId(1L)
                            .action("CREATE_PARTNER")
                            .timestamp(LocalDateTime.now())
                            .details("Created initial partner.")
                            .build()
            );

            aiInteractionRepository.save(
                    AiInteraction.builder()
                            .clientId(101L)
                            .sessionId("session-001")
                            .question("Which partner offers music services?")
                            .answer("Event Partner d.o.o. offers music services.")
                            .timestamp(LocalDateTime.now())
                            .build()
            );
        };
    }
}