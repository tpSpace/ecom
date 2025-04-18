package com.example.ecommerce.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.ecommerce.model.CartModel;
import com.example.ecommerce.repository.CartRepository;

public class CartService {
    @Autowired
    private CartRepository cartRepository;

    public CartModel getCartByUserId(UUID userId) {
        return cartRepository.findByUserId(userId);
    }

    public CartModel createCart(CartModel cart) {
        return cartRepository.save(cart);
    }
}
