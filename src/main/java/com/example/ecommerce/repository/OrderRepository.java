package com.example.ecommerce.repository;

import java.util.UUID;

import org.springdoc.core.converters.models.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecommerce.model.OrderModel;

public interface  OrderRepository extends JpaRepository<OrderModel, UUID> {
    /* 
     Status can be one of the following:
        - PENDING
        - SHIPPED
        - DELIVERED
        - CANCELLED
        - RETURNED
     */


    Page<OrderModel> findAllByStatus(String status, Pageable pageable);
    Page<OrderModel> findAllByUserId(UUID userId, Pageable pageable);

    Page<OrderModel> findAllByUserIdAndStatus(UUID userId, String status, Pageable pageable);
}
