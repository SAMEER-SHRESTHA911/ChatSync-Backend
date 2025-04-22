package com.sameer.ChatApp.service;

import com.sameer.ChatApp.dto.ChatRoomDTO;
import com.sameer.ChatApp.dto.UserDTO;
import com.sameer.ChatApp.model.ChatRoom;
import com.sameer.ChatApp.model.RoomMembership;
import com.sameer.ChatApp.model.User;
import com.sameer.ChatApp.repository.ChatRoomRepository;
import com.sameer.ChatApp.repository.RoomMembershipRepository;
import com.sameer.ChatApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final RoomMembershipRepository roomMembershipRepository;
    private final UserRepository userRepository;

    @Autowired
    public ChatRoomService(
            ChatRoomRepository chatRoomRepository,
            RoomMembershipRepository roomMembershipRepository,
            UserRepository userRepository
    ){
        this.chatRoomRepository = chatRoomRepository;
        this.roomMembershipRepository = roomMembershipRepository;
        this.userRepository = userRepository;
    }

    public ChatRoomDTO createRoom(String name, Long creatorId){
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: "+ creatorId));

        ChatRoom room = new ChatRoom();
        room.setName(name);
        room.setCreatedBy(creator);
        ChatRoom savedRoom = chatRoomRepository.save(room);

        //Add creator as a member wala feature
        RoomMembership membership = new RoomMembership();
        membership.setUser(creator);
        membership.setRoom(savedRoom);
        roomMembershipRepository.save(membership);

        UserDTO userDTO = new UserDTO(creator.getId(), creator.getUsername());
        return new ChatRoomDTO(savedRoom.getId(), savedRoom.getName(), savedRoom.getCreatedAt(), userDTO);
    }

    public List<ChatRoomDTO> listRooms(){
        return chatRoomRepository.findAll().stream()
                .map(room -> new ChatRoomDTO(
                        room.getId(),
                        room.getName(),
                        room.getCreatedAt(),
                        new UserDTO(room.getCreatedBy().getId(), room.getCreatedBy().getUsername())
                ))
                .collect(Collectors.toList());
    }

    public void joinRoom(Long roomId, Long userId){
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with ID :"+ roomId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID :"+userId));

        Optional<RoomMembership> existingMembership =
                roomMembershipRepository.findByUserIdAndRoomId(userId, roomId);

        if(existingMembership.isPresent()){
            throw new IllegalStateException("User is already a member of this room");
        }

        RoomMembership membership = new RoomMembership();
        membership.setUser(user);
        membership.setRoom(room);
        roomMembershipRepository.save(membership);
    }

    public void leaveRoom(Long roomId, Long userId){
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with ID: " + roomId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        RoomMembership roomMembership = roomMembershipRepository.findByUserIdAndRoomId(userId, roomId)
                .orElseThrow(() -> new IllegalArgumentException("User is not a member of this room"));
        roomMembershipRepository.delete(roomMembership);
    }
}
