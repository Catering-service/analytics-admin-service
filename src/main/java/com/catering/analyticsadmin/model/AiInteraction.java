package com.catering.analyticsadmin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ai_interactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiInteraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @NotBlank
    @Column(name = "session_id", nullable = false)
    private String sessionId;

    @NotBlank
    @Column(columnDefinition = "TEXT", nullable = false)
    private String question;

    @Column(columnDefinition = "TEXT")
    private String answer;

    @Column(nullable = false)
    private LocalDateTime timestamp;
}