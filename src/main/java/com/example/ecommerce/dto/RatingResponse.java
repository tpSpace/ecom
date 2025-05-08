package com.example.ecommerce.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class RatingResponse {
    private UUID id;
    private UUID productId;
    private UUID userId;
    private Integer score; // Changed from rating to score
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}