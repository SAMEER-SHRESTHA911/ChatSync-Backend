package com.sameer.ChatApp.dto;

import java.time.LocalDateTime;

public class ChatRoomDTO {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private UserDTO createdBy;

    public ChatRoomDTO(Long id, String name, LocalDateTime createdAt, UserDTO createdBy) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UserDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserDTO createdBy) {
        this.createdBy = createdBy;
    }
}