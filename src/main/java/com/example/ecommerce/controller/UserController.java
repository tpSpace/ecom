package com.example.ecommerce.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.model.UserModel;
import com.example.ecommerce.service.UserService;

@RestController
@Tag(name = "UserController", description = "User Management")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    @Operation(summary = "Get all users", description = "Retrieve a list of all users")
    @ApiResponse(responseCode = "200", description = "List of users")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    public List<UserModel> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieve a user by their ID")
    @ApiResponse(responseCode = "200", description = "User details")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    public Optional<UserModel> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // Create user endpoint
    @PostMapping("/users")
    @Operation(summary = "Create a new user", description = "Create a new user in the system")
    @ApiResponse(responseCode = "201", description = "User created successfully")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    public UserModel createUser(@RequestBody UserModel user) {
        return userService.createUser(user);
    }

    // Delete user endpoint
    @DeleteMapping("/users/{id}")
    @Operation(summary = "Delete a user", description = "Delete a user by their ID")
    @ApiResponse(responseCode = "200", description = "User deleted successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    public void deleteUserByUUID(@PathVariable UUID uuid) {
        userService.deleteUserByUUID(uuid);
    }
}
