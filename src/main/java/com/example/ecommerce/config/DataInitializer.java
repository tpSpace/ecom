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
        // Create 20 customer accounts
        String[] firstNames = { "John", "Sarah", "Michael", "Emily", "David", "Jessica", "James", "Jennifer",
                "Robert", "Lisa", "William", "Mary", "Richard", "Patricia", "Joseph",
                "Linda", "Thomas", "Elizabeth", "Charles", "Susan" };

        String[] lastNames = { "Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson",
                "Moore", "Taylor", "Anderson", "Thomas", "Jackson", "White", "Harris",
                "Martin", "Thompson", "Garcia", "Martinez", "Robinson" };

        String[] cities = { "New York", "Los Angeles", "Chicago", "Houston", "Phoenix", "Philadelphia",
                "San Antonio", "San Diego", "Dallas", "San Jose", "Austin", "Jacksonville",
                "Fort Worth", "Columbus", "San Francisco", "Charlotte", "Indianapolis",
                "Seattle", "Denver", "Washington" };
        for (int i = 0; i < 20; i++) {
            String email = "customer" + (i + 1) + "@example.com";
            if (userRepository.findByEmail(email) == null) {
                UserModel customer = new UserModel();
                customer.setEmail(email);
                customer.setPassword(passwordEncoder.encode("password123"));
                customer.setRole(roleRepository.findByRole(Role.ROLE_CUSTOMER.name()).orElse(null));
                customer.setFirstName(firstNames[i]);
                customer.setLastName(lastNames[i]);
                customer.setPhoneNumber("555" + String.format("%07d", i + 1000000));
                customer.setAddress((100 + i) + " Main Street, " + cities[i] + ", " + generateRandomZip());
                userRepository.save(customer);
                System.out.println("Created customer user: " + customer.getEmail());
            }
        }
    }

    private String generateRandomZip() {
        // Generate a random 5-digit ZIP code
        return String.format("%05d", (int) (Math.random() * 90000) + 10000);
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