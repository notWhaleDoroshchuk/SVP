package com.example.demo.dao;

import com.example.demo.dto.Chat;
import com.example.demo.dto.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessagesRepository extends JpaRepository<Message, UUID> {
    List<Message> findAllByChat(Chat chat);
}
