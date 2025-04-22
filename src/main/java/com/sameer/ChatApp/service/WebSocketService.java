package com.sameer.ChatApp.service;

import com.sameer.ChatApp.dto.WebSocketMessage;
import com.sameer.ChatApp.model.Message;
import com.sameer.ChatApp.model.User;
import com.sameer.ChatApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WebSocketService {
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;
    private final UserRepository userRepository;
    private final Map<Long, Set<String>> roomUsers = new ConcurrentHashMap<>();

    @Autowired
    public WebSocketService(SimpMessagingTemplate messagingTemplate, MessageService messageService, UserRepository userRepository) {
        this.messagingTemplate = messagingTemplate;
        this.messageService = messageService;
        this.userRepository = userRepository;
    }

    public void handleJoinRoom(Long roomId, String username) {
        roomUsers.computeIfAbsent(roomId, k -> new HashSet<>()).add(username);
        WebSocketMessage<String> message = new WebSocketMessage<>(username, "User joined the room", 200);
        messagingTemplate.convertAndSend("/topic/room/" + roomId, message);
    }

    public void handleLeaveRoom(Long roomId, String username) {
        roomUsers.computeIfAbsent(roomId, k -> new HashSet<>()).remove(username);
        WebSocketMessage<String> message = new WebSocketMessage<>(username, "User left the room", 200);
        messagingTemplate.convertAndSend("/topic/room/" + roomId, message);
    }

    public void handleSendMessage(Long roomId, Long userId, String content, String timestamp) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
            Message savedMessage = messageService.saveMessage(roomId, userId, content);
            MessageData messageData = new MessageData(user.getUsername(), content, savedMessage.getTimestamp().toString());
            WebSocketMessage<MessageData> message = new WebSocketMessage<>(messageData, "Message sent", 200);
            messagingTemplate.convertAndSend("/topic/room/" + roomId, message);
        } catch (IllegalArgumentException e) {
            WebSocketMessage<String> errorMessage = new WebSocketMessage<>(null, "Failed to save message: " + e.getMessage(), 400);
            messagingTemplate.convertAndSend("/topic/room/" + roomId, errorMessage);
        }
    }

    public static class MessageData {
        private final String username;
        private final String content;
        private final String timestamp;

        public MessageData(String username, String content, String timestamp) {
            this.username = username;
            this.content = content;
            this.timestamp = timestamp;
        }

        public String getUsername() {
            return username;
        }

        public String getContent() {
            return content;
        }

        public String getTimestamp() {
            return timestamp;
        }
    }
}