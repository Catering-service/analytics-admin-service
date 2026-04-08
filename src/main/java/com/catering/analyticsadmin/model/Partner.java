package com.catering.analyticsadmin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "partners")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Partner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Email
    @Column(unique = true)
    private String email;

    @Column(name = "contact_person")
    private String contactPerson;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Column(name = "website_url")
    private String websiteUrl;

    @OneToMany(mappedBy = "partner", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PartnerOffer> offers = new ArrayList<>();
}