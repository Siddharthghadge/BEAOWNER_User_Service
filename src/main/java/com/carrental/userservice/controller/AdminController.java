package com.carrental.userservice.controller;

import com.carrental.userservice.entity.User;
import com.carrental.userservice.entity.UserRole;
import com.carrental.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/users/{userId}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> changeUserRole(
            @PathVariable Long userId,
            @RequestParam String role) {

        userService.updateUserRole(userId, UserRole.fromString(role));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/users/{userId}/toggle-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> toggleStatus(@PathVariable Long userId) {
        userService.toggleUserAccountStatus(userId);
        return ResponseEntity.ok().build();
    }
}