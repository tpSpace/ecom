package com.example.ecommerce.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.model.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, UUID> {
    UserModel findByEmail(String email);

    Page<UserModel> findByFirstNameContainingOrLastNameContaining(String firstName, String lastName, Pageable pageable);

    // OR
    Page<UserModel> findAllByFirstNameContaining(String name, Pageable pageable);

    // OR
    @Query("SELECT u FROM UserModel u WHERE u.firstName LIKE %:name% OR u.lastName LIKE %:name%")
    Page<UserModel> searchByName(@Param("name") String name, Pageable pageable);

    UserModel findByEmailAndPassword(String email, String password);
}