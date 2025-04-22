package com.sameer.ChatApp.repository;

import com.sameer.ChatApp.model.RoomMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomMembershipRepository extends JpaRepository<RoomMembership, Long> {
    Optional<RoomMembership> findByUserIdAndRoomId(Long userId, Long roomId);
    List<RoomMembership> findByRoomId(Long roomId);
}