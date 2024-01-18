package com.example.demo.dao;

import com.example.demo.dto.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatsRepository extends JpaRepository<Chat, UUID> {
    public List<Chat> findAllByUserId(UUID userId);
    void deleteByIdAndUserId(UUID id, UUID userId);
    @Query("SELECT c FROM Chats c WHERE c.name LIKE %?1% AND c.user_id=?2")
    List<Chat> findChatsByName(String name, UUID userId);
}
