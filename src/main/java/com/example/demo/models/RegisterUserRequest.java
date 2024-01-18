package com.example.demo.models;

import lombok.Data;

@Data
public class RegisterUserRequest {
    private String email;
    private String login;
    private String password;
}
