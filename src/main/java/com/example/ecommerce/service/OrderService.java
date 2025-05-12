package com.example.ecommerce.service;

import java.time.LocalDate;
import java.util.stream.Collectors;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.ecommerce.enums.OrderStatus;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.model.CartModel;
import com.example.ecommerce.model.OrderItemModel;
import com.example.ecommerce.model.OrderModel;
import com.example.ecommerce.repository.CartItemRepository;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    /**
     * Create an order from the given cart:
     * - copies each CartItem → OrderItem
     * - sets initial status to PENDING
     * - deletes all cart items afterwards
     */
    @Transactional
    public OrderModel createOrderFromCart(CartModel cart) {
        OrderModel order = new OrderModel();
        order.setUser(cart.getUser());
               
        order.setCreatedAt(LocalDate.now());
        order.setOrderStatus(OrderStatus.PENDING.name());

        List<OrderItemModel> items = cart.getItems().stream()
                .map(ci -> {
                    OrderItemModel oi = new OrderItemModel();
                    oi.setOrder(order);
                    oi.setProduct(ci.getProduct());
                    oi.setQuantity(ci.getQuantity());
                    oi.setPrice(ci.getProduct().getPrice());
                    return oi;
                })
                .collect(Collectors.toList());

        order.setOrderItems(items);
        OrderModel savedOrder = orderRepository.save(order);

        // remove all items from the cart
        cartItemRepository.deleteByCart_Id(cart.getId());

        return savedOrder;
    }

    public Page<OrderModel> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public Optional<OrderModel> getOrderById(UUID id) {
        return orderRepository.findById(id);
    }

    public Page<OrderModel> getOrdersByUser(String userId, Pageable pageable) {
        return orderRepository.findAllByUserId(userId, pageable);
    }

    public Page<OrderModel> getOrdersByStatus(String status, Pageable pageable) {
        return orderRepository.findAllByOrderStatus(status, pageable);
    }

    public OrderModel createOrder(OrderModel order) {
        return orderRepository.save(order);
    }

    public OrderModel updateOrderStatus(UUID id, String status) {
        OrderModel order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
        order.setOrderStatus(status);
        return orderRepository.save(order);
    }

    public void deleteOrder(UUID id) {
        orderRepository.deleteById(id);
    }

    public OrderModel updateOrder(UUID id, OrderModel order) {
        OrderModel existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
        existingOrder.setCreatedAt(order.getCreatedAt());
        existingOrder.setOrderStatus(order.getOrderStatus());
        existingOrder.setUser(order.getUser());
        return orderRepository.save(existingOrder);
    }

}
