package com.example.ecommerce.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.ecommerce.enums.Role;
import com.example.ecommerce.model.CategoryModel;
import com.example.ecommerce.model.OrderItemModel;
import com.example.ecommerce.model.OrderModel;
import com.example.ecommerce.model.ProductModel;
import com.example.ecommerce.model.RoleModel;
import com.example.ecommerce.model.UserModel;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.RoleRepository;
import com.example.ecommerce.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderRepository orderItemRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        // Initialize roles
        initRoles();

        // Initialize admin user
        initAdminUser();

        // Initialize furniture categories
        initFurnitureCategories();

        // Initialize sample products
        initSampleProducts();

        // Initialize random orders
        initRandomOrders();
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
        String[] firstNames = { "John", "Sarah", "Michael", };

        String[] lastNames = { "Smith", "Johnson", "Williams" };

        String[] cities = { "New York", "Los Angeles", "Chicago" };
        for (int i = 0; i < 3; i++) {
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

    private void initSampleProducts() {
        // Get all categories
        List<CategoryModel> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            System.out.println("No categories found - skipping product generation");
            return;
        }

        // Sample product data structure: name, description, price, category index
        List<Object[]> sampleProducts = Arrays.asList(
                // Living Room products
                new Object[] { "Modern Sofa", "Three-seater sofa with premium fabric upholstery", 999.99,
                        "Living Room" },
                new Object[] { "Coffee Table", "Wood and glass coffee table with storage", 249.99, "Living Room" },
                new Object[] { "TV Stand", "Wide entertainment center with cable management", 349.99, "Living Room" },

                // Bedroom products
                new Object[] { "Queen Bed Frame", "Solid wood queen-size bed frame", 599.99, "Bedroom" },
                new Object[] { "Wardrobe", "Large wardrobe with sliding doors", 799.99, "Bedroom" },
                new Object[] { "Nightstand", "Bedside table with drawer", 129.99, "Bedroom" },

                // Dining Room products
                new Object[] { "Dining Table Set", "Table with 6 chairs", 899.99, "Dining Room" },
                new Object[] { "China Cabinet", "Glass-front cabinet for dishes", 699.99, "Dining Room" },

                // Kitchen products
                new Object[] { "Bar Stools (Set of 2)", "Counter-height stools", 179.99, "Kitchen" },
                new Object[] { "Kitchen Island", "Rolling kitchen island with butcher block top", 449.99, "Kitchen" },

                // Office products
                new Object[] { "Office Desk", "L-shaped corner desk", 329.99, "Office" },
                new Object[] { "Ergonomic Chair", "Adjustable office chair with lumbar support", 249.99, "Office" },
                new Object[] { "Bookshelf", "5-shelf bookcase", 159.99, "Office" });

        // Create and save products
        Random random = new Random();
        for (Object[] productData : sampleProducts) {
            String name = (String) productData[0];
            String description = (String) productData[1];
            Double price = (Double) productData[2];
            String categoryName = (String) productData[3];

            // Find the category
            CategoryModel category = categoryRepository.findByName(categoryName);
            if (category == null)
                continue;

            // Check if product already exists
            if (!productRepository.findByNameAndCategory_Id(name, category.getId()).isEmpty()) {
                continue;
            }

            // Create new product
            ProductModel product = new ProductModel();
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setQuantity(random.nextInt(50) + 10); // Random stock between 10-60
            product.setCategory(category);
            product.setFeatured(random.nextBoolean()); // Random featured status

            // Save product
            productRepository.save(product);
            System.out.println("Created product: " + name + " in category " + categoryName);
        }
    }

    private void initRandomOrders() {
        // Only proceed if we have products and users
        List<UserModel> customers = userRepository.findAll().stream()
                .filter(u -> u.getRole().getRole().equals(Role.ROLE_CUSTOMER.name()))
                .collect(Collectors.toList());

        List<ProductModel> products = productRepository.findAll();

        if (products.isEmpty()) {
            System.out.println("No products found - skipping order generation");
            return;
        }

        Random random = new Random();
        String[] statuses = { "PENDING", "PROCESSING", "SHIPPED", "DELIVERED", "CANCELLED" };

        // Create between 1-4 orders for each customer
        for (UserModel customer : customers) {
            int numOrders = 1;

            for (int i = 0; i < numOrders; i++) {
                OrderModel order = new OrderModel();
                order.setUser(customer);

                // Set random dates within the last 30 days
                LocalDateTime orderDate = LocalDateTime.now().minusDays(random.nextInt(30));
                order.setCreatedAt(orderDate.toLocalDate());

                // Set random status
                String status = statuses[random.nextInt(statuses.length)];
                order.setOrderStatus(status);

                // Generate shipping address from customer data
                order.setShippingAddress(customer.getAddress());
                // Set billing address (required field)
                order.setBillingAddress(customer.getAddress());

                // Save the order first to get an ID
                orderRepository.save(order);

                // Create 1-5 order items with random products
                int itemCount = random.nextInt(5) + 1;
                double totalAmount = 0.0;

                for (int j = 0; j < itemCount; j++) {
                    // Get random product
                    ProductModel product = products.get(random.nextInt(products.size()));

                    OrderItemModel orderItem = new OrderItemModel();
                    orderItem.setOrder(order);
                    orderItem.setProduct(product);

                    // Random quantity between 1-3
                    int quantity = random.nextInt(3) + 1;
                    orderItem.setQuantity(quantity);

                    // Set price from product
                    orderItem.setPrice(product.getPrice());

                    // Calculate item total
                    double itemTotal = quantity * product.getPrice();
                    totalAmount += itemTotal;

                    // Save order item using the correct repository
                    orderItemRepository.save(orderItem);
                }

                // Update order with final amount
                order.setTotalAmount(totalAmount);
                orderRepository.save(order);

                System.out.println("Created order #" + order.getId() + " for " + customer.getEmail() +
                        " with " + itemCount + " items, total: $" + totalAmount);
            }
        }

        System.out.println("Order initialization complete!");
    }
}