package com.sameer.ChatApp.dto;

import jakarta.validation.constraints.NotBlank;

public class WebSocketActionRequest {

    @NotBlank(message = "Username is required")
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}