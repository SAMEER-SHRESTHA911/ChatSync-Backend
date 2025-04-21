package com.sameer.ChatApp.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateRoomRequest {

    @NotBlank(message = "Room name is required")
    private String roomName;

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}