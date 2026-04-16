package com.catering.analyticsadmin.repository;

import com.catering.analyticsadmin.model.entity.AiInteraction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AiInteractionRepository extends JpaRepository<AiInteraction, Long> {
    List<AiInteraction> findByClientId(Long clientId);

    List<AiInteraction> findBySessionId(String sessionId);
}