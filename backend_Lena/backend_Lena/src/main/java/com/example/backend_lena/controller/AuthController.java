package com.example.backend_lena.controller;

import com.example.backend_lena.dto.PasswordChangeRequest;
import com.example.backend_lena.dto.ProfileUpdateRequest;
import com.example.backend_lena.dto.UserProfileResponse;
import com.example.backend_lena.model.User;
import com.example.backend_lena.repository.UserRepository;
import com.example.backend_lena.security.JWTUtil;
import com.example.backend_lena.service.ResendEmailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private ResendEmailService resendEmailService;

    // Register API endpoint
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) { // check the availability of username
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
        }
        
        // Check if email is provided
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is required for registration");
        }
        
        // Check if email already exists
        Optional<User> existingEmailUser = userRepository.findByEmail(user.getEmail());
        if (existingEmailUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword())); // encrypted the password for safe security
        user.setRole("USER"); // set 'user' role for every user who can only see their own booking
        user.setEmailVerified(false); // Email not verified yet
        
        // Generate verification token
        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken);
        
        userRepository.save(user); // then create a new account for the user and send to database
        
        // Send email verification instead of welcome email
        try {
            resendEmailService.sendEmailVerification(user.getEmail(), user.getUsername(), verificationToken);
        } catch (Exception e) {
            System.err.println("Failed to send verification email: " + e.getMessage());
            // Continue with registration even if email fails
        }
        
        // Send admin notification
        try {
            resendEmailService.sendAdminRegistrationNotification(
                user.getUsername(), 
                user.getEmail(), 
                user.getPhone()
            );
        } catch (Exception e) {
            System.err.println("Failed to send admin notification: " + e.getMessage());
            // Continue with registration even if notification fails
        }
        
        return ResponseEntity.ok("Registration successful! Please check your email to verify your account before logging in.");
    }

    // Login API endpoint - supports both email and username
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        try {
            // User can login with either email or username
            String loginIdentifier = user.getEmail() != null && !user.getEmail().isEmpty() 
                ? user.getEmail() 
                : user.getUsername();
                
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginIdentifier, user.getPassword())
            );

            // Generate JWT token
            String token = jwtUtil.generateToken((UserDetails) authentication.getPrincipal());
            return ResponseEntity.ok(token);

        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please verify your email address before logging in. Check your inbox for the verification link.");
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

    // Email verification endpoint
    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        Optional<User> userOpt = userRepository.findByVerificationToken(token);
        
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired verification token");
        }
        
        User user = userOpt.get();
        user.setEmailVerified(true);
        user.setVerificationToken(null); // Clear the token after verification
        userRepository.save(user);
        
        // Send welcome email after successful verification
        try {
            resendEmailService.sendWelcomeEmail(user.getEmail(), user.getUsername());
        } catch (Exception e) {
            System.err.println("Failed to send welcome email: " + e.getMessage());
        }
        
        return ResponseEntity.ok("Email verified successfully! You can now log in to your account.");
    }

    // Resend verification email
    @PostMapping("/resend-verification")
    public ResponseEntity<String> resendVerificationEmail(@RequestParam("email") String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        
        User user = userOpt.get();
        
        if (user.isEmailVerified()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is already verified");
        }
        
        // Generate new verification token
        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken);
        userRepository.save(user);
        
        // Send verification email
        try {
            resendEmailService.sendEmailVerification(user.getEmail(), user.getUsername(), verificationToken);
            return ResponseEntity.ok("Verification email sent successfully! Please check your inbox.");
        } catch (Exception e) {
            System.err.println("Failed to send verification email: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send verification email");
        }
    }
}

