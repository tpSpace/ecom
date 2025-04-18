package com.example.ecommerce.repository;

import java.util.UUID;

import org.springdoc.core.converters.models.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecommerce.model.CartModel;

public interface  CartRepository extends JpaRepository<CartModel, UUID> {
    // Add any custom query methods if needed
    // For example, find cart by user ID
    CartModel findByUserId(UUID userId);
    CartModel findByUserId(UUID userId, Pageable pageable);
    Page<CartModel> findAll(UUID productId, Pageable pageable);
}
