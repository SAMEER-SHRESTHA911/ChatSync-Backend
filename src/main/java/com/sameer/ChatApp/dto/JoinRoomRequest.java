package com.sameer.ChatApp.dto;

import jakarta.validation.constraints.NotNull;

public class JoinRoomRequest {


    public boolean isJoin() {
        return join;
    }

    public void setJoin(boolean join) {
        this.join = join;
    }

    private boolean join;

//    @NotNull(message = "User ID is required")
//    private Long userId;

//    public Long getUserId() {
//        return userId;
//    }
//
//    public void setUserId(Long userId) {
//        this.userId = userId;
//    }
}