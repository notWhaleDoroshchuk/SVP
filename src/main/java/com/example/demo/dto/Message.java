package com.example.demo.dto;


import com.example.demo.enums.MessageType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "messages")
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private Chat chat;

    @Column(name = "parent_message_id")
    private UUID parentMessageId;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "text")
    private String text;

    @Column(name = "created")
    private Instant created;

    @Column(name = "updated")
    private Instant updated;

    private MessageType type;
}
