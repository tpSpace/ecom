package com.example.ecommerce.service;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.model.RoleModel;
import com.example.ecommerce.model.UserModel;
import com.example.ecommerce.repository.RoleRepository;
import com.example.ecommerce.repository.UserRepository;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public UserModel getUserByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            log.error("User deletion failed: User ID {} not found", id);
            throw new ResourceNotFoundException("User", "id", id);
        }
        log.info("Deleting user with ID: {}", id);
        userRepository.deleteById(id);
    }

    public UserModel updateUser(UUID id, UserModel user) {
        if (!userRepository.existsById(id)) {
            log.error("User update failed: User ID {} not found", id);
            throw new ResourceNotFoundException("User", "id", id);
        }
        log.info("Updating user with ID: {}", id);
        user.setId(id);
        return userRepository.save(user);
    }

    public UserModel updateUserDetails(UUID id, UserModel user) {
        if (!userRepository.existsById(id)) {
            log.error("User details update failed: User ID {} not found", id);
            throw new ResourceNotFoundException("User", "id", id);
        }
        log.info("Updating details for user with ID: {}", id);
        user.setId(id);
        return userRepository.save(user);
    }

    public UserModel createUser(UserModel user) {
        log.info("Creating new user: {}", user.getFirstName());
        return userRepository.save(user);
    }

    public Page<UserModel> getAllUsers(int page, int size) {
        log.info("Retrieving all users, page: {}, size: {}", page, size);
        PageRequest pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable);
    }

    public Optional<UserModel> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    public UserModel getUserByIdOrThrow(UUID id) {
        Optional<UserModel> user = userRepository.findById(id);
        if (!user.isPresent()) {
            log.error("User retrieval failed: User ID {} not found", id);
            throw new ResourceNotFoundException("User", "id", id);
        }
        log.info("Retrieved user with ID: {}", id);
        return user.get();
    }

    public Optional<UserModel> getUserByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

    public void saveUser(UserModel user) {
        userRepository.save(user);
    }

    public UserModel changeUserRole(UUID id, String roleName) {
        UserModel user = getUserByIdOrThrow(id);
        RoleModel role = roleRepository.findByRole(roleName)
                .orElseThrow(() -> {
                    log.error("Role change failed: Role {} not found for user ID {}", roleName, id);
                    throw new IllegalArgumentException("Invalid role: " + roleName);
                });
        log.info("Changing role for user ID {} to {}", id, roleName);
        user.setRole(role);
        return userRepository.save(user);
    }
}