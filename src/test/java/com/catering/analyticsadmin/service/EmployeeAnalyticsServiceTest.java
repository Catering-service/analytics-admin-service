package com.catering.analyticsadmin.service;

import com.catering.analyticsadmin.model.entity.EmployeeAnalytics;
import com.catering.analyticsadmin.model.enums.PeriodType;
import com.catering.analyticsadmin.repository.EmployeeAnalyticsRepository;
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
class EmployeeAnalyticsServiceTest {

    @Mock
    private EmployeeAnalyticsRepository employeeAnalyticsRepository;

    @InjectMocks
    private EmployeeAnalyticsService employeeAnalyticsService;

    @Test
    void getAllEmployeeAnalytics_returnsMappedEntities() {
        EmployeeAnalytics entity = new EmployeeAnalytics(99L, "Jane", PeriodType.YEARLY, "2026-W18", 15, 7, 3500.0, 4.8, 92.0, LocalDateTime.now());
        entity.setId(2L);

        when(employeeAnalyticsRepository.findAll()).thenReturn(List.of(entity));

        var results = employeeAnalyticsService.getAllEmployeeAnalytics();

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getEmployeeId()).isEqualTo(99L);
        assertThat(results.get(0).getEmployeeName()).isEqualTo("Jane");
    }

    @Test
    void getEmployeeAnalytics_whenMissing_throws() {
        when(employeeAnalyticsRepository.findById(123L)).thenReturn(Optional.empty());

        var exception = assertThrows(RuntimeException.class, () -> employeeAnalyticsService.getEmployeeAnalytics(123L));

        assertThat(exception).hasMessageContaining("Employee analytics not found");
    }
}
