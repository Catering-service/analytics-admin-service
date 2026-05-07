package com.catering.analyticsadmin.controller;

import com.catering.analyticsadmin.model.dto.AdminLogResponseDTO;
import com.catering.analyticsadmin.service.AdminLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin-logs")
public class AdminLogController {
    private final AdminLogService adminLogService;

    public AdminLogController(AdminLogService adminLogService) {
        this.adminLogService = adminLogService;
    }

    @GetMapping
    public List<AdminLogResponseDTO> getAll() {
        return adminLogService.getAll();
    }

    @GetMapping("/paginated")
    public Page<AdminLogResponseDTO> getAllPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return adminLogService.getAll(pageable);
    }

    @GetMapping("/{id}")
    public AdminLogResponseDTO getById(@PathVariable Long id) {
        return adminLogService.getById(id);
    }

    @GetMapping("/administrator/{administratorId}")
    public List<AdminLogResponseDTO> getByAdministratorId(@PathVariable Long administratorId) {
        return adminLogService.getByAdministratorId(administratorId);
    }

    @GetMapping("/action/{action}")
    public List<AdminLogResponseDTO> getByAction(@PathVariable String action) {
        return adminLogService.getByAction(action);
    }
}
