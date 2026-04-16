package com.catering.analyticsadmin.repository;

import com.catering.analyticsadmin.model.entity.AdminLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdminLogRepository extends JpaRepository<AdminLog, Long> {

    // To prevent N+1 happening
    @Query("SELECT al FROM AdminLog al JOIN FETCH al.administrator")
    List<AdminLog> findAllWithAdministrator();

    @Query("SELECT al FROM AdminLog al JOIN FETCH al.administrator WHERE al.id = :id")
    java.util.Optional<AdminLog> findByIdWithAdministrator(Long id);

    @Query("SELECT al FROM AdminLog al JOIN FETCH al.administrator WHERE al.administrator.id = :administratorId")
    List<AdminLog> findByAdministratorIdWithAdministrator(Long administratorId);

    @Query("SELECT al FROM AdminLog al JOIN FETCH al.administrator WHERE al.action = :action")
    List<AdminLog> findByActionWithAdministrator(String action);
}