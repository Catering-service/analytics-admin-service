package com.catering.analyticsadmin.repository;

import com.catering.analyticsadmin.model.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdministratorRepository extends JpaRepository<Administrator, Long> {
    Optional<Administrator> findByUsername(String username);
    Optional<Administrator> findByEmail(String email);
}