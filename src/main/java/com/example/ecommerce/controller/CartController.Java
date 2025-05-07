package com.example.ecommerce.controller;

import com.example.ecommerce.dto.CartItemRequest;
import com.example.ecommerce.dto.CartResponse;
import com.example.ecommerce.service.CartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cart")
@Tag(name = "CartController", description = "Shopping Cart Management")
public class CartController {
    private static final Logger log = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CartService cartService;

    @GetMapping
    @Operation(summary = "Get user's cart", description = "Retrieves the current user's shopping cart")
    @ApiResponse(responseCode = "200", description = "Cart retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Cart not found")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartResponse> getUserCart(Authentication authentication) {
        log.info("Getting cart for user: {}", authentication.getName());
        CartResponse cart = cartService.getCartByUsername(authentication.getName());
        log.info("Retrieved cart with {} items", cart.getItems().size());
        
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/items")
    @Operation(summary = "Add item to cart", description = "Adds a product to the user's shopping cart")
    @ApiResponse(responseCode = "200", description = "Item added successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartResponse> addItemToCart(
            @Valid @RequestBody CartItemRequest request,
            Authentication authentication) {
        
        log.info("Adding product {} to cart, quantity: {}", request.getProductId(), request.getQuantity());
        CartResponse updatedCart = cartService.addItemToCart(authentication.getName(), request);
        log.info("Product added to cart successfully");
        
        return ResponseEntity.ok(updatedCart);
    }

    @PutMapping("/items/{itemId}")
    @Operation(summary = "Update cart item", description = "Updates the quantity of an item in the cart")
    @ApiResponse(responseCode = "200", description = "Item updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request")
    @ApiResponse(responseCode = "404", description = "Item not found")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartResponse> updateCartItem(
            @PathVariable UUID itemId,
            @RequestParam int quantity,
            Authentication authentication) {
        
        log.info("Updating cart item: {}, new quantity: {}", itemId, quantity);
        CartResponse updatedCart = cartService.updateCartItemQuantity(authentication.getName(), itemId, quantity);
        log.info("Cart item updated successfully");
        
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/items/{itemId}")
    @Operation(summary = "Remove item from cart", description = "Removes an item from the shopping cart")
    @ApiResponse(responseCode = "204", description = "Item removed successfully")
    @ApiResponse(responseCode = "404", description = "Item not found")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> removeCartItem(
            @PathVariable UUID itemId,
            Authentication authentication) {
        
        log.info("Removing item {} from cart", itemId);
        cartService.removeItemFromCart(authentication.getName(), itemId);
        log.info("Item removed from cart successfully");
        
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    @Operation(summary = "Clear cart", description = "Removes all items from the user's shopping cart")
    @ApiResponse(responseCode = "204", description = "Cart cleared successfully")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> clearCart(Authentication authentication) {
        log.info("Clearing cart for user: {}", authentication.getName());
        cartService.clearCart(authentication.getName());
        log.info("Cart cleared successfully");
        
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/checkout")
    @Operation(summary = "Checkout cart", description = "Creates an order from the current cart")
    @ApiResponse(responseCode = "201", description = "Order created successfully")
    @ApiResponse(responseCode = "400", description = "Cart is empty")
    @ApiResponse(responseCode = "409", description = "Insufficient product inventory")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UUID> checkout(Authentication authentication) {
        log.info("Processing checkout for user: {}", authentication.getName());
        UUID orderId = cartService.checkoutCart(authentication.getName());
        log.info("Checkout completed successfully, order created: {}", orderId);
        
        return new ResponseEntity<>(orderId, HttpStatus.CREATED);
    }
}