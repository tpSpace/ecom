package com.example.ecommerce.service;

import com.example.ecommerce.dto.CartResponse;
import com.example.ecommerce.model.CartModel;
import com.example.ecommerce.model.CartItemModel;
import com.example.ecommerce.model.ProductModel;
import com.example.ecommerce.model.UserModel;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public CartResponse getCartById(UUID id) {
        CartModel cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "id", id));
        return mapToCartResponse(cart);
    }

    @Transactional
    public CartResponse addItemToCart(UUID id, UUID productId, int quantity) {
        // Get or create cart
        CartModel cart = cartRepository.findById(id)
                .orElseGet(() -> {
                    CartModel newCart = new CartModel();
                    UserModel user = userRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
                    if (user == null) {
                        throw new ResourceNotFoundException("User", "id", id);
                    }
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        // Get product
        ProductModel product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        // Check if item already exists in cart
        CartItemModel existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            // Update quantity if item exists
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            // Add new item if it doesn't exist
            CartItemModel newItem = new CartItemModel();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            cart.getItems().add(newItem);
        }

        cart = cartRepository.save(cart);
        return mapToCartResponse(cart);
    }

    @Transactional
    public CartResponse updateCartItemQuantity(UUID id, UUID itemId, int quantity) {
        CartModel cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "id", id));

        CartItemModel item = cart.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", itemId));

        item.setQuantity(quantity);
        cart = cartRepository.save(cart);
        return mapToCartResponse(cart);
    }

    @Transactional
    public void removeItemFromCart(UUID id, UUID itemId) {
        CartModel cart = cartRepository.findByUserId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "id", id));

        cart.getItems().removeIf(item -> item.getId().equals(itemId));
        cartRepository.save(cart);
    }

    @Transactional
    public void clearCart(UUID id) {
        CartModel cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "id", id));
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Transactional
    public UUID checkoutCart(UUID id) {
        CartModel cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "id", id));
        
        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot checkout empty cart");
        }

        // TODO: Implement order creation logic
        // For now, just return a random UUID
        return UUID.randomUUID();
    }

    private CartResponse mapToCartResponse(CartModel cart) {
        CartResponse response = new CartResponse();
        response.setId(cart.getId());
        response.setUserId(cart.getUser().getId());
        response.setItems(cart.getItems().stream()
                .map(item -> {
                    CartResponse.CartItemResponse itemResponse = new CartResponse.CartItemResponse();
                    itemResponse.setId(item.getId());
                    itemResponse.setProductId(item.getProduct().getId());
                    itemResponse.setProductName(item.getProduct().getName());
                    itemResponse.setQuantity(item.getQuantity());
                    itemResponse.setPrice(item.getProduct().getPrice());
                    return itemResponse;
                })
                .toList());
        return response;
    }
}