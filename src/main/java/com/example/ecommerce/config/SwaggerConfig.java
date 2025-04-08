package com.example.ecommerce.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

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

    /**
     * Configures the security filter chain to allow access to Swagger UI and API documentation.
     *
     * @param http the HttpSecurity object to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */ 
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF protection if not needed
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/"
                ).permitAll()
                .anyRequest().authenticated()
            );
        return http.build();
    }
}