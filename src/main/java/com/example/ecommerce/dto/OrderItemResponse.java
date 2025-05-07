package com.example.ecommerce.dto;

import java.util.UUID;

import lombok.Data;

/**
 * OrderItemResponse is a DTO class that represents the response for an order
 * item.
 * It contains the details of the order item such as id, productId, productName,
 * quantity, and price.
 */
@Data
public class OrderItemResponse {

    private UUID id;
    private UUID productId;
    private String productName;
    private Integer quantity;
    private Double price;

}
