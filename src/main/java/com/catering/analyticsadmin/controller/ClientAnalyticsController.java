package com.catering.analyticsadmin.controller;

import com.catering.analyticsadmin.model.dto.ClientAnalyticsResponseDTO;
import com.catering.analyticsadmin.service.ClientAnalyticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/client-analytics")
public class ClientAnalyticsController {
    private final ClientAnalyticsService clientAnalyticsService;

    public ClientAnalyticsController(ClientAnalyticsService clientAnalyticsService) {
        this.clientAnalyticsService = clientAnalyticsService;
    }

    @GetMapping
    public List<ClientAnalyticsResponseDTO> getAll() {
        return clientAnalyticsService.getAll();
    }

    @GetMapping("/{id}")
    public ClientAnalyticsResponseDTO getById(@PathVariable Long id) {
        return clientAnalyticsService.getById(id);
    }

    @GetMapping("/client/{clientId}")
    public List<ClientAnalyticsResponseDTO> getByClientId(@PathVariable Long clientId) {
        return clientAnalyticsService.getByClientId(clientId);
    }
}
