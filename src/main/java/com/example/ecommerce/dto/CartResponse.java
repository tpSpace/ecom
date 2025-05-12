package com.example.ecommerce.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class CartResponse {
    private UUID id;
    private UUID userId;
    private List<CartItemResponse> items = new ArrayList<>();
    private int itemCount;
    private BigDecimal totalPrice;

    @Data
    public static class CartItemResponse {
        private UUID id;
        private UUID productId;
        private String productName;
        private Integer quantity;
        private Double price;
    }
}