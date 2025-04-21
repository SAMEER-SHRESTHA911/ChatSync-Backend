package com.sameer.ChatApp.controller;

import com.sameer.ChatApp.dto.WebSocketActionRequest;
import com.sameer.ChatApp.dto.WebSocketMessageRequest;
import com.sameer.ChatApp.service.MessageService;
import com.sameer.ChatApp.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    private final WebSocketService webSocketService;
    private  final MessageService messageService;

    @Autowired
    public WebSocketController(
            WebSocketService webSocketService,
            MessageService messageService
    ) {
        this.webSocketService = webSocketService;
        this.messageService = messageService;
    }

    @MessageMapping("/join_room/{roomId}")
    public void joinRoom(@DestinationVariable Long roomId, WebSocketActionRequest request){
        webSocketService.handleJoinRoom(roomId, request.getUsername());
    }

    @MessageMapping("/leave_room/{roomId}")
    public void leaveRoom(@DestinationVariable Long roomId, WebSocketActionRequest request){
        webSocketService.handleLeaveRoom(roomId, request.getUsername());
    }

    @MessageMapping("/send_message/{roomId}")
    public void sendMessage(@DestinationVariable Long roomId, WebSocketMessageRequest request){
        messageService.saveMessage(roomId, request.getUserId(), request.getContent());
        webSocketService. handleSendMessage(roomId, request.getUsername(), request.getContent());
    }
}
