package com.example.ecommerce.controller;

import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.model.UserModel;
import com.example.ecommerce.service.UserService;

@RestController
@Tag(name = "UserController", description = "User Management")
public class UserController {

    private UserService userService;

    // Add your endpoints here
    @GetMapping("")
    @Operation(summary = "Home", description = "Welcome to the E-commerce Application")
    @ApiResponse(responseCode = "200", description = "Welcome message")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    public String home() {
        return "Welcome to the E-commerce Application!";
    }

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
}
