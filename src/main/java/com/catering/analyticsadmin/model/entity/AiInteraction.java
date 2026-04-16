package com.catering.analyticsadmin.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "ai_interactions")
public class AiInteraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Client ID cannot be null")
    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @NotNull(message = "Session ID cannot be null")
    @Column(name = "session_id", nullable = false)
    private String sessionId;

    @NotBlank(message = "Question cannot be blank")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String question;

    @Column(columnDefinition = "TEXT")
    private String answer;

    @NotNull(message = "Timestamp cannot be null")
    @Column(nullable = false)
    private LocalDateTime timestamp;

    public AiInteraction() {}

    public AiInteraction(Long clientId, String sessionId, String question, String answer, LocalDateTime timestamp) {
        this.clientId = clientId;
        this.sessionId = sessionId;
        this.question = question;
        this.answer = answer;
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AiInteraction that = (AiInteraction) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "AiInteraction{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", sessionId='" + sessionId + '\'' +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}