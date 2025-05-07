package com.example.ecommerce.dto;

import lombok.Data;

@Data
public class ProfileUpdateRequest {

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String address;

    // email is not included in the update request
    // password is not included in the update request
    private String password;
    private String newPassword;
    private String confirmPassword;

    // Add any other fields you want to update

}