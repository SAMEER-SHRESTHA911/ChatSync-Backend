package com.sameer.ChatApp.controller;

import com.sameer.ChatApp.dto.ApiResponse;
import com.sameer.ChatApp.dto.ChatRoomDTO;
import com.sameer.ChatApp.dto.CreateRoomRequest;
import com.sameer.ChatApp.dto.JoinRoomRequest;
import com.sameer.ChatApp.model.ChatRoom;
import com.sameer.ChatApp.model.User;
import com.sameer.ChatApp.service.ChatRoomService;
import com.sameer.ChatApp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final UserService userService;

    @Autowired
    public ChatRoomController(ChatRoomService chatRoomService, UserService userService) {
        this.chatRoomService = chatRoomService;
        this.userService=userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ChatRoom>>> getRooms() {
        List<ChatRoom> rooms = chatRoomService.listRooms();
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

    @PostMapping("/{roomId}/join")
    public ResponseEntity<ApiResponse<String>> joinRoom(
            @PathVariable Long roomId,
            @Valid @RequestBody JoinRoomRequest request) {
        try {
            chatRoomService.joinRoom(roomId, request.getUserId());
            return ResponseEntity.ok(new ApiResponse<>(
                    null, "User joined room successfully", HttpStatus.OK.value())
            );
        }catch(IllegalArgumentException | IllegalStateException e){
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(
                            null, e.getMessage(), HttpStatus.BAD_REQUEST.value())
                    );
        }
    }

}
