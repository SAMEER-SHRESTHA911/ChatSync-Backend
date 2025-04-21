package com.sameer.ChatApp.service;

import com.sameer.ChatApp.model.ChatRoom;
import com.sameer.ChatApp.model.Message;
import com.sameer.ChatApp.model.User;
import com.sameer.ChatApp.repository.ChatRoomRepository;
import com.sameer.ChatApp.repository.MessageRepository;
import com.sameer.ChatApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    ){
        this.messageRepository = messageRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.userRepository = userRepository;
    }

    public Message saveMessage(Long roomId, Long userId, String content){
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with ID: "+roomId));
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(String.format("User not found with ID: %s",userId)));
        Message message = new Message();
        message.setRoom(room);
        message.setUser(user);
        message.setContent(content);
        return messageRepository.save(message);
    }

    public List<Message> getMessageHistory(Long roomId){
        return messageRepository.findByRoomId(roomId);
    }
}
