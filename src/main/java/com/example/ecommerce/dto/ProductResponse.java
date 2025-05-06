package com.example.ecommerce.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ProductResponse {
    private UUID id;
    private String name;
    private String description;
    private Double price;
    private String category; // category ID as String
    private Integer quantity;
    private List<byte[]> images; // base64‑encoded image data
}