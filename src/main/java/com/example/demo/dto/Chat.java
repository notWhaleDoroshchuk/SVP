package com.example.demo.dto;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "chats")
public class Chat {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "user_id")
    private UUID user_id;

    @Column(name = "chat_name")
    private String chatName;

    @Column(name = "created")
    private Instant created;

    @Column(name = "last_update_date")
    private Instant lastUpdateDate;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id")
    private List<Message> messages;
}
