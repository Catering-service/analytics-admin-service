package com.catering.analyticsadmin.repository;

import com.catering.analyticsadmin.model.entity.OfferType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OfferTypeRepository extends JpaRepository<OfferType, Long> {
    Optional<OfferType> findByName(String name);
}