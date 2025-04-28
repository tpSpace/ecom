package com.example.ecommerce.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CategoryResponse {
    private UUID id;
    private String name;
    private String description;
    private UUID parentId;
    private List<CategoryResponse> subcategories;
}