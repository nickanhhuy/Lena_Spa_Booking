package com.example.backend_lena.controller;

import com.example.backend_lena.dto.PasswordChangeRequest;
import com.example.backend_lena.dto.ProfileUpdateRequest;
import com.example.backend_lena.dto.UserProfileResponse;
import com.example.backend_lena.model.User;
import com.example.backend_lena.repository.UserRepository;
import com.example.backend_lena.security.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtUtil;

    // Register API endpoint
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) { // check the availability of username
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");

        }
        user.setPassword(passwordEncoder.encode(user.getPassword())); // encrypted the password for safe security
        user.setRole("USER"); // set 'user' role for every user who can only see their own booking
        userRepository.save(user); // then create a new account for the user and send to database
        return ResponseEntity.ok("User registered successfully");
    }

    // Login API endpoint
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );

            // Generate JWT token
            String token = jwtUtil.generateToken((UserDetails) authentication.getPrincipal());
            return ResponseEntity.ok(token);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");


        }
    }

    // check the current session of the user who just logged in ( this endpoint is used to debug whether correct user in the session)
    @GetMapping("/current-user")
    public ResponseEntity<String> getCurrentUser(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return ResponseEntity.ok(authentication.getName());
        } else {
            return ResponseEntity.ok("anonymousUser");
        }
    }
    // logout endpoint

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logout successful");
    }

    // Get user profile
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication) {
        String username = authentication.getName();
        Optional<User> userOpt = userRepository.findByUsername(username);
        
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        
        User user = userOpt.get();
        UserProfileResponse profile = new UserProfileResponse(
            user.getUsername(),
            user.getEmail(),
            user.getPhone(),
            user.getAvatarUrl(),
            user.getRole()
        );
        
        return ResponseEntity.ok(profile);
    }

    // Update user profile
    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(@RequestBody ProfileUpdateRequest request, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> userOpt = userRepository.findByUsername(username);
        
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        
        User user = userOpt.get();
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getAvatarUrl() != null) {
            user.setAvatarUrl(request.getAvatarUrl());
        }
        
        userRepository.save(user);
        return ResponseEntity.ok("Profile updated successfully");
    }

    // Change password
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody PasswordChangeRequest request, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> userOpt = userRepository.findByUsername(username);
        
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        
        User user = userOpt.get();
        
        // Verify current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Current password is incorrect");
        }
        
        // Update to new password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        
        return ResponseEntity.ok("Password changed successfully");
    }
}

