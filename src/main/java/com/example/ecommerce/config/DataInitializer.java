package com.example.ecommerce.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.ecommerce.enums.Role;
import com.example.ecommerce.model.RoleModel;
import com.example.ecommerce.model.UserModel;
import com.example.ecommerce.repository.RoleRepository;
import com.example.ecommerce.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Initialize roles from the Role enum
        for (Role role : Role.values()) {
            if (roleRepository.findByRole(role.name()).isEmpty()) {
                RoleModel roleModel = new RoleModel();
                roleModel.setRole(role.name());
                roleModel.setDescription(getDescriptionForRole(role));
                roleRepository.save(roleModel);
                System.out.println("Created role: " + role.name());
            }
        }

        // Create an admin user if it doesn't exist
        String adminEmail = "admin@admin.com";
        if (userRepository.findByEmail(adminEmail) == null) {
            UserModel adminUser = new UserModel();
            adminUser.setEmail(adminEmail);
            adminUser.setPassword(passwordEncoder.encode("admin1234")); // Encode the password
            adminUser.setRole(roleRepository.findByRole(Role.ADMIN.name()).orElse(null)); // Use Role enum for
            adminUser.setFirstName("lmao");
            adminUser.setLastName("super_lmao");
            adminUser.setPhoneNumber("1234567890");
            adminUser.setAddress("123 Admin St");
            userRepository.save(adminUser);
            System.out.println("Created admin user: " + adminUser.getEmail());
        }
    }

    private String getDescriptionForRole(Role role) {
        switch (role) {
            case ADMIN:
                return "Administrator role with full access";
            case CUSTOMER:
                return "Regular customer role with limited access";
            default:
                return role.name() + " role";
        }
    }
}