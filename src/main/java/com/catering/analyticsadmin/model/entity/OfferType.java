package com.catering.analyticsadmin.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "offer_types")
public class OfferType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "offerType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PartnerOffer> partnerOffers = new ArrayList<>();

    public OfferType() {
    }

    public OfferType(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OfferType offerType = (OfferType) o;
        return Objects.equals(id, offerType.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "OfferType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", partnerOffers=" + partnerOffers +
                '}';
    }

    public void addPartnerOffer(PartnerOffer offer) {
        partnerOffers.add(offer);
        offer.setOfferType(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PartnerOffer> getPartnerOffers() {
        return partnerOffers;
    }

    public void setPartnerOffers(List<PartnerOffer> partnerOffers) {
        this.partnerOffers = partnerOffers;
    }
}