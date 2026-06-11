package com.catering.analyticsadmin.controller;

import com.catering.analyticsadmin.model.dto.AdministratorCreateDTO;
import com.catering.analyticsadmin.model.dto.AdministratorResponseDTO;
import com.catering.analyticsadmin.model.dto.AdministratorUpdateDTO;
import com.catering.analyticsadmin.service.AdministratorService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/administrators")
public class AdministratorController {
    private final AdministratorService administratorService;

    public AdministratorController(AdministratorService administratorService) {
        this.administratorService = administratorService;
    }

    @GetMapping
    public List<AdministratorResponseDTO> getAll() {
        return administratorService.getAll();
    }

    @GetMapping("/paginated")
    public Page<AdministratorResponseDTO> getAllPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return administratorService.getAll(pageable);
    }

    @GetMapping("/{id}")
    public AdministratorResponseDTO getById(@PathVariable Long id) {
        return administratorService.getById(id);
    }

    @GetMapping("/me")
    public AdministratorResponseDTO getCurrentAdmin(@RequestParam String email) {
        return administratorService.getByEmail(email);
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
