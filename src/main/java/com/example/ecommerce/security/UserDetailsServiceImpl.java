package com.example.ecommerce.security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.ecommerce.model.UserModel;
import com.example.ecommerce.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserModel user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        // Create a set of authorities based on user's role
        Set<GrantedAuthority> authorities = new HashSet<>();

        // Add the user's main role
        if (user.getRole() != null) {
            String roleName = user.getRole().getRole();
            // Ensure role has ROLE_ prefix
            if (!roleName.startsWith("ROLE_")) {
                roleName = "ROLE_" + roleName;
            }
            authorities.add(new SimpleGrantedAuthority(roleName));

            // Log for debugging
            System.out.println("Added authority: " + roleName);
        }

        // You could add additional permissions here if needed
        // authorities.add(new SimpleGrantedAuthority("PERMISSION_READ_PRODUCTS"));

        // Create the user details with the authority set
        return UserDetailsImpl.buildWithAuthorities(user, authorities);
    }
}