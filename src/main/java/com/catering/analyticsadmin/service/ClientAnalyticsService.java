package com.catering.analyticsadmin.service;

import com.catering.analyticsadmin.model.dto.ClientAnalyticsResponseDTO;
import com.catering.analyticsadmin.model.dto.FinancialAnalyticsResponseDTO;
import com.catering.analyticsadmin.model.entity.ClientAnalytics;
import com.catering.analyticsadmin.repository.ClientAnalyticsRepository;

import java.util.List;

public class ClientAnalyticsService {
    private final ClientAnalyticsRepository clientAnalyticsRepository;

    public ClientAnalyticsService(ClientAnalyticsRepository clientAnalyticsRepository) {
        this.clientAnalyticsRepository = clientAnalyticsRepository;
    }

    public List<ClientAnalyticsResponseDTO> getAll() {
        return clientAnalyticsRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ClientAnalyticsResponseDTO getById(Long id) {
        ClientAnalytics clientAnalytics = clientAnalyticsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client analytics not found"));
        return mapToResponse(clientAnalytics);
    }

    private ClientAnalyticsResponseDTO mapToResponse(ClientAnalytics entity) {
        ClientAnalyticsResponseDTO dto = new ClientAnalyticsResponseDTO();
        dto.setId(entity.getId());
        dto.setPeriodLabel(entity.getPeriodLabel());
        dto.setPeriodType(entity.getPeriodType());
        dto.setActive(entity.getActive());
        dto.setClientName(entity.getClientName());
        dto.setEventsCount(entity.getEventsCount());
        dto.setReturning(entity.getReturning());
        dto.setTotalSpent(entity.getTotalSpent());

        return dto;
    }
}
