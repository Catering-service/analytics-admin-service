package com.catering.analyticsadmin.service;

import com.catering.analyticsadmin.model.dto.AdminLogResponseDTO;
import com.catering.analyticsadmin.model.entity.AdminLog;
import com.catering.analyticsadmin.repository.AdminLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminLogService {
    private final AdminLogRepository adminLogRepository;

    public AdminLogService(AdminLogRepository adminLogRepository) {
        this.adminLogRepository = adminLogRepository;
    }

    public List<AdminLogResponseDTO> getAll() {
        return adminLogRepository.findAllWithAdministrator()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public Page<AdminLogResponseDTO> getAll(Pageable pageable) {
        return adminLogRepository.findAllWithAdministrator(pageable)
                .map(this::mapToResponse);
    }

    public AdminLogResponseDTO getById(Long id) {
        AdminLog adminLog = adminLogRepository.findByIdWithAdministrator(id)
                .orElseThrow(() -> new RuntimeException("Admin log not found"));
        return mapToResponse(adminLog);
    }

    public List<AdminLogResponseDTO> getByAdministratorId(Long administratorId) {
        return adminLogRepository.findByAdministratorIdWithAdministrator(administratorId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<AdminLogResponseDTO> getByAction(String action) {
        return adminLogRepository.findByActionWithAdministrator(action)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private AdminLogResponseDTO mapToResponse(AdminLog adminLog) {
        AdminLogResponseDTO response = new AdminLogResponseDTO();
        response.setId(adminLog.getId());
        response.setAction(adminLog.getAction());
        response.setTimestamp(adminLog.getTimestamp());
        response.setDetails(adminLog.getDetails());

        if (adminLog.getAdministrator() != null) {
            response.setAdministratorId(adminLog.getAdministrator().getId());
            response.setAdministratorUsername(adminLog.getAdministrator().getUsername());
        }

        return response;
    }
}
