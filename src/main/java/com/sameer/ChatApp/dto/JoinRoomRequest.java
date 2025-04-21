package com.sameer.ChatApp.dto;

import jakarta.validation.constraints.NotNull;

public class JoinRoomRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}