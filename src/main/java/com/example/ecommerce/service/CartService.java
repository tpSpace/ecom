package com.example.ecommerce.service;

import com.example.ecommerce.dto.CartItemRequest;
import com.example.ecommerce.dto.CartResponse;
import java.util.UUID;

public interface CartService {
    /**
     * Get cart for a user by username
     */
    CartResponse getCartByUsername(String username);

    /**
     * Add product to user's cart
     */
    CartResponse addItemToCart(String username, CartItemRequest request);

    /**
     * Update cart item quantity
     */
    CartResponse updateCartItemQuantity(String username, UUID itemId, int quantity);

    /**
     * Remove item from cart
     */
    void removeItemFromCart(String username, UUID itemId);

    /**
     * Clear all items from cart
     */
    void clearCart(String username);

    /**
     * Create order from cart (checkout)
     * 
     * @return ID of the created order
     */
    UUID checkoutCart(String username);
}