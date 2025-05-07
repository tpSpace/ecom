package com.example.ecommerce.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class RatingResponse {
    private UUID id;
    private UUID productId;
    private String productName;
    private String productImageUrl;
    private String username;
    private Integer score;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}