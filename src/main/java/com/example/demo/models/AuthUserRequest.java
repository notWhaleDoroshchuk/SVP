package com.example.demo.models;

import lombok.Data;

@Data
public class AuthUserRequest {
    private String login;
    private String password;
}
