package com.example.ecommerce.controller;

import com.example.ecommerce.dto.ApiResponse;
import com.example.ecommerce.dto.OrderResponse;
import com.example.ecommerce.mapper.OrderMapper;
import com.example.ecommerce.model.OrderModel;
import com.example.ecommerce.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderMapper orderMapper;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Page<OrderResponse>>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable p = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<OrderResponse> dtoPage = orderService
                .getAllOrders(p)
                .map(orderMapper::toDto);
        return ResponseEntity.ok(ApiResponse.success("Orders retrieved", dtoPage));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> create(@RequestBody OrderModel order) {
        OrderModel o = orderService.createOrder(order);
        return ResponseEntity.status(201)
                .body(ApiResponse.success("Order created", orderMapper.toDto(o)));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<OrderResponse>> update(
            @RequestParam UUID id,
            @RequestBody OrderModel order) {
        OrderModel o = orderService.updateOrder(id, order);
        return ResponseEntity.ok(ApiResponse.success("Order updated", orderMapper.toDto(o)));
    }

    @PutMapping("/status")
    public ResponseEntity<ApiResponse<OrderResponse>> updateStatus(
            @RequestParam UUID id,
            @RequestParam String status) {
        OrderModel o = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("Status updated", orderMapper.toDto(o)));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> delete(@RequestParam UUID id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok(ApiResponse.success("Order deleted", null));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Change order status", description = "Update the status of an order")
    public ResponseEntity<ApiResponse<OrderResponse>> changeOrderStatus(
            @PathVariable("id") UUID id,
            @RequestParam("status") String status) {

        OrderModel updated = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(
                ApiResponse.success("Status updated", orderMapper.toDto(updated)));
    }
}