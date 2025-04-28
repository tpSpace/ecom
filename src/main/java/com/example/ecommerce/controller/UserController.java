package com.example.ecommerce.controller;

import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.ecommerce.model.UserModel;
import com.example.ecommerce.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve a list of all users")
    @ApiResponse(responseCode = "200", description = "List of users returned successfully")
    @ApiResponse(responseCode = "403", description = "Access denied")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<UserModel>> getAllUsers(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getAllUsers(page, size));
    }

    @GetMapping("/by-id")
    @Operation(summary = "Get user by ID", description = "Retrieve a user by their ID")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<UserModel> getUserById(@RequestParam UUID id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create user", description = "Create a new user in the system")
    @ApiResponse(responseCode = "201", description = "User created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid user data")
    public ResponseEntity<UserModel> createUser(@Valid @RequestBody UserModel user) {
        UserModel createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping
    @Operation(summary = "Update user", description = "Update an existing user")
    @ApiResponse(responseCode = "200", description = "User updated successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "400", description = "Invalid user data")
    public ResponseEntity<UserModel> updateUser(
            @RequestParam UUID id,
            @Valid @RequestBody UserModel user) {

        return userService.getUserById(id)
                .map(existingUser -> {
                    user.setId(id);
                    UserModel updatedUser = userService.createUser(user);
                    return ResponseEntity.ok(updatedUser);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    @Operation(summary = "Delete user", description = "Delete a user by their ID")
    @ApiResponse(responseCode = "204", description = "User deleted successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<Void> deleteUser(@RequestParam UUID id) {
        if (userService.getUserById(id).isPresent()) {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}