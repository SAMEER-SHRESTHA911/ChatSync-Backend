package com.sameer.ChatApp.controller;

import com.sameer.ChatApp.dto.ChatMessage;
import com.sameer.ChatApp.dto.WebSocketActionRequest;
import com.sameer.ChatApp.model.User;
import com.sameer.ChatApp.repository.UserRepository;
import com.sameer.ChatApp.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class WebSocketController {
    private final WebSocketService webSocketService;
    private final UserRepository userRepository;

    @Autowired
    public WebSocketController(WebSocketService webSocketService, UserRepository userRepository) {
        this.webSocketService = webSocketService;
        this.userRepository = userRepository;
    }

    @MessageMapping("/send_message/{roomId}")
    public void sendMessage(@DestinationVariable Long roomId, ChatMessage message) {
        User user = userRepository.findById(message.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + message.getUserId()));
        System.out.println("Received send_message request for room: " + roomId + ", message: " + message.getMessage() + ", userId: " + message.getUserId());
        webSocketService.handleSendMessage(roomId, user.getId(), message.getMessage(), LocalDateTime.now().toString());
    }

    @MessageMapping("/join_room/{roomId}")
    public void joinRoom(@DestinationVariable Long roomId, WebSocketActionRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + request.getUserId()));
        System.out.println("Received join_room request for room: " + roomId + ", userId: " + request.getUserId());
        webSocketService.handleJoinRoom(roomId, user.getUsername());
    }

    @MessageMapping("/leave_room/{roomId}")
    public void leaveRoom(@DestinationVariable Long roomId, WebSocketActionRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + request.getUserId()));
        System.out.println("Received leave_room request for room: " + roomId + ", userId: " + request.getUserId());
        webSocketService.handleLeaveRoom(roomId, user.getUsername());
    }
}