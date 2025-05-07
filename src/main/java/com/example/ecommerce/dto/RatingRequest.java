package com.example.ecommerce.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class RatingRequest {
    @NotNull(message = "Product ID is required")
    private UUID productId;

    @NotNull(message = "Rating score is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot be more than 5")
    private Integer score;

    @Size(max = 1000, message = "Comment cannot exceed 1000 characters")
    private String comment;
}