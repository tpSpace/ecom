package com.example.ecommerce.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class ProductImageResponse {
    private UUID id;
    private UUID productId;
    private byte[] imageData;
}