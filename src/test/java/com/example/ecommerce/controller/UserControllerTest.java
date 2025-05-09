package com.example.ecommerce.controller;

import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.model.UserModel;
import com.example.ecommerce.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserModel user;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = new UserModel();
        user.setId(userId);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
    }

    @Test
    void getAllUsers_ShouldReturnPageOfUsers() {
        // Arrange
        Page<UserModel> page = new PageImpl<>(Collections.singletonList(user));
        when(userService.getAllUsers(0, 10)).thenReturn(page);

        // Act
        ResponseEntity<Page<UserModel>> response = userController.getAllUsers(0, 10);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        Page<UserModel> body = response.getBody();
        assertNotNull(body, "Response body should not be null");
        assertEquals(1, body.getTotalElements());
        verify(userService).getAllUsers(0, 10);
    }

    @Test
    void getUserById_ShouldReturnUser() {
        // Arrange
        when(userService.getUserByIdOrThrow(userId)).thenReturn(user);

        // Act
        ResponseEntity<UserModel> response = userController.getUserById(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void getUserById_WhenNotFound_ShouldThrow() {
        // Arrange
        when(userService.getUserByIdOrThrow(userId))
            .thenThrow(new ResourceNotFoundException("User", "id", userId));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> userController.getUserById(userId));
    }

    @Test
    void createUser_ShouldReturnCreatedUser() {
        // Arrange
        when(userService.createUser(any(UserModel.class))).thenReturn(user);

        // Act
        ResponseEntity<UserModel> response = userController.createUser(user);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() {
        // Arrange
        when(userService.updateUser(userId, user)).thenReturn(user);

        // Act
        ResponseEntity<UserModel> response = userController.updateUser(userId, user);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void deleteUser_ShouldReturnNoContent() {
        // Act
        ResponseEntity<Void> response = userController.deleteUser(userId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService).deleteUser(userId);
    }

    @Test
    void changeUserRole_ShouldReturnUpdatedUser() {
        // Arrange
        String newRole = "ROLE_ADMIN";
        when(userService.changeUserRole(userId, newRole)).thenReturn(user);

        // Act
        ResponseEntity<UserModel> response = userController.changeUserRole(userId, newRole);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }
} 