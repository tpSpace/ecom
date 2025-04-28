package com.example.ecommerce.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.ecommerce.model.UserModel;
import com.example.ecommerce.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserModel getUserByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    public UserModel updateUser(UserModel user) {
        return userRepository.save(user);
    }

    public UserModel createUser(UserModel user) {
        return userRepository.save(user);
    }

    public Page<UserModel> getAllUsers(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<UserModel> userPage = userRepository.findAll(pageable);
        return userPage;
    }

    public Optional<UserModel> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    public Optional<UserModel> getUserByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

    public void saveUser(UserModel user) {
        userRepository.save(user);
    }
}