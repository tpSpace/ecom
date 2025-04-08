package com.example.ecommerce.repository;

import com.example.ecommerce.model.UserModel;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    
    public UserModel findByUsername(String username);
    public UserModel findByEmail(String email);
    
    @Query("SELECT u FROM UserModel u")
    public List<UserModel> findAllUsers();

    @Query("SELECT u FROM UserModel u WHERE u.id = ?1")
    public UserModel findUserById(Long id);
}