package com.example.ecommerce.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class RatingRequest {
    private UUID productId; // Changed to String for compatibility
    private Integer score;
    private String comment;
    private UUID userId;
}
