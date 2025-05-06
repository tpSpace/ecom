package com.example.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class CategoryRequest {
    @NotBlank(message = "Category name is required")
    private String name;

    private String description;

    /**
     * If this is a sub‑category, provide the parent category's UUID,
     * otherwise leave null for a top‑level category.
     */
    private UUID parentId;
}