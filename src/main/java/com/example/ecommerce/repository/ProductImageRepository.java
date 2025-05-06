package com.example.ecommerce.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecommerce.model.ProductImageModel;

public interface ProductImageRepository extends JpaRepository<ProductImageModel, UUID> {
    // Add any custom query methods if needed
    // For example, find product image by product ID
    List<ProductImageModel> findByProductId(UUID productId);

}
