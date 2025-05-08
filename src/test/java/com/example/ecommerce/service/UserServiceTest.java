package com.example.ecommerce.service;

import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.model.RoleModel;
import com.example.ecommerce.model.UserModel;
import com.example.ecommerce.repository.RoleRepository;
import com.example.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserService userService;

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
    void createUser_ShouldReturnSavedUser() {
        // Arrange
        when(userRepository.save(any(UserModel.class))).thenReturn(user);

        // Act
        UserModel result = userService.createUser(user);

        // Assert
        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        verify(userRepository).save(user);
    }

    @Test
    void getAllUsers_ShouldReturnPageOfUsers() {
        // Arrange
        PageRequest pageable = PageRequest.of(0, 10);
        Page<UserModel> page = new PageImpl<>(Collections.singletonList(user));
        when(userRepository.findAll(pageable)).thenReturn(page);

        // Act
        Page<UserModel> result = userService.getAllUsers(0, 10);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals(user, result.getContent().get(0));
    }

    @Test
    void getUserByIdOrThrow_ShouldReturnUser() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        UserModel result = userService.getUserByIdOrThrow(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getId());
    }

    @Test
    void getUserByIdOrThrow_WhenNotFound_ShouldThrow() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserByIdOrThrow(userId));
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() {
        // Arrange
        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.save(any(UserModel.class))).thenReturn(user);

        // Act
        UserModel result = userService.updateUser(userId, user);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getId());
        verify(userRepository).save(user);
    }

    @Test
    void deleteUser_WhenUserExists_ShouldDelete() {
        // Arrange
        when(userRepository.existsById(userId)).thenReturn(true);

        // Act
        userService.deleteUser(userId);

        // Assert
        verify(userRepository).deleteById(userId);
    }

    @Test
    void changeUserRole_ShouldUpdateRole() {
        // Arrange
        String roleName = "ADMIN";
        RoleModel role = new RoleModel();
        role.setRole(roleName);
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findByRole(roleName)).thenReturn(Optional.of(role));
        when(userRepository.save(any(UserModel.class))).thenReturn(user);

        // Act
        UserModel result = userService.changeUserRole(userId, roleName);

        // Assert
        assertNotNull(result);
        assertEquals(role, result.getRole());
    }

    @Test
    void changeUserRole_WhenRoleNotFound_ShouldThrow() {
        // Arrange
        String invalidRole = "INVALID";
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findByRole(invalidRole)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.changeUserRole(userId, invalidRole));
    }
} 