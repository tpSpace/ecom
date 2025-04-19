package com.example.ecommerce.repository;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ecommerce.model.CartModel;

public interface  CartRepository extends JpaRepository<CartModel, UUID> {
    // Add any custom query methods if needed
    // For example, find cart by user ID
    CartModel findByUser_Id(UUID userId);
    Page<CartModel> findByUser_Id(UUID userId, Pageable pageable);
    @Query("SELECT DISTINCT c FROM CartModel c JOIN c.cartItems ci WHERE ci.product.id = :productId")
    Page<CartModel> findByCartItems_Product_Id(@Param("productId") UUID productId, Pageable pageable);
}
