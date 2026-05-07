package com.catering.analyticsadmin.service;

import com.catering.analyticsadmin.model.dto.AdministratorCreateDTO;
import com.catering.analyticsadmin.model.dto.AdministratorUpdateDTO;
import com.catering.analyticsadmin.model.entity.Administrator;
import com.catering.analyticsadmin.model.enums.AdminRole;
import com.catering.analyticsadmin.repository.AdministratorRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdministratorServiceTest {

    @Mock
    private AdministratorRepository administratorRepository;

    @InjectMocks
    private AdministratorService administratorService;

    @Test
    void getAll_returnsMappedResponseList() {
        Administrator admin = new Administrator("user", "user@example.com", "hash", "John", "Doe", AdminRole.ANALYTICS);
        admin.setId(1L);
        admin.setCreatedAt(LocalDateTime.now());
        admin.setLastLoginAt(LocalDateTime.now().minusDays(1));

        when(administratorRepository.findAll()).thenReturn(List.of(admin));

        var responses = administratorService.getAll();

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getId()).isEqualTo(1L);
        assertThat(responses.get(0).getUsername()).isEqualTo("user");
    }

    @Test
    void getById_whenMissing_throws() {
        when(administratorRepository.findById(1L)).thenReturn(Optional.empty());

        var exception = assertThrows(RuntimeException.class, () -> administratorService.getById(1L));

        assertThat(exception).hasMessageContaining("Administrator not found");
    }

    @Test
    void create_whenUsernameExists_throws() {
        AdministratorCreateDTO request = new AdministratorCreateDTO();
        request.setUsername("user");
        request.setEmail("user@example.com");
        request.setPasswordHash("hash");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setRole(AdminRole.ANALYTICS);

        when(administratorRepository.existsByUsername("user")).thenReturn(true);

        var exception = assertThrows(RuntimeException.class, () -> administratorService.create(request));
        assertThat(exception).hasMessageContaining("Username already exists");
        verify(administratorRepository, never()).save(any());
    }

    @Test
    void delete_whenNotFound_throws() {
        when(administratorRepository.existsById(99L)).thenReturn(false);

        var exception = assertThrows(RuntimeException.class, () -> administratorService.delete(99L));

        assertThat(exception).hasMessageContaining("Administrator not found");
    }
}
