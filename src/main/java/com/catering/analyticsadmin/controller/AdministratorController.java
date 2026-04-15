package com.catering.analyticsadmin.controller;

import com.catering.analyticsadmin.model.dto.AdministratorCreateDTO;
import com.catering.analyticsadmin.model.dto.AdministratorResponseDTO;
import com.catering.analyticsadmin.model.dto.AdministratorUpdateDTO;
import com.catering.analyticsadmin.service.AdministratorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public class AdministratorController {
    private final AdministratorService administratorService;

    public AdministratorController(AdministratorService administratorService) {
        this.administratorService = administratorService;
    }

    @GetMapping
    public List<AdministratorResponseDTO> getAll() {
        return administratorService.getAll();
    }

    @GetMapping("/{id}")
    public AdministratorResponseDTO getById(@PathVariable Long id) {
        return administratorService.getById(id);
    }

    @PostMapping
    public ResponseEntity<AdministratorResponseDTO> create(@Valid @RequestBody AdministratorCreateDTO request) {
        AdministratorResponseDTO response = administratorService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public AdministratorResponseDTO update(@PathVariable Long id,
                                        @Valid @RequestBody AdministratorUpdateDTO request) {
        return administratorService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        administratorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
