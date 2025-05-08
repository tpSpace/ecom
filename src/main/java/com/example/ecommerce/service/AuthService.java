package com.example.ecommerce.service;

import com.example.ecommerce.dto.AuthRequest;
import com.example.ecommerce.dto.AuthResponse;
import com.example.ecommerce.dto.RegisterRequest;
import com.example.ecommerce.enums.Role;
import com.example.ecommerce.model.RoleModel;
import com.example.ecommerce.model.UserModel;
import com.example.ecommerce.repository.RoleRepository;
import com.example.ecommerce.security.JwtUtils;
import com.example.ecommerce.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

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

    public AuthResponse login(AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return buildAuthResponse(authentication);
    }

    @Transactional
    public AuthResponse register(RegisterRequest registerRequest) {
        checkEmailAvailability(registerRequest.getEmail());
        createAndSaveUser(registerRequest);
        // After registration, authenticate the user
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail(registerRequest.getEmail());
        authRequest.setPassword(registerRequest.getPassword());
        return login(authRequest);
    }

    private void checkEmailAvailability(String email) {
        if (userService.getUserByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email is already in use");
        }
    }

    private UserModel createAndSaveUser(RegisterRequest request) {
        UserModel user = new UserModel();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAddress(request.getAddress());
        assignDefaultRole(user);
        return userService.createUser(user);
    }

    private void assignDefaultRole(UserModel user) {
        String defaultRole = Role.ROLE_CUSTOMER.name();
        RoleModel userRole = roleRepository.findByRole(defaultRole)
                .orElseThrow(() -> new RuntimeException("Error: Default role not found"));
        user.setRole(userRole);
    }

    private AuthResponse buildAuthResponse(Authentication authentication) {
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(item -> item.getAuthority())
                .orElse(Role.ROLE_CUSTOMER.name());
        return new AuthResponse(
                jwt,
                role,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getFirstName(),
                userDetails.getLastName());
    }
}
