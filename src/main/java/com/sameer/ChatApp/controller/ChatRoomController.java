package com.sameer.ChatApp.controller;

import com.sameer.ChatApp.dto.*;
import com.sameer.ChatApp.model.ChatRoom;
import com.sameer.ChatApp.model.Message;
import com.sameer.ChatApp.model.RoomMembership;
import com.sameer.ChatApp.model.User;
import com.sameer.ChatApp.repository.RoomMembershipRepository;
import com.sameer.ChatApp.service.ChatRoomService;
import com.sameer.ChatApp.service.MessageService;
import com.sameer.ChatApp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rooms")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final UserService userService;
    private final MessageService messageService;
    private final RoomMembershipRepository roomMembershipRepository;

    @Autowired
    public ChatRoomController(ChatRoomService chatRoomService, UserService userService, MessageService messageService, RoomMembershipRepository roomMembershipRepository) {
        this.chatRoomService = chatRoomService;
        this.userService=userService;
        this.messageService = messageService;
        this.roomMembershipRepository = roomMembershipRepository;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ChatRoomDTO>>> getRooms() {
        List<ChatRoomDTO> rooms = chatRoomService.listRooms();
        return ResponseEntity.ok(new ApiResponse<>(
                rooms, "Rooms retrieved successfully", HttpStatus.OK.value()
        ));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ChatRoomDTO>> createRoom(
            @Valid @RequestBody CreateRoomRequest request
    ) {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Long creatorId = userService.findUserByEmail(email).getId();
            ChatRoomDTO room = chatRoomService.createRoom(request.getRoomName(), creatorId);
            return ResponseEntity.ok(new ApiResponse<>(
                    room, "Room created successfully", HttpStatus.OK.value())
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(
                            null, e.getMessage(), HttpStatus.BAD_REQUEST.value())
                    );
        }
    }

    @GetMapping("/{roomId}/join")
    public ResponseEntity<ApiResponse<String>> joinRoom(
            @PathVariable Long roomId,
            @RequestParam boolean join) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Long userId = extractUserIdFromAuthentication(authentication);
            if (join) {
                chatRoomService.joinRoom(roomId, userId);
                return ResponseEntity.ok(new ApiResponse<>("Successfully joined room", null, 200));
            } else {
                chatRoomService.leaveRoom(roomId, userId);
                return ResponseEntity.ok(new ApiResponse<>("Successfully left room", null, 200));
            }
        }catch(IllegalArgumentException | IllegalStateException e){
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(
                            null, e.getMessage(), HttpStatus.BAD_REQUEST.value())
                    );
        }
    }

    @GetMapping("/{roomId}/messages")
    public ResponseEntity<ApiResponse<List<MessageDTO>>> getMessages(@PathVariable Long roomId) {
        try {
            // Get messages for the room
            List<Message> messages = messageService.getMessageHistory(roomId);

            // Convert to DTOs
            List<MessageDTO> messageDTOs = messages.stream()
                    .map(message -> new MessageDTO(
                            message.getId(),
                            message.getContent(),
                            message.getTimestamp(),
                            new UserDTO(message.getUser().getId(), message.getUser().getUsername())
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new ApiResponse<>(
                    messageDTOs, "Messages retrieved successfully", HttpStatus.OK.value()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(
                            null, e.getMessage(), HttpStatus.BAD_REQUEST.value()
                    ));
        }
    }

    @GetMapping("/{roomId}/participants")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getParticipants(@PathVariable Long roomId) {
        try {
            // Get room memberships
            List<RoomMembership> memberships = roomMembershipRepository.findByRoomId(roomId);

            // Convert to user DTOs
            List<UserDTO> participantDTOs = memberships.stream()
                    .map(membership -> new UserDTO(
                            membership.getUser().getId(),
                            membership.getUser().getUsername()
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new ApiResponse<>(
                    participantDTOs, "Participants retrieved successfully", HttpStatus.OK.value()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(
                            null, e.getMessage(), HttpStatus.BAD_REQUEST.value()
                    ));
        }
    }

    private Long extractUserIdFromAuthentication(Authentication authentication) {
        String email = authentication.getName();
        User user = userService.findUserByEmail(email);
        return user.getId();
    }

}
