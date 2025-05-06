package com.example.ecommerce.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.model.CartModel;
import com.example.ecommerce.repository.CartRepository;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    public CartModel getCartByUserId(UUID userId) {
        return cartRepository.findByUser_Id(userId);
    }

    public CartModel getCartById(UUID cartId) {
        return cartRepository.findById(cartId).orElse(null);
    }
    public void deleteCart(UUID cartId) {
        cartRepository.deleteById(cartId);
    }
    public CartModel updateCart(CartModel cart) {
        return cartRepository.save(cart);
    }

    public CartModel createCart(CartModel cart) {
        return cartRepository.save(cart);
    }
}
