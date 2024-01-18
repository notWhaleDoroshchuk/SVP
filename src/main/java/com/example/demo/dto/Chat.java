package com.example.demo.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "chats")
@AllArgsConstructor
@NoArgsConstructor
public class Chat {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

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
