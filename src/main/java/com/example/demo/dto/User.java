package com.example.demo.dto;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue
    private UUID id;
    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "login")
    private String login;

    @Column(name = "last_visited_at")
    private Instant lastVisitedAt;
}
