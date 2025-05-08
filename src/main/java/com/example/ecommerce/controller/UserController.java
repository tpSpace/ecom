package com.example.ecommerce.controller;

import java.util.UUID;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve a list of all users")
    @ApiResponse(responseCode = "200", description = "List of users returned successfully")
    @ApiResponse(responseCode = "403", description = "Access denied")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<UserModel>> getAllUsers(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Request to retrieve all users, page: {}, size: {}", page, size);
        return ResponseEntity.ok(userService.getAllUsers(page, size));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieve a user by their ID")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<UserModel> getUserById(@PathVariable UUID id) {
        log.info("Request to retrieve user by ID: {}", id);
        return ResponseEntity.ok(userService.getUserByIdOrThrow(id));
    }

    @PostMapping
    @Operation(summary = "Create user", description = "Create a new user in the system")
    @ApiResponse(responseCode = "201", description = "User created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid user data")
    public ResponseEntity<UserModel> createUser(@Valid @RequestBody UserModel user) {
        log.info("Request to create a new user: {}", user.getFirstName());
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
        log.info("Request to update user with ID: {}", id);
        UserModel updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping
    @Operation(summary = "Delete user", description = "Delete a user by their ID")
    @ApiResponse(responseCode = "204", description = "User deleted successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<Void> deleteUser(@RequestParam UUID id) {
        log.info("Request to delete user with ID: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/role")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Change user role", description = "Change the role of a user")
    @ApiResponse(responseCode = "200", description = "User role changed successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "400", description = "Invalid role")
    public ResponseEntity<UserModel> changeUserRole(
            @RequestParam("id") UUID id,
            @RequestParam("role") String roleName) {
        log.info("Request to change role for user ID: {} to role: {}", id, roleName);
        UserModel updatedUser = userService.changeUserRole(id, roleName);
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping()
    @Operation(summary = "Update user details", description = "Update user details")
    @ApiResponse(responseCode = "200", description = "User details updated successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "400", description = "Invalid user data")
    public ResponseEntity<UserModel> updateUserDetails(
            @RequestParam UUID id,
            @Valid @RequestBody UserModel user) {
        log.info("Request to update details for user ID: {}", id);
        UserModel updatedUser = userService.updateUserDetails(id, user);
        return ResponseEntity.ok(updatedUser);
    }
}