package com.example.ecommerce.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "products")
@Data
public class ProductModel {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", name = "id", updatable = false, nullable = false, unique = true)
    private UUID id;

    @NotBlank(message = "Product name cannot be empty")
    @Column(name = "product_name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonBackReference
    private CategoryModel category;

    @NotBlank(message = "Product description cannot be empty")
    @Column(name = "product_description", nullable = false)
    private String description;

    @NotNull(message = "Product price is required")
    @Column(name = "product_price", nullable = false)
    @Min(value = 0, message = "Price cannot be negative")
    private Double price;

    @NotNull(message = "Product quantity cannot be null")
    @Min(value = 0, message = "Quantity cannot be negative")
    @Column(name = "product_quantity", nullable = false)
    private Integer quantity;

    @Column(unique = true)
    private String sku;

    // mappedby mean
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ProductImageModel> productImages = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<RatingModel> ratings;

    @Column(name = "is_featured")
    private boolean isFeatured;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Automatically set the createdAt and updatedAt fields before persisting the
    // entity
    @PrePersist
    private void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    private void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void setImages(byte[] imageData) {
        if (imageData != null) {
            ProductImageModel image = new ProductImageModel();
            image.setImageData(imageData);
            image.setProduct(this);
            this.productImages.add(image);
        }
    }
}
