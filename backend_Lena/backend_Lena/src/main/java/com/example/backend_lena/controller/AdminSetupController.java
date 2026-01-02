package com.example.backend_lena.controller;

import com.example.backend_lena.model.User;
import com.example.backend_lena.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/setup")
public class AdminSetupController {

    @Autowired
    private UserRepository userRepository;

    // TEMPORARY ENDPOINT - Remove after creating admin user
    // This endpoint should be protected or removed in production
    @PostMapping("/make-admin/{username}")
    public ResponseEntity<String> makeAdmin(@PathVariable String username, @RequestParam String secretKey) {
        // Simple security check - you should use a strong secret key
        if (!"LENA_ADMIN_SETUP_2026".equals(secretKey)) {
            return ResponseEntity.status(403).body("Invalid secret key");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setRole("ADMIN");
        userRepository.save(user);
        
        return ResponseEntity.ok("User " + username + " is now an ADMIN");
    }
}
