package com.catering.analyticsadmin.service;

import com.catering.analyticsadmin.model.dto.AdministratorCreateDTO;
import com.catering.analyticsadmin.model.dto.AdministratorResponseDTO;
import com.catering.analyticsadmin.model.dto.AdministratorUpdateDTO;
import com.catering.analyticsadmin.model.entity.Administrator;
import com.catering.analyticsadmin.repository.AdministratorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdministratorService {

    private final AdministratorRepository administratorRepository;

    public AdministratorService(AdministratorRepository administratorRepository) {
        this.administratorRepository = administratorRepository;
    }

    public List<AdministratorResponseDTO> getAll() {
        return administratorRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public AdministratorResponseDTO getById(Long id) {
        Administrator administrator = administratorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Administrator not found"));
        return mapToResponse(administrator);
    }

    public AdministratorResponseDTO create(AdministratorCreateDTO request) {
        if (administratorRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (administratorRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Administrator administrator = new Administrator(
                request.getUsername(),
                request.getEmail(),
                request.getPasswordHash(),
                request.getFirstName(),
                request.getLastName(),
                request.getRole()
        );

        administrator = administratorRepository.save(administrator);
        return mapToResponse(administrator);
    }

    public AdministratorResponseDTO update(Long id, AdministratorUpdateDTO request) {
        Administrator administrator = administratorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Administrator not found"));

        administrator.setUsername(request.getUsername());
        administrator.setEmail(request.getEmail());
        administrator.setFirstName(request.getFirstName());
        administrator.setLastName(request.getLastName());
        administrator.setRole(request.getRole());
        administrator.setActive(request.getActive());

        administrator = administratorRepository.save(administrator);
        return mapToResponse(administrator);
    }

    public void delete(Long id) {
        if (!administratorRepository.existsById(id)) {
            throw new RuntimeException("Administrator not found");
        }
        administratorRepository.deleteById(id);
    }

    private AdministratorResponseDTO mapToResponse(Administrator administrator) {
        AdministratorResponseDTO response = new AdministratorResponseDTO();
        response.setId(administrator.getId());
        response.setUsername(administrator.getUsername());
        response.setEmail(administrator.getEmail());
        response.setFirstName(administrator.getFirstName());
        response.setLastName(administrator.getLastName());
        response.setRole(administrator.getRole());
        response.setActive(administrator.getActive());
        response.setCreatedAt(administrator.getCreatedAt());
        response.setLastLoginAt(administrator.getLastLoginAt());
        return response;
    }
}