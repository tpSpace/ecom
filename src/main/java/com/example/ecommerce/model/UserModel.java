package com.example.ecommerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import lombok.Data;

import java.sql.Date;
import java.util.UUID;

import org.springframework.data.annotation.Id;

@Entity
@Table(name = "users")
@Data
public class UserModel {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", name = "id", updatable = false, nullable = false, unique = true)
    private UUID id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "created_at")
    private Date createdAt; 

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "last_login")
    private Date lastLogin;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_admin")
    private Boolean isAdmin;
}
