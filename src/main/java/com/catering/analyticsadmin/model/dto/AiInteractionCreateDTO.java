package com.catering.analyticsadmin.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AiInteractionCreateDTO {
    @NotNull(message = "Client ID is required")
    private Long clientId;

    @NotBlank(message = "Session ID is required")
    private String sessionId;

    @NotBlank(message = "Question is required")
    private String question;

    private String answer;

    public AiInteractionCreateDTO() {
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
}
