# Ecommerce Application

Ecommerce for NashTech is a Spring Boot application designed to provide a backend for an e-commerce platform. It includes features for product management, shopping carts, user authentication, orders, ratings, and category management.

## Features

*   **Product Management:** Create, read, update, and delete products.
*   **Category Management:** Organize products into categories.
*   **Shopping Cart:** Allow users to add/remove items from their cart.
*   **Order Processing:** Handle customer orders and payment (payment integration not explicitly confirmed but typical for e-commerce).
*   **User Authentication & Authorization:** Secure user registration and login using JWT.
*   **User Management:** Manage user accounts.
*   **Product Ratings:** Allow users to rate products.

## Technologies Used

*   **Framework:** Spring Boot 3.4.4
*   **Language:** Java 21
*   **Database:** PostgreSQL
*   **Authentication:** JWT (JSON Web Tokens)
*   **API Documentation:** SpringDoc OpenAPI
*   **Build Tool:** Maven
*   **Other Key Libraries:**
    *   Spring Data JPA
    *   Spring Security
    *   MapStruct (for DTO mapping)
    *   Lombok
    *   dotenv-java (for environment variable management)
    *   gRPC (for potential microservice communication)

## Prerequisites

*   Java JDK 21 or later
*   Maven 3.6.x or later
*   PostgreSQL database server
*   Docker (optional, for containerized deployment)

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/tpSpace/ecom
cd ecom
```

### 2. Database Setup

This project uses PostgreSQL.
1.  Ensure you have a PostgreSQL server running.
2.  Create a database for the application (e.g., `ecommerce_db`).
3.  The application uses Hibernate's `ddl-auto: create` feature, which means it will attempt to create the necessary tables on startup based on your entities. Alternatively, you can use the `src/main/resources/schema.sql` file to set up the database schema manually if preferred or if `ddl-auto` is changed.

### 3. Configure Environment Variables

The application requires certain environment variables for configuration. Create a `.env` file in the root of the project with the following content, replacing the placeholder values with your actual configuration:

```env
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/ecommerce_db
SPRING_DATASOURCE_USERNAME=your_db_user
SPRING_DATASOURCE_PASSWORD=your_db_password
JWT_SECRET=your_very_strong_and_long_jwt_secret_key_at_least_256_bits
```

*   `SPRING_DATASOURCE_URL`: The JDBC URL for your PostgreSQL database.
*   `SPRING_DATASOURCE_USERNAME`: The username for your database.
*   `SPRING_DATASOURCE_PASSWORD`: The password for your database.
*   `JWT_SECRET`: A strong secret key for signing JWTs.

The application uses `dotenv-java` to load these variables.

### 4. Build and Run the Application

You can build and run the application using Maven:

```bash
./mvnw spring-boot:run
```

Or, to build a JAR file:

```bash
./mvnw clean package
java -jar target/ecommerce-0.0.1-SNAPSHOT.jar
```

The application will typically start on port `8080` (this is the Spring Boot default, but it can be overridden in `application.yaml` or via environment variables if needed. Currently, it is not explicitly set in the provided `application.yaml`, so it will use the default).

## API Documentation

The API is documented using SpringDoc OpenAPI. Once the application is running, you can access the Swagger UI at:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

(Adjust the port if you have configured it differently).

## Docker

A `dockerfile` is provided to containerize the application.

### Build the Docker Image

```bash
docker build -t ecommerce-app .
```

### Run the Docker Container

Make sure to pass the required environment variables to the container. You can do this by providing each variable individually using the `-e` flag, or more conveniently, by using an `--env-file` option if you have your environment variables defined in a `.env` file (as described in the "Configure Environment Variables" section).

**Option 1: Using individual `-e` flags (as previously shown)**
```bash
docker run -e SPRING_DATASOURCE_URL=jdbc:postgresql://your_db_host:5432/your_db_name \
           -e SPRING_DATASOURCE_USERNAME=your_db_user \
           -e SPRING_DATASOURCE_PASSWORD=your_db_password \
           -e JWT_SECRET=your_very_strong_jwt_secret \
           -p 8080:8080 ecommerce-app
```
Replace placeholders with your actual database connection details and JWT secret.

**Option 2: Using an `--env-file` (Recommended)**

If you have a `.env` file in your current directory (e.g., the project root) with the necessary variables:
```env
# Example .env file contents:
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/ecommerce_db
SPRING_DATASOURCE_USERNAME=your_db_user
SPRING_DATASOURCE_PASSWORD=your_db_password
JWT_SECRET=your_very_strong_and_long_jwt_secret_key_at_least_256_bits
```

You can run the container like this:
```bash
docker run --env-file .env -p 8080:8080 ecommerce-app
```
This command tells Docker to load environment variables from the `.env` file located in the current directory where you are running the command.

Ensure your `.env` file is present and correctly formatted.

## Project Structure

*   `src/main/java/com/example/ecommerce`: Main application code
    *   `EcommerceApplication.java`: Main Spring Boot application class.
    *   `config/`: Spring Boot and application-specific configurations.
    *   `controller/`: REST API controllers.
    *   `dto/`: Data Transfer Objects.
    *   `enums/`: Enumeration types.
    *   `exception/`: Custom exception handlers.
    *   `mapper/`: MapStruct mappers for DTO conversions.
    *   `model/`: JPA entities representing the database tables.
    *   `repository/`: Spring Data JPA repositories for database access.
    *   `security/`: Security configuration, including JWT handling.
    *   `service/`: Business logic and services.
*   `src/main/resources`: Application resources
    *   `application.yaml`: Main application configuration.
    *   `schema.sql`: SQL script for database schema creation (can be used if `ddl-auto` is not `create`).
    *   `static/`: Static assets (CSS, JS, images).
    *   `templates/`: Server-side templates (if any, e.g., Thymeleaf).
*   `pom.xml`: Maven project configuration.
*   `dockerfile`: Docker image definition.
*   `.env` (You need to create this): For local environment variable configuration.

## Contributing

Please refer to `HELP.md` for contribution guidelines (assuming `HELP.md` contains this information).
