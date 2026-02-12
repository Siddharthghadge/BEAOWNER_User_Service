package com.carrental.userservice.controller;

import com.carrental.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
//@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class IdentityController {

    @Autowired
    private UserService userService;

    @PostMapping("/upload-identity")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> uploadIdentity(
            @RequestParam("license") MultipartFile license,
            @RequestParam("aadhar") MultipartFile aadhar,
            Principal principal) {

        try {
            if (principal == null) {
                return ResponseEntity.status(401).body(Map.of("message", "Token missing or invalid"));
            }

            String email = principal.getName();
            userService.updateUserIdentity(email, license, aadhar);

            return ResponseEntity.ok(Map.of(
                    "message", "Documents uploaded successfully!",
                    "isVerified", true
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Upload failed: " + e.getMessage()));
        }
    }
}