package com.catering.analyticsadmin.repository;

import com.catering.analyticsadmin.model.Partner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartnerRepository extends JpaRepository<Partner, Long> {
    Optional<Partner> findByEmail(String email);
}