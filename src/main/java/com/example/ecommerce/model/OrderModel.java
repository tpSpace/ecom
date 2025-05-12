package com.example.ecommerce.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Entity
@Table(name = "orders")
@Data
public class OrderModel {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    private UserModel user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemModel> orderItems = new ArrayList<>();

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "order_status")
    private String orderStatus;

    @Column(name = "shipping_address", nullable = false, length = 500)
    private String shippingAddress;

    @Column(name = "billing_address", nullable = false, length = 500)
    private String billingAddress;

    private double totalAmount;
}
