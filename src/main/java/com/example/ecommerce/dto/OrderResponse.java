package com.example.ecommerce.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class OrderResponse {
    private UUID id;
    private UUID userId;
    private List<OrderItemResponse> items;
    private BigDecimal totalAmount;
    private String status;
    private LocalDateTime orderDate;
    private String shippingAddress;

}
