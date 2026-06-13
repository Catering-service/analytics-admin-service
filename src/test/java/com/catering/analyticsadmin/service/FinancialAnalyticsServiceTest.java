package com.catering.analyticsadmin.service;

import com.catering.analyticsadmin.model.entity.FinancialAnalytics;
import com.catering.analyticsadmin.model.enums.PeriodType;
import com.catering.analyticsadmin.repository.FinancialAnalyticsRepository;
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
class FinancialAnalyticsServiceTest {

    @Mock
    private FinancialAnalyticsRepository financialAnalyticsRepository;

    @InjectMocks
    private FinancialAnalyticsService financialAnalyticsService;

    @Test
    void getByPeriodType_returnsMappedResponses() {
        FinancialAnalytics entity = new FinancialAnalytics(PeriodType.MONTHLY, "May 2026", 10000.0, 7000.0, 3000.0, 20, 5, 500.0, 4.2, LocalDateTime.now());
        entity.setId(1L);

        when(financialAnalyticsRepository.findByPeriodType(PeriodType.MONTHLY)).thenReturn(List.of(entity));

        var responses = financialAnalyticsService.getByPeriodType("MONTHLY");

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getPeriodLabel()).isEqualTo("May 2026");
    }

    @Test
    void getById_whenMissing_throws() {
        when(financialAnalyticsRepository.findById(5L)).thenReturn(Optional.empty());

        var exception = assertThrows(RuntimeException.class, () -> financialAnalyticsService.getById(5L));

        assertThat(exception).hasMessageContaining("Financial analytics not found");
    }
}
