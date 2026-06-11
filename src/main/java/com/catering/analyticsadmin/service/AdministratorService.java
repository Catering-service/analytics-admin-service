package com.catering.analyticsadmin.service;

import com.catering.analyticsadmin.feign.AuthServiceClient;
import com.catering.analyticsadmin.model.dto.AdministratorCreateDTO;
import com.catering.analyticsadmin.model.dto.AdministratorResponseDTO;
import com.catering.analyticsadmin.model.dto.AdministratorUpdateDTO;
import com.catering.analyticsadmin.model.dto.external.AuthUserCreateRequest;
import com.catering.analyticsadmin.model.entity.Administrator;
import com.catering.analyticsadmin.repository.AdministratorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdministratorService {

    private final AdministratorRepository administratorRepository;
    private final AuthServiceClient authServiceClient;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AdministratorService(AdministratorRepository administratorRepository,
                                AuthServiceClient authServiceClient) {
        this.administratorRepository = administratorRepository;
        this.authServiceClient = authServiceClient;
    }

    public List<AdministratorResponseDTO> getAll() {
        return administratorRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public Page<AdministratorResponseDTO> getAll(Pageable pageable) {
        return administratorRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    public AdministratorResponseDTO getById(Long id) {
        Administrator administrator = administratorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Administrator not found"));
        return mapToResponse(administrator);
    }

    public AdministratorResponseDTO getByEmail(String email) {
        Administrator administrator = administratorRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Administrator not found"));
        return mapToResponse(administrator);
    }

    @Transactional
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
                passwordEncoder.encode(request.getPassword()),
                request.getFirstName(),
                request.getLastName(),
                request.getRole()
        );

        administrator = administratorRepository.save(administrator);

        // Sync admin to auth-service
        try {
            AuthUserCreateRequest authRequest = new AuthUserCreateRequest(
                    request.getFirstName(),
                    request.getLastName(),
                    request.getEmail(),
                    request.getPassword(),
                    "ADMIN"
            );
            authServiceClient.createUser(authRequest);
        } catch (Exception e) {
            System.err.println("Failed to sync admin to auth-service: " + e.getMessage());
        }

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