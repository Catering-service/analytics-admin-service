package com.catering.analyticsadmin.controller;

import com.catering.analyticsadmin.model.dto.AdministratorCreateDTO;
import com.catering.analyticsadmin.model.dto.AdministratorResponseDTO;
import com.catering.analyticsadmin.model.entity.Administrator;
import com.catering.analyticsadmin.model.enums.AdminRole;
import com.catering.analyticsadmin.service.AdministratorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
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
    void createReturnsCreatedStatus() throws Exception {
        // Define the request object
        AdministratorCreateDTO request = new AdministratorCreateDTO();
        request.setUsername("new-admin");
        request.setEmail("new-admin@example.com");
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
