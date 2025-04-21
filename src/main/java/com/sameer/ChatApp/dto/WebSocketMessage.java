package com.sameer.ChatApp.dto;

public class WebSocketMessage<T> {
    private final T data;
    private final String message;
    private final int status;

    public WebSocketMessage(T data, String message, int status) {
        this.data = data;
        this.message = message;
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}