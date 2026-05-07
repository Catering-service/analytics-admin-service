package com.catering.analyticsadmin.service;

import com.catering.analyticsadmin.model.entity.ClientAnalytics;
import com.catering.analyticsadmin.model.enums.PeriodType;
import com.catering.analyticsadmin.repository.ClientAnalyticsRepository;
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
class ClientAnalyticsServiceTest {

    @Mock
    private ClientAnalyticsRepository clientAnalyticsRepository;

    @InjectMocks
    private ClientAnalyticsService clientAnalyticsService;

    @Test
    void getAll_returnsMappedResponses() {
        ClientAnalytics entity = new ClientAnalytics(1L, "Client A", PeriodType.MONTHLY, "2026-05", 3, 1500.0, true, true, LocalDateTime.now());
        entity.setId(10L);

        when(clientAnalyticsRepository.findAll()).thenReturn(List.of(entity));

        var responses = clientAnalyticsService.getAll();

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getClientName()).isEqualTo("Client A");
    }

    @Test
    void getById_whenMissing_throws() {
        when(clientAnalyticsRepository.findById(20L)).thenReturn(Optional.empty());

        var exception = assertThrows(RuntimeException.class, () -> clientAnalyticsService.getById(20L));

        assertThat(exception).hasMessageContaining("Client analytics not found");
    }
}
