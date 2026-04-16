package com.catering.analyticsadmin.controller;

import com.catering.analyticsadmin.model.dto.AiInteractionCreateDTO;
import com.catering.analyticsadmin.model.dto.AiInteractionResponseDTO;
import com.catering.analyticsadmin.service.AiInteractionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai-interactions")
public class AiInteractionController {
    private final AiInteractionService aiInteractionService;

    public AiInteractionController(AiInteractionService aiInteractionService) {
        this.aiInteractionService = aiInteractionService;
    }

    @GetMapping
    public List<AiInteractionResponseDTO> getAll() {
        return aiInteractionService.getAll();
    }

    @GetMapping("/{id}")
    public AiInteractionResponseDTO getById(@PathVariable Long id) {
        return aiInteractionService.getById(id);
    }

    @GetMapping("/client/{clientId}")
    public List<AiInteractionResponseDTO> getByClientId(@PathVariable Long clientId) {
        return aiInteractionService.getByClientId(clientId);
    }

    @GetMapping("/session/{sessionId}")
    public List<AiInteractionResponseDTO> getBySessionId(@PathVariable String sessionId) {
        return aiInteractionService.getBySessionId(sessionId);
    }

    @PostMapping
    public ResponseEntity<AiInteractionResponseDTO> create(@Valid @RequestBody AiInteractionCreateDTO request) {
        AiInteractionResponseDTO response = aiInteractionService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}