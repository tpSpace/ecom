package com.example.ecommerce.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

public class productModel {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", name = "id", updatable = false, nullable = false, unique = true)
    private UUID id;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_description", nullable = false)
    private String productDescription;

    @Column(name = "product_price", nullable = false)
    private double productPrice;

    @Column(name = "average_rating", nullable = false)
    private Double averageRating;

    @Column(name = "product_image", nullable = false)
    private String productImage;

    @Column(name = "product_category", nullable = false)
    private String productCategory;
    
}
