package com.catering.analyticsadmin.service;

import com.catering.analyticsadmin.model.entity.AdminLog;
import com.catering.analyticsadmin.model.entity.Administrator;
import com.catering.analyticsadmin.repository.AdminLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminLogServiceTest {

    @Mock
    private AdminLogRepository adminLogRepository;

    @InjectMocks
    private AdminLogService adminLogService;

    @Test
    void getAll_returnsMappedAdminLogResponses() {
        Administrator admin = new Administrator();
        admin.setId(10L);
        admin.setUsername("admin1");

        AdminLog adminLog = new AdminLog(admin, "LOGIN", LocalDateTime.now(), "details");
        adminLog.setId(100L);

        when(adminLogRepository.findAllWithAdministrator()).thenReturn(List.of(adminLog));

        var results = adminLogService.getAll();

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getAdministratorId()).isEqualTo(10L);
        assertThat(results.get(0).getAction()).isEqualTo("LOGIN");
    }

    @Test
    void getById_returnsMappedAdminLogResponse() {
        Administrator admin = new Administrator();
        admin.setId(10L);
        admin.setUsername("admin1");

        AdminLog adminLog = new AdminLog(admin, "LOGIN", LocalDateTime.now(), "details");
        adminLog.setId(100L);

        when(adminLogRepository.findByIdWithAdministrator(100L)).thenReturn(Optional.of(adminLog));

        var result = adminLogService.getById(100L);

        assertThat(result).isNotNull();
        assertThat(result.getAdministratorId()).isEqualTo(10L);
        assertThat(result.getAction()).isEqualTo("LOGIN");
    }

    @Test
    void getAll_withPageable_returnsMappedPagedAdminLogResponses() {
        Administrator admin = new Administrator();
        admin.setId(10L);
        admin.setUsername("admin1");

        AdminLog adminLog = new AdminLog(admin, "LOGIN", LocalDateTime.now(), "details");
        adminLog.setId(100L);

        // ✅ Stub the overload that accepts a Pageable argument
        when(adminLogRepository.findAllWithAdministrator(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(adminLog)));

        var results = adminLogService.getAll(Pageable.unpaged());
        var resultList = results.getContent();

        assertThat(resultList).hasSize(1);
        assertThat(resultList.get(0).getAdministratorId()).isEqualTo(10L);
        assertThat(resultList.get(0).getAction()).isEqualTo("LOGIN");
    }

    @Test
    void getById_whenNotFound_throws() {
        when(adminLogRepository.findByIdWithAdministrator(5L)).thenReturn(Optional.empty());

        var exception = assertThrows(RuntimeException.class, () -> adminLogService.getById(5L));

        assertThat(exception).hasMessageContaining("Admin log not found");
    }
}
