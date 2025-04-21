package com.example.ecommerce.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.ecommerce.model.CartItemModel;
import com.example.ecommerce.model.CartModel;
import com.example.ecommerce.service.CartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/carts")
@Tag(name = "Cart Controller", description = "APIs for managing shopping carts")
public class CartController {

    @Autowired
    private CartService cartService;
    
    @GetMapping("/by-id")
    @Operation(summary = "Get cart by ID", description = "Retrieves a cart by its ID")
    @ApiResponse(responseCode = "200", description = "Cart found")
    @ApiResponse(responseCode = "404", description = "Cart not found")
    public ResponseEntity<CartModel> getCartById(@RequestParam UUID id) {
        return cartService.getCartById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/by-user")
    @Operation(summary = "Get cart by user ID", description = "Retrieves a user's shopping cart")
    @ApiResponse(responseCode = "200", description = "Cart found")
    @ApiResponse(responseCode = "404", description = "Cart not found for this user")
    public ResponseEntity<CartModel> getCartByUserId(@RequestParam UUID userId) {
        return cartService.getCartByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/items")
    @Operation(summary = "Add item to cart", description = "Adds a product to the user's cart")
    @ApiResponse(responseCode = "200", description = "Item added to cart")
    @ApiResponse(responseCode = "404", description = "Cart or product not found")
    public ResponseEntity<CartModel> addItemToCart(@Valid @RequestBody CartItemModel cartItem) {
        CartModel updatedCart = cartService.addItemToCart(cartItem);
        return ResponseEntity.ok(updatedCart);
    }
    
    @PutMapping("/items")
    @Operation(summary = "Update cart item", description = "Updates quantity of an item in the cart")
    @ApiResponse(responseCode = "200", description = "Cart item updated")
    @ApiResponse(responseCode = "404", description = "Cart or cart item not found")
    public ResponseEntity<CartModel> updateCartItem(
            @RequestParam UUID cartItemId, 
            @RequestParam Integer quantity) {
        CartModel updatedCart = cartService.updateCartItemQuantity(cartItemId, quantity);
        return ResponseEntity.ok(updatedCart);
    }
    
    @DeleteMapping("/items")
    @Operation(summary = "Remove item from cart", description = "Removes an item from the cart")
    @ApiResponse(responseCode = "200", description = "Item removed from cart")
    @ApiResponse(responseCode = "404", description = "Cart or cart item not found")
    public ResponseEntity<CartModel> removeItemFromCart(@RequestParam UUID cartItemId) {
        CartModel updatedCart = cartService.removeItemFromCart(cartItemId);
        return ResponseEntity.ok(updatedCart);
    }
    
    @DeleteMapping("/clear")
    @Operation(summary = "Clear cart", description = "Removes all items from a cart")
    @ApiResponse(responseCode = "204", description = "Cart cleared successfully")
    @ApiResponse(responseCode = "404", description = "Cart not found")
    public ResponseEntity<Void> clearCart(@RequestParam UUID cartId) {
        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/create")
    @Operation(summary = "Create cart", description = "Creates a new cart for a user")
    @ApiResponse(responseCode = "201", description = "Cart created successfully")
    public ResponseEntity<CartModel> createCart(@RequestParam UUID userId) {
        CartModel newCart = cartService.createCartForUser(userId);
        return new ResponseEntity<>(newCart, HttpStatus.CREATED);
    }
}