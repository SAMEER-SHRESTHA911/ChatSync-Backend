package com.sameer.ChatApp.service;

import com.sameer.ChatApp.dto.WebSocketMessage;
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
    // Room ma user track garna
    private final Map<Long, Set<String>> roomUsers = new ConcurrentHashMap<>();

    @Autowired
    public WebSocketService(SimpMessagingTemplate messagingTemplate){
        this.messagingTemplate = messagingTemplate;
    }

    public void handleJoinRoom(Long roomId, String username){
        roomUsers.computeIfAbsent(roomId, k-> new HashSet<>()).add(username);
        WebSocketMessage<String> message = new WebSocketMessage<>(username, "User joined the room", 200);
        messagingTemplate.convertAndSend("/topic/room/" + roomId, message);
    }

    public void handleLeaveRoom(Long roomId, String username){
        roomUsers.computeIfAbsent(roomId, k-> new HashSet<>()).remove(username);
        WebSocketMessage<String> message = new WebSocketMessage<>(username, "User left the room", 200);
        messagingTemplate.convertAndSend("/topic/room/" + roomId, message);
    }

    public void handleSendMessage(Long roomId, String username, String content){
        String messageContent = username + ": " + content;
        WebSocketMessage<String> message = new WebSocketMessage<>(messageContent, "Message sent", 200);
        messagingTemplate.convertAndSend("/topic/room/" + roomId, message);
    }
}
