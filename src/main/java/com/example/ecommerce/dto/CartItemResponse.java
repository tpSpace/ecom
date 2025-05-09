package com.example.ecommerce.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CartItemResponse {
    private UUID id;
    private UUID productId;
    private String productName;
    private String productImage;
    private BigDecimal price;
    private int quantity;
    private BigDecimal subtotal;
}