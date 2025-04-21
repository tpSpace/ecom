package com.example.ecommerce.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

    /**
     * Configures Swagger UI and API documentation for the application.
     * This configuration allows access to the Swagger UI and API documentation endpoints.
     *
     * @return the OpenAPI bean for Swagger configuration
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("E-commerce API")
                .version("1.0")
                .description("API documentation for the E-commerce application")
            )
            .servers(List.of(new Server().url("http://localhost:8080")));
    }

}