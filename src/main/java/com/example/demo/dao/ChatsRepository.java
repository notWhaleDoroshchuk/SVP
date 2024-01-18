package com.example.demo.dao;

import com.example.demo.dto.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChatsRepository extends JpaRepository<Chat, UUID> {
}
