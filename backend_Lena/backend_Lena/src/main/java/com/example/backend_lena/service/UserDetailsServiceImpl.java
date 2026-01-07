package com.example.backend_lena.service;

import com.example.backend_lena.model.User;
import com.example.backend_lena.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String emailOrUsername) throws UsernameNotFoundException {
        // Try to find user by email first, then by username
        User user = userRepository.findByEmail(emailOrUsername)
                .or(() -> userRepository.findByUsername(emailOrUsername))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email or username: " + emailOrUsername));

        // Make sure role starts with "ROLE_"
        String role = user.getRole();
        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }

        return user; // Return the User entity directly since it implements UserDetails
    }

}


