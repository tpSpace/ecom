package com.example.ecommerce.mapper;

import com.example.ecommerce.dto.OrderResponse;
import com.example.ecommerce.dto.OrderItemResponse;
import com.example.ecommerce.model.OrderItemModel;
import com.example.ecommerce.model.OrderModel;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderResponse toDto(OrderModel o) {
        OrderResponse r = new OrderResponse();
        r.setId(o.getId());
        r.setUserId(o.getUser().getId());
        // convert LocalDate → LocalDateTime at start of day
        r.setOrderDate(o.getCreatedAt().atStartOfDay());
        r.setStatus(o.getOrderStatus());
        r.setShippingAddress(o.getShippingAddress());
        // model.totalAmount is double, DTO expects BigDecimal
        r.setTotalAmount(BigDecimal.valueOf(o.getTotalAmount()));
        // model uses orderItems, not items
        r.setItems(o.getOrderItems().stream()
                .map(i -> toItemDto(i))
                .collect(Collectors.toList()));
        return r;
    }

    private OrderItemResponse toItemDto(OrderItemModel i) {
        OrderItemResponse ir = new OrderItemResponse();
        ir.setId(i.getId());
        ir.setProductId(i.getProduct().getId());
        ir.setProductName(i.getProduct().getName());
        ir.setQuantity(i.getQuantity());
        ir.setPrice(i.getPrice());
        return ir;
    }
}