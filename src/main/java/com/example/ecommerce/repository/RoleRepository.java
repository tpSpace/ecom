package com.example.ecommerce.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.enums.Role;
import com.example.ecommerce.model.RoleModel;

@Repository
public interface RoleRepository extends JpaRepository<RoleModel, UUID> {
    Optional<RoleModel> findByRole(String name);

    // Add this method to find by enum directly
    default Optional<RoleModel> findByRoleEnum(Role role) {
        return findByRole(role.name());
    }
}