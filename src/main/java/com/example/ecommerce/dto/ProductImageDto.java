package com.example.ecommerce.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class ProductImageDto {
    private UUID id;
    private UUID productId;
    private byte[] imageData;
}