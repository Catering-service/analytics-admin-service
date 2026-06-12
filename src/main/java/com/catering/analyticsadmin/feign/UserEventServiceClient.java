package com.catering.analyticsadmin.feign;

import com.catering.analyticsadmin.model.dto.external.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * Feign client for user-event-service.
 * Uses Eureka service discovery — no hardcoded URLs.
 */
@FeignClient(name = "user-event-service")
public interface UserEventServiceClient {

    // --- Events ---

    @GetMapping("/api/events")
    List<UserEventDTO> getAllEvents();

    @GetMapping("/api/events/stats")
    UserEventStatsDTO getEventStats();

    @GetMapping("/api/events/client/{clientId}")
    List<UserEventDTO> getEventsByClient(@PathVariable Long clientId);

    // --- Employees ---

    @GetMapping("/api/employees")
    List<UserEmployeeDTO> getAllEmployees();

    @GetMapping("/api/employees/{employeeId}/average-rating")
    Map<String, Object> getEmployeeAverageRating(@PathVariable Long employeeId);

    // --- Clients ---

    @GetMapping("/api/clients")
    List<UserClientDTO> getAllClients();

    // --- Tickets ---

    @GetMapping("/api/tickets")
    List<UserTicketDTO> getAllTickets();

    @GetMapping("/api/tickets/employee/{employeeId}")
    List<UserTicketDTO> getTicketsByEmployee(@PathVariable Long employeeId);

    // --- Menus ---

    @GetMapping("/api/menus")
    List<UserMenuDTO> getAllMenus();

    @GetMapping("/api/menus/event/{eventId}")
    UserMenuDTO getMenuByEvent(@PathVariable Long eventId);
}
