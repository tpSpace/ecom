package com.example.ecommerce.model;

import java.sql.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "orders")
@Data
public class OrderModel {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", name = "id", updatable = false, nullable = false, unique = true)
    private UUID id;

    @OneToMany(mappedBy = "user")
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @OneToMany(mappedBy = "product")
    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "order_date", nullable = false)
    private Date orderDate;

    @Column(name = "order_status", nullable = false)
    private String orderStatus;

    @Column(name = "shipping_address", nullable = false)
    private String shippingAddress;

    @Column(name = "billing_address", nullable = false)
    private String billingAddress;

}
