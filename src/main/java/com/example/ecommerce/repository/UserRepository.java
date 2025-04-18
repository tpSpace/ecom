package com.example.ecommerce.repository;

import com.example.ecommerce.model.UserModel;
import java.util.UUID;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserModel, UUID> {
    UserModel findByEmail(String email);
    Page<UserModel> findAll(String name, Pageable pageable);
    Page<UserModel> findByUserRole(String role, Pageable pageable);
    UserModel findByEmailAndPassword(String email, String password);
}