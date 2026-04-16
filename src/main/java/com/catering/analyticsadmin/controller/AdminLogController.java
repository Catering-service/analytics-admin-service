package com.catering.analyticsadmin.controller;

import com.catering.analyticsadmin.model.dto.AdminLogResponseDTO;
import com.catering.analyticsadmin.service.AdminLogService;
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
