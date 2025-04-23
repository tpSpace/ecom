package com.example.ecommerce.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.ecommerce.model.CartModel;
import com.example.ecommerce.model.OrderModel;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/orders")
@Tag(name = "Order Controller", description = "APIs for managing orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @GetMapping
    @Operation(summary = "Get all orders", description = "Retrieves a paginated list of all orders")
    @ApiResponse(responseCode = "200", description = "List of orders returned successfully")
    public ResponseEntity<Page<OrderModel>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "orderDate") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        return ResponseEntity.ok(orderService.getAllOrders(pageable));
    }

    @GetMapping("/by-id")
    @Operation(summary = "Get order by ID", description = "Retrieves an order by its ID")
    @ApiResponse(responseCode = "200", description = "Order found")
    @ApiResponse(responseCode = "404", description = "Order not found")
    public ResponseEntity<OrderModel> getOrderById(@RequestParam UUID id) {
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-user")
    @Operation(summary = "Get orders by user", description = "Retrieves a paginated list of orders for a user")
    @ApiResponse(responseCode = "200", description = "List of orders returned successfully")
    public ResponseEntity<Page<OrderModel>> getOrdersByUser(
            @RequestParam UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(orderService.getOrdersByUser(userId, pageable));
    }

    @GetMapping("/by-status")
    @Operation(summary = "Get orders by status", description = "Retrieves a paginated list of orders by status")
    @ApiResponse(responseCode = "200", description = "List of orders returned successfully")
    public ResponseEntity<Page<OrderModel>> getOrdersByStatus(
            @RequestParam String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(orderService.getOrdersByStatus(status, pageable));
    }

    @PostMapping("/from-cart")
    @Operation(summary = "Create order from cart", description = "Creates an order from the user's cart")
    @ApiResponse(responseCode = "201", description = "Order created successfully")
    @ApiResponse(responseCode = "404", description = "Cart not found")
    public ResponseEntity<?> createOrderFromCart(@RequestParam UUID cartId) {
        Optional<CartModel> optionalCart = Optional.ofNullable(cartService.getCartById(cartId));

        return optionalCart.map(cart -> {
            OrderModel createdOrder = orderService.createOrderFromCart(cart);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
        }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create order", description = "Creates a new order")
    @ApiResponse(responseCode = "201", description = "Order created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid order data")
    public ResponseEntity<OrderModel> createOrder(@Valid @RequestBody OrderModel order) {
        OrderModel createdOrder = orderService.createOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @PutMapping
    @Operation(summary = "Update order", description = "Updates an existing order")
    @ApiResponse(responseCode = "200", description = "Order updated successfully")
    @ApiResponse(responseCode = "404", description = "Order not found")
    public ResponseEntity<OrderModel> updateOrder(
            @RequestParam UUID id,
            @Valid @RequestBody OrderModel order) {

        return orderService.getOrderById(id)
                .map(existingOrder -> {
                    order.setId(id);
                    OrderModel updatedOrder = orderService.updateOrder(id, order);
                    return ResponseEntity.ok(updatedOrder);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/status")
    @Operation(summary = "Update order status", description = "Updates the status of an order")
    @ApiResponse(responseCode = "200", description = "Order status updated successfully")
    @ApiResponse(responseCode = "404", description = "Order not found")
    public ResponseEntity<OrderModel> updateOrderStatus(
            @RequestParam UUID id,
            @RequestParam String status) {

        return orderService.getOrderById(id)
                .map(existingOrder -> {
                    OrderModel updatedOrder = orderService.updateOrderStatus(id, status);
                    return ResponseEntity.ok(updatedOrder);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    @Operation(summary = "Delete order", description = "Deletes an order by its ID")
    @ApiResponse(responseCode = "204", description = "Order deleted successfully")
    @ApiResponse(responseCode = "404", description = "Order not found")
    public ResponseEntity<Void> deleteOrder(@RequestParam UUID id) {
        return orderService.getOrderById(id)
                .map(order -> {
                    orderService.deleteOrder(id);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}