package com.sameer.ChatApp.service;

import com.sameer.ChatApp.model.ChatRoom;
import com.sameer.ChatApp.model.Message;
import com.sameer.ChatApp.model.User;
import com.sameer.ChatApp.repository.ChatRoomRepository;
import com.sameer.ChatApp.repository.MessageRepository;
import com.sameer.ChatApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Autowired
    public MessageService(
            MessageRepository messageRepository,
            ChatRoomRepository chatRoomRepository,
            UserRepository userRepository
    ) {
        this.messageRepository = messageRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.userRepository = userRepository;
    }

    public Message saveMessage(Long roomId, Long userId, String content) {
        System.out.println("Saving message: roomId=" + roomId + ", userId=" + userId + ", content=" + content);
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with ID: " + roomId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        Message message = new Message();
        message.setUser(user);
        message.setRoomId(room.getId());
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        Message savedMessage = messageRepository.save(message);
        System.out.println("Message saved: id=" + savedMessage.getId());
        return savedMessage;
    }

    public List<Message> getMessageHistory(Long roomId) {
        return messageRepository.findByRoomIdOrderByTimestampAsc(roomId);
    }
}