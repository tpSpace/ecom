package com.example.ecommerce.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "product_images")
@Data
public class ProductImageModel {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", name = "id", updatable = false, nullable = false, unique = true)
    private UUID id;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @ManyToOne(optional = false, targetEntity = ProductModel.class)
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private ProductModel productId;

}
