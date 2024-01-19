package com.example.demo.dao;

import com.example.demo.dto.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatsRepository extends JpaRepository<Chat, UUID> {
    List<Chat> findAllByUserId(UUID userId);
    void deleteByIdAndUserId(UUID id, UUID userId);
    @Query("SELECT c FROM Chat c WHERE c.chatName LIKE %?1% AND c.userId=?2 ORDER BY c.lastUpdateDate")
    List<Chat> findChatsByNameAndUserId(String name, UUID userId);
    Optional<Chat> findByChatName(String chatName);
}
