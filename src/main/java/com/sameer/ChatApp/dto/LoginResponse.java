package com.sameer.ChatApp.dto;

public class LoginResponse {
    private String message;
    private String token;
    private int status;
    private Long userId;

    public LoginResponse(String message, String token, int status, Long userId) {
        this.message = message;
        this.token = token;
        this.status = status;
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
