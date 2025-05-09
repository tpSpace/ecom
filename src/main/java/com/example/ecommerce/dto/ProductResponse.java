package com.example.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class ProductResponse {
    private UUID id;
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
    private String sku;
    private UUID categoryId;
    private String categoryName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Only include when explicitly set
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ProductImageResponse> images;

    public boolean hasImages() {
        return images != null && !images.isEmpty();
    }
}