package com.example.ecommerce.service;

import com.example.ecommerce.model.UserModel;
import com.example.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    
    // Constructor injection instead of field injection
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    /**
     * Retrieves all users from the database
     * @return List of all users
     */
    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }
    
    /**
     * Creates a new user or updates an existing one
     * @param user the user to save
     * @return the saved user with generated ID
     */
    public UserModel createUser(UserModel user) {
        return userRepository.save(user);
    }

    /**
     * Finds a user by their ID
     * @param id the UUID of the user
     * @return an Optional containing the user if found
     */
    public Optional<UserModel> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    /**
     * Deletes a user by their ID
     * @param id the UUID of the user to delete
     */
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }
        
    /**
     * Finds a user by their email
     * @param email the email to search for
     * @return the found user or null
     */
    public UserModel getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}