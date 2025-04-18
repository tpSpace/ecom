package com.example.ecommerce.service;

import java.time.LocalDate;
import java.util.stream.Collectors;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.ecommerce.enums.OrderStatus;
import com.example.ecommerce.model.CartModel;
import com.example.ecommerce.model.OrderItemModel;
import com.example.ecommerce.model.OrderModel;
import com.example.ecommerce.repository.CartItemRepository;
import com.example.ecommerce.repository.OrderRepository;

import jakarta.transaction.Transactional;

public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartItemRepository cartItemRepository;
    
    /**
     * Create an order from the given cart:
     *  - copies each CartItem → OrderItem
     *  - sets initial status to PENDING
     *  - deletes all cart items afterwards
     */
    @Transactional
    public OrderModel createOrderFromCart(CartModel cart) {
        OrderModel order = new OrderModel();
        order.setUser(cart.getUser());
        order.setOrderDate(LocalDate.now());
        order.setOrderStatus(OrderStatus.PENDING.name());

        List<OrderItemModel> items = cart.getCartItems().stream()
            .map(ci -> {
                OrderItemModel oi = new OrderItemModel();
                oi.setOrder(order);
                oi.setProduct(ci.getProduct());
                oi.setQuantity(ci.getQuantity());
                oi.setPrice(ci.getProduct().getProductPrice());
                return oi;
            })
            .collect(Collectors.toList());

        order.setOrderItems(items);
        OrderModel savedOrder = orderRepository.save(order);

        // remove all items from the cart
        cartItemRepository.deleteByCart_Id(cart.getId());

        return savedOrder;
    }
    
}
