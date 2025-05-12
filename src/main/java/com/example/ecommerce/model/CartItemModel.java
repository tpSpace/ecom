package com.example.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "cart_items")
@Data
public class CartItemModel {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cart_id", nullable = false)
    private CartModel cart;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductModel product;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;
}