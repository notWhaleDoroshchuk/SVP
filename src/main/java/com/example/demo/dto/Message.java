package com.example.demo.dto;


import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "messages")
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
}
