package com.example.ecommerce.controller;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.ecommerce.dto.AuthRequest;
import com.example.ecommerce.dto.AuthResponse;
import com.example.ecommerce.dto.RegisterRequest;
import com.example.ecommerce.enums.Role;
import com.example.ecommerce.model.RoleModel;
import com.example.ecommerce.model.UserModel;
import com.example.ecommerce.repository.RoleRepository;
import com.example.ecommerce.security.JwtUtils;
import com.example.ecommerce.security.UserDetailsImpl;
import com.example.ecommerce.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication API")
public class AuthController {

        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        private UserService userService;

        @Autowired
        private RoleRepository roleRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private JwtUtils jwtUtils;

        @PostMapping("/login")
        @Operation(summary = "Login", description = "Authenticate user and generate JWT token")
        @ApiResponse(responseCode = "200", description = "Login successful")
        @ApiResponse(responseCode = "401", description = "Invalid credentials")
        public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest) {
                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(authRequest.getEmail(),
                                                authRequest.getPassword()));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                String jwt = jwtUtils.generateJwtToken(authentication);

                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
                String role = userDetails.getAuthorities().stream()
                                .findFirst()
                                .map(item -> item.getAuthority())
                                .orElse(Role.CUSTOMER.name());
                // Create an HTTP-only cookie for JWT
                ResponseCookie jwtCookie = ResponseCookie.from("jwt", jwt)
                                .httpOnly(true)
                                .secure(false) // Set to true if using HTTPS
                                .path("/")
                                .maxAge(24 * 60 * 60) // 1 day
                                .build();
                System.out.println("JWT Cookie: " + jwt);
                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                                .body(new AuthResponse(
                                                jwt,
                                                role,
                                                userDetails.getId(),
                                                userDetails.getUsername(),
                                                userDetails.getFirstName(),
                                                userDetails.getLastName()));
        }

        @PostMapping("/register")
        @Operation(summary = "Register", description = "Register a new user")
        @ApiResponse(responseCode = "200", description = "Registration successful")
        @ApiResponse(responseCode = "400", description = "Email already in use")
        public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
                if (userService.getUserByEmail(registerRequest.getEmail()).isPresent()) {
                        return ResponseEntity.badRequest().body("Error: Email is already in use");
                }

                // Create new user account
                UserModel user = new UserModel();
                user.setEmail(registerRequest.getEmail());
                user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
                user.setFirstName(registerRequest.getFirstName());
                user.setLastName(registerRequest.getLastName());
                user.setPhoneNumber(registerRequest.getPhoneNumber());
                user.setAddress(registerRequest.getAddress());

                String userZ = Role.CUSTOMER.name();
                // Assign default ROLE_USER
                RoleModel userRole = roleRepository.findByRole(userZ)
                                .orElseThrow(() -> new RuntimeException("Error: Default role not found"));
                user.setRole(userRole);

                userService.createUser(user);

                // Login the user after registration
                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(registerRequest.getEmail(),
                                                registerRequest.getPassword()));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                String jwt = jwtUtils.generateJwtToken(authentication);

                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
                String role = userDetails.getAuthorities().stream()
                                .findFirst()
                                .map(item -> item.getAuthority())
                                .orElse(Role.CUSTOMER.name());

                return ResponseEntity.ok(new AuthResponse(jwt, role, userDetails.getId(), userDetails.getUsername(),
                                userDetails.getFirstName(), userDetails.getLastName()));
        }
}