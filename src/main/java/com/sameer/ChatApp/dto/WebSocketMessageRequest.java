package com.sameer.ChatApp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class WebSocketMessageRequest {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Content is required")
    private String content;

    @NotNull(message = "User ID is required")
    private Long userId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}