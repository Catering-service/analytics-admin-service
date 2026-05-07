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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
    void getAll_withPageable_returnsMappedPagedResponses() {
        Administrator admin1 = new Administrator("user1", "user1@example.com", "hash", "John", "Doe", AdminRole.ANALYTICS);
        admin1.setId(1L);
        admin1.setCreatedAt(LocalDateTime.now());

        Administrator admin2 = new Administrator("user2", "user2@example.com", "hash", "Jane", "Doe", AdminRole.BASIC_ADMIN);
        admin2.setId(2L);
        admin2.setCreatedAt(LocalDateTime.now());

        Pageable pageable = PageRequest.of(0, 10);
        Page<Administrator> page = new PageImpl<>(List.of(admin1, admin2), pageable, 2);

        when(administratorRepository.findAll(pageable)).thenReturn(page);

        var responses = administratorService.getAll(pageable);

        assertThat(responses.getContent()).hasSize(2);
        assertThat(responses.getTotalElements()).isEqualTo(2);
        assertThat(responses.getNumber()).isEqualTo(0);
        assertThat(responses.getSize()).isEqualTo(10);
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
