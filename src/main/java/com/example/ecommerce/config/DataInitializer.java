package com.example.ecommerce.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.ecommerce.enums.Role;
import com.example.ecommerce.model.CategoryModel;
import com.example.ecommerce.model.RoleModel;
import com.example.ecommerce.model.UserModel;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.repository.RoleRepository;
import com.example.ecommerce.repository.UserRepository;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Initialize roles
        initRoles();

        // Initialize admin user
        initAdminUser();

        // Initialize furniture categories
        initFurnitureCategories();
    }

    private void initRoles() {
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
    }

    private void initAdminUser() {
        // Create an admin user if it doesn't exist
        String adminEmail = "admin@admin.com";
        if (userRepository.findByEmail(adminEmail) == null) {
            UserModel adminUser = new UserModel();
            adminUser.setEmail(adminEmail);
            adminUser.setPassword(passwordEncoder.encode("admin1234"));
            adminUser.setRole(roleRepository.findByRole(Role.ROLE_ADMIN.name()).orElse(null));
            adminUser.setFirstName("lmao");
            adminUser.setLastName("super_lmao");
            adminUser.setPhoneNumber("1234567890");
            adminUser.setAddress("123 Admin St");
            userRepository.save(adminUser);
            System.out.println("Created admin user: " + adminUser.getEmail());
        }
    }

    private void initFurnitureCategories() {
        // Define furniture categories with descriptions
        List<String[]> furnitureCategories = Arrays.asList(
                new String[] { "Living Room", "Sofas, armchairs, coffee tables, TV stands, and decorative items" },
                new String[] { "Bedroom", "Beds, wardrobes, dressers, nightstands, and bedroom accessories" },
                new String[] { "Dining Room", "Dining tables, chairs, buffets, and dining storage solutions" },
                new String[] { "Kitchen", "Kitchen islands, bar stools, kitchen storage, and accessories" },
                new String[] { "Office", "Desks, office chairs, bookcases, and office organization" },
                new String[] { "Outdoor", "Patio furniture, outdoor dining sets, and garden accessories" },
                new String[] { "Storage", "Cabinets, shelving units, and organization systems" },
                new String[] { "Bathroom", "Bathroom vanities, mirrors, and storage solutions" },
                new String[] { "Kids", "Children's furniture, bunk beds, and playroom items" },
                new String[] { "Lighting", "Lamps, ceiling lights, and decorative lighting" });

        // Save each category if it doesn't exist already
        for (String[] categoryInfo : furnitureCategories) {
            String name = categoryInfo[0];
            String description = categoryInfo[1];

            if (categoryRepository.findByName(name) == null) {
                CategoryModel category = new CategoryModel();
                category.setName(name);
                category.setDescription(description);
                categoryRepository.save(category);
                System.out.println("Created furniture category: " + name);
            }
        }
    }

    private String getDescriptionForRole(Role role) {
        switch (role) {
            case ROLE_ADMIN:
                return "Administrator role with full access";
            case ROLE_CUSTOMER:
                return "Regular customer role with limited access";
            default:
                return role.name() + " role";
        }
    }
}