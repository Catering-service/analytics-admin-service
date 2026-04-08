package com.catering.analyticsadmin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

@Entity
@Table(name = "partner_offers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartnerOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "partner_id", nullable = false)
    private Partner partner;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "offer_type_id", nullable = false)
    private OfferType offerType;

    @Column(columnDefinition = "TEXT")
    private String description;

    @DecimalMin(value = "0.0", inclusive = true)
    private Double price;
}