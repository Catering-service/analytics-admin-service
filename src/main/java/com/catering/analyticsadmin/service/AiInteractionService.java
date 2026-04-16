package com.catering.analyticsadmin.service;

import com.catering.analyticsadmin.model.dto.AiInteractionCreateDTO;
import com.catering.analyticsadmin.model.dto.AiInteractionResponseDTO;
import com.catering.analyticsadmin.model.entity.AiInteraction;
import com.catering.analyticsadmin.repository.AiInteractionRepository;
import org.springframework.stereotype.Service;


import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AiInteractionService {
    private final AiInteractionRepository aiInteractionRepository;

    public AiInteractionService(AiInteractionRepository aiInteractionRepository) {
        this.aiInteractionRepository = aiInteractionRepository;
    }

    public List<AiInteractionResponseDTO> getAll() {
        return aiInteractionRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public AiInteractionResponseDTO getById(Long id) {
        AiInteraction aiInteraction = aiInteractionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AI interaction not found"));
        return mapToResponse(aiInteraction);
    }

    public List<AiInteractionResponseDTO> getByClientId(Long clientId) {
        return aiInteractionRepository.findByClientId(clientId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<AiInteractionResponseDTO> getBySessionId(String sessionId) {
        return aiInteractionRepository.findBySessionId(sessionId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public AiInteractionResponseDTO create(AiInteractionCreateDTO request) {
        AiInteraction aiInteraction = new AiInteraction(
                request.getClientId(),
                request.getSessionId(),
                request.getQuestion(),
                request.getAnswer(),
                LocalDateTime.now()
        );

        aiInteraction = aiInteractionRepository.save(aiInteraction);
        return mapToResponse(aiInteraction);
    }

    private AiInteractionResponseDTO mapToResponse(AiInteraction aiInteraction) {
        AiInteractionResponseDTO response = new AiInteractionResponseDTO();
        response.setId(aiInteraction.getId());
        response.setClientId(aiInteraction.getClientId());
        response.setSessionId(aiInteraction.getSessionId());
        response.setQuestion(aiInteraction.getQuestion());
        response.setAnswer(aiInteraction.getAnswer());
        response.setTimestamp(aiInteraction.getTimestamp());
        return response;
    }
}
