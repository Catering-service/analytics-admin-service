package com.catering.analyticsadmin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "offer_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfferType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "offerType", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PartnerOffer> partnerOffers = new ArrayList<>();
}