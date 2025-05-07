package com.example.ecommerce.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class CartResponse {
    private UUID id;
    private String username;
    private List<CartItemDto> items = new ArrayList<>();
    private int itemCount;
    private BigDecimal totalPrice;
}