package com.example.ecommerce.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class AuthResponse {

    private String token;
    private String type = "Bearer";
    private String role;
    private UUID userId;
    private String email;
    private String firstName;
    private String lastName;

    public AuthResponse(String token, String role, UUID userId, String email, String firstName, String lastName) {
        this.token = token;
        this.role = role;
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}