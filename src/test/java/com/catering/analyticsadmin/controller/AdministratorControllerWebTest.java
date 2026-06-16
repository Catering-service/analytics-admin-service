package com.catering.analyticsadmin.controller;

import com.catering.analyticsadmin.model.dto.AdministratorCreateDTO;
import com.catering.analyticsadmin.model.dto.AdministratorResponseDTO;
import com.catering.analyticsadmin.model.enums.AdminRole;
import com.catering.analyticsadmin.service.AdministratorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdministratorController.class)
class AdministratorControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AdministratorService administratorService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getAllReturnsAdministratorList() throws Exception {
        AdministratorResponseDTO admin = new AdministratorResponseDTO();
        admin.setId(1L);
        admin.setUsername("admin-user");
        admin.setEmail("admin@example.com");
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setRole(AdminRole.ANALYTICS);
        admin.setActive(true);
        admin.setCreatedAt(LocalDateTime.now());

        when(administratorService.getAll()).thenReturn(List.of(admin));

        mockMvc.perform(get("/api/administrators"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("admin-user"))
                .andExpect(jsonPath("$[0].email").value("admin@example.com"));
    }

    @Test
    void getAllPaginatedReturnsPagedContent() throws Exception {
        AdministratorResponseDTO admin1 = new AdministratorResponseDTO();
        admin1.setId(1L);
        admin1.setUsername("admin-user-1");
        admin1.setEmail("admin1@example.com");
        admin1.setFirstName("Admin");
        admin1.setLastName("User1");
        admin1.setRole(AdminRole.ANALYTICS);
        admin1.setActive(true);
        admin1.setCreatedAt(LocalDateTime.now());

        AdministratorResponseDTO admin2 = new AdministratorResponseDTO();
        admin2.setId(2L);
        admin2.setUsername("admin-user-2");
        admin2.setEmail("admin2@example.com");
        admin2.setFirstName("Admin");
        admin2.setLastName("User2");
        admin2.setRole(AdminRole.BASIC_ADMIN);
        admin2.setActive(true);
        admin2.setCreatedAt(LocalDateTime.now());

        Page<AdministratorResponseDTO> page = new PageImpl<>(List.of(admin1, admin2), PageRequest.of(0, 10), 2);

        when(administratorService.getAll(any())).thenReturn(page);

        mockMvc.perform(get("/api/administrators/paginated")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "id")
                        .param("sortDirection", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].username").value("admin-user-1"))
                .andExpect(jsonPath("$.content[1].username").value("admin-user-2"))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(10));
    }


    @Test
    void createReturnsCreatedStatus() throws Exception {
        // Define the request object
        AdministratorCreateDTO request = new AdministratorCreateDTO();
        request.setUsername("new-admin");
        request.setEmail("new-admin@example.com");
        request.setPassword("securepassword");
        request.setPasswordHash("securepassword");
        request.setFirstName("New");
        request.setLastName("Admin");
        request.setRole(AdminRole.ANALYTICS);

        // Define the response object
        AdministratorResponseDTO response = new AdministratorResponseDTO();
        response.setId(2L);
        response.setUsername("new-admin");
        response.setEmail("new-admin@example.com");
        response.setFirstName("New");
        response.setLastName("Admin");
        response.setRole(AdminRole.ANALYTICS);
        response.setActive(true);
        response.setCreatedAt(LocalDateTime.now());

        // Mock the service call
        when(administratorService.create(any(AdministratorCreateDTO.class))).thenReturn(response);

        // Convert the request to JSON
        String requestJson = objectMapper.writeValueAsString(request);

        // Perform the POST request and validate the response
        mockMvc.perform(post("/api/administrators")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.username").value("new-admin"));
    }
}
