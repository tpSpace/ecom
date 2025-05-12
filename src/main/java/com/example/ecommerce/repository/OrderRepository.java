package com.example.ecommerce.repository;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.util.Streamable;

import com.example.ecommerce.dto.RatingResponse;
import com.example.ecommerce.model.OrderItemModel;
import com.example.ecommerce.model.OrderModel;

public interface OrderRepository extends JpaRepository<OrderModel, UUID> {
    /*
     * Status can be one of the following:
     * - PENDING
     * - SHIPPED
     * - DELIVERED
     * - CANCELLED
     * - RETURNED
     */
    Page<OrderModel> findAllByOrderStatus(String status, Pageable pageable);

    Page<OrderModel> findAllByUserId(String userId, Pageable pageable);

    Page<OrderModel> findAllByUser_IdAndOrderStatus(UUID userId, String status, Pageable pageable);

    void deleteByUserId(UUID userId);

    void save(OrderItemModel orderItem);

    Streamable<RatingResponse> findByUserId(UUID userId);
}
