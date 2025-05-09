package com.sameer.ChatApp.repository;

import com.sameer.ChatApp.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByRoomId(Long roomId);
    List<Message> findByRoomIdOrderByTimestampAsc(Long roomId);
}
