package com.catering.analyticsadmin.controller;

import com.catering.analyticsadmin.model.dto.AdminLogResponseDTO;
import com.catering.analyticsadmin.service.AdminLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminLogController.class)
class AdminLogControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AdminLogService adminLogService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getAllReturnsAdminLogList() throws Exception {
        AdminLogResponseDTO log = new AdminLogResponseDTO();
        log.setId(1L);
        log.setAction("LOGIN");
        log.setTimestamp(LocalDateTime.now());
        log.setDetails("Admin login successful");
        log.setAdministratorId(10L);
        log.setAdministratorUsername("admin1");

        when(adminLogService.getAll()).thenReturn(List.of(log));

        mockMvc.perform(get("/api/admin-logs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].action").value("LOGIN"))
                .andExpect(jsonPath("$[0].administratorUsername").value("admin1"));
    }

    @Test
    void getByIdReturnsAdminLog() throws Exception {
        AdminLogResponseDTO log = new AdminLogResponseDTO();
        log.setId(5L);
        log.setAction("DELETE");
        log.setTimestamp(LocalDateTime.now());
        log.setDetails("Deleted user");
        log.setAdministratorId(10L);
        log.setAdministratorUsername("admin1");

        when(adminLogService.getById(5L)).thenReturn(log);

        mockMvc.perform(get("/api/admin-logs/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.action").value("DELETE"))
                .andExpect(jsonPath("$.details").value("Deleted user"));
    }

    @Test
    void getAllPaginatedReturnsPagedContent() throws Exception {
        AdminLogResponseDTO log1 = new AdminLogResponseDTO();
        log1.setId(1L);
        log1.setAction("LOGIN");
        log1.setTimestamp(LocalDateTime.now());
        log1.setAdministratorId(10L);
        log1.setAdministratorUsername("admin1");

        AdminLogResponseDTO log2 = new AdminLogResponseDTO();
        log2.setId(2L);
        log2.setAction("UPDATE");
        log2.setTimestamp(LocalDateTime.now());
        log2.setAdministratorId(10L);
        log2.setAdministratorUsername("admin1");

        Page<AdminLogResponseDTO> page = new PageImpl<>(List.of(log1, log2), PageRequest.of(0, 10), 2);

        when(adminLogService.getAll(any())).thenReturn(page);

        mockMvc.perform(get("/api/admin-logs/paginated")
                .param("page", "0")
                .param("size", "10")
                .param("sortBy", "id")
                .param("sortDirection", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].action").value("LOGIN"))
                .andExpect(jsonPath("$.content[1].action").value("UPDATE"))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(10));
    }

    @Test
    void getByAdministratorIdReturnsList() throws Exception {
        AdminLogResponseDTO log = new AdminLogResponseDTO();
        log.setId(6L);
        log.setAction("UPDATE");
        log.setTimestamp(LocalDateTime.now());
        log.setAdministratorId(10L);
        log.setAdministratorUsername("admin1");

        when(adminLogService.getByAdministratorId(10L)).thenReturn(List.of(log));

        mockMvc.perform(get("/api/admin-logs/administrator/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].action").value("UPDATE"))
                .andExpect(jsonPath("$[0].administratorId").value(10));
    }

    @Test
    void getByActionReturnsList() throws Exception {
        AdminLogResponseDTO log = new AdminLogResponseDTO();
        log.setId(7L);
        log.setAction("LOGIN");
        log.setTimestamp(LocalDateTime.now());
        log.setAdministratorId(10L);
        log.setAdministratorUsername("admin1");

        when(adminLogService.getByAction("LOGIN")).thenReturn(List.of(log));

        mockMvc.perform(get("/api/admin-logs/action/LOGIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].action").value("LOGIN"));
    }
}
