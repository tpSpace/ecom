package com.example.ecommerce.service;

import com.example.ecommerce.model.UserModel;
import com.example.ecommerce.repository.UserRepository;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

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

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}