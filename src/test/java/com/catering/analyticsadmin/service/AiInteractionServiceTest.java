package com.catering.analyticsadmin.service;

import com.catering.analyticsadmin.model.dto.AiInteractionCreateDTO;
import com.catering.analyticsadmin.model.entity.AiInteraction;
import com.catering.analyticsadmin.repository.AiInteractionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AiInteractionServiceTest {

    @Mock
    private AiInteractionRepository aiInteractionRepository;

    @InjectMocks
    private AiInteractionService aiInteractionService;

    @Test
    void getById_whenMissing_throws() {
        when(aiInteractionRepository.findById(1L)).thenReturn(Optional.empty());

        var exception = assertThrows(RuntimeException.class, () -> aiInteractionService.getById(1L));

        assertThat(exception).hasMessageContaining("AI interaction not found");
    }

    @Test
    void create_savesAndReturnsResponse() {
        AiInteractionCreateDTO request = new AiInteractionCreateDTO();
        request.setClientId(5L);
        request.setSessionId("session-1");
        request.setQuestion("What is the total?");
        request.setAnswer("100");

        AiInteraction saved = new AiInteraction(5L, "session-1", "What is the total?", "100", LocalDateTime.now());
        saved.setId(3L);

        when(aiInteractionRepository.save(org.mockito.ArgumentMatchers.any(AiInteraction.class))).thenReturn(saved);

        var response = aiInteractionService.create(request);

        assertThat(response.getId()).isEqualTo(3L);
        assertThat(response.getClientId()).isEqualTo(5L);
        assertThat(response.getSessionId()).isEqualTo("session-1");
    }
}
