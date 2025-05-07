# ecom

## Installation

1. Build the Docker image:

```bash
docker build -t my-java-app .
```

2. Run the Docker container:

```bash
docker run -p 8080:8080 my-java-app
```

3. Access the application:

```bash
http://localhost:8080
```

## Schema

![Schema](./asset/schema.jpeg)

## Roles

- CUSTOMER
- ADMIN

```bash
├─ .mvn
│  └─ wrapper
│     ├─ maven-wrapper.properties
│     └─ maven-wrapper.properties:Zone.Identifier
├─ asset
│  └─ schema.jpeg
├─ src
│  ├─ main
│  │  ├─ java
│  │  │  └─ com
│  │  │     └─ example
│  │  │        └─ ecommerce
│  │  │           ├─ config
│  │  │           │  ├─ CorsConfig.java
│  │  │           │  ├─ DataInitializer.java
│  │  │           │  ├─ JwtProperties.java
│  │  │           │  ├─ SecurityConfig.java
│  │  │           │  └─ SwaggerConfig.java
│  │  │           ├─ controller
│  │  │           │  ├─ AuthController.java
│  │  │           │  ├─ CartController.Java
│  │  │           │  ├─ CategoryController.java
│  │  │           │  ├─ OrderController.java
│  │  │           │  ├─ ProductController.java
│  │  │           │  ├─ RatingController.java
│  │  │           │  └─ UserController.java
│  │  │           ├─ dto
│  │  │           │  ├─ AuthRequest.java
│  │  │           │  ├─ AuthResponse.java
│  │  │           │  ├─ CategoryRequest.java
│  │  │           │  ├─ CategoryResponse.java
│  │  │           │  ├─ ProductRequest.java
│  │  │           │  ├─ ProductResponse.java
│  │  │           │  └─ RegisterRequest.java
│  │  │           ├─ enums
│  │  │           │  ├─ OrderStatus.java
│  │  │           │  └─ Role.java
│  │  │           ├─ exception
│  │  │           │  └─ ResourceNotFoundException.java
│  │  │           ├─ mapper
│  │  │           │  ├─ CategoryMapper.java
│  │  │           │  ├─ ProductMapper.java
│  │  │           │  └─ ProductMapperMS.java
│  │  │           ├─ model
│  │  │           │  ├─ CartItemModel.java
│  │  │           │  ├─ CartModel.java
│  │  │           │  ├─ CategoryModel.java
│  │  │           │  ├─ OrderItemModel.java
│  │  │           │  ├─ OrderModel.java
│  │  │           │  ├─ ProductImageModel.java
│  │  │           │  ├─ ProductModel.java
│  │  │           │  ├─ RatingModel.java
│  │  │           │  ├─ RoleModel.java
│  │  │           │  └─ UserModel.java
│  │  │           ├─ repository
│  │  │           │  ├─ CartItemRepository.java
│  │  │           │  ├─ CartRepository.java
│  │  │           │  ├─ CategoryRepository.java
│  │  │           │  ├─ OrderRepository.java
│  │  │           │  ├─ ProductImageRepository.java
│  │  │           │  ├─ ProductRepository.java
│  │  │           │  ├─ RatingRepository.java
│  │  │           │  ├─ RoleRepository.java
│  │  │           │  └─ UserRepository.java
│  │  │           ├─ security
│  │  │           │  ├─ AuthEntryPointJwt.java
│  │  │           │  ├─ JwtAuthenticationFilter.java
│  │  │           │  ├─ JwtUtils.java
│  │  │           │  ├─ UserDetailsImpl.java
│  │  │           │  └─ UserDetailsServiceImpl.java
│  │  │           ├─ service
│  │  │           │  ├─ CartService.java
│  │  │           │  ├─ CategoryService.java
│  │  │           │  ├─ OrderService.java
│  │  │           │  ├─ ProductService.java
│  │  │           │  ├─ RatingService.java
│  │  │           │  └─ UserService.java
│  │  │           └─ EcommerceApplication.java
│  │  └─ resources
│  │     ├─ application.yaml
│  │     └─ schema.sql
│  └─ test
│     └─ java
│        └─ com
│           └─ example
│              └─ ecommerce
│                 ├─ EcommerceApplicationTests.java
│                 └─ EcommerceApplicationTests.java:Zone.Identifier
├─ .env
├─ .gitattributes
├─ .gitignore
├─ README.md
├─ dockerfile
├─ mvnw
├─ mvnw.cmd
└─ pom.xml
```
