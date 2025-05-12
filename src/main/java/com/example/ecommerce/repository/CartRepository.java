package com.example.ecommerce.repository;

import java.util.UUID;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecommerce.model.CartModel;

public interface  CartRepository extends JpaRepository<CartModel, UUID> {
    // Add any custom query methods if needed
    // For example, find cart by user ID
    Page<CartModel> findByUserId(UUID userId, Pageable pageable);
    Optional<CartModel> findByUserId(UUID userId);
    // find cart by user id and item id
    Optional<CartModel> findByUserIdAndItemsId(UUID userId, UUID itemId);
}
