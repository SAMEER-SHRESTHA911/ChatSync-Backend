package com.sameer.ChatApp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class WebSocketMessageRequest {

    @NotBlank(message = "Message is required")
    private String message;

    @NotNull(message = "Room ID is required")
    private Long roomId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }
}