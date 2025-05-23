package com.sameer.ChatApp.repository;

import com.sameer.ChatApp.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>{
    Optional<ChatRoom> findByName(String name);
}
