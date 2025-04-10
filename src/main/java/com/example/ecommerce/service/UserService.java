package com.example.ecommerce.service;

import com.example.ecommerce.model.UserModel;
import com.example.ecommerce.repository.UserRepository;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Data
@Tag(name = "UserService", description = "User Management")
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }
    
    public UserModel createUser(UserModel user) {
        return userRepository.save(user);
    }

    public Optional<UserModel> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public void deleteUserByUUID(UUID uuid) {
        userRepository.deleteUserByUUID(uuid);
    }
}