package com.carrental.userservice.controller;

import com.carrental.userservice.dto.*;
import com.carrental.userservice.entity.User;
import com.carrental.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
//@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    // ----------------- PUBLIC / INTERNAL ENDPOINTS -----------------

    // Public register (if you use it directly)
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody CreateUserRequest request) {
        User user = userService.registerUser(request);
        return ResponseEntity.ok(UserMapper.toResponse(user));
    }

    // Internal register (Auth Service uses this)
    @PostMapping("/register/internal")
    public ResponseEntity<UserResponse> registerInternal(@RequestBody CreateUserRequest request) {
        User user = userService.registerUser(request);
        return ResponseEntity.ok(UserMapper.toResponse(user));
    }

    // Internal: used by Auth Service when logging in
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(UserMapper.toResponse(user));
    }

    // ----------------- PROTECTED ENDPOINTS -----------------

    // Get my own profile (any authenticated user)
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> getMyProfile(@RequestAttribute("email") String email) {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(UserMapper.toResponse(user));
    }

    // Get user by ID (ADMIN only, or self if you want)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_Customer', 'ROLE_Owner', 'ROLE_Admin') or hasAnyRole('CUSTOMER', 'OWNER', 'ADMIN')")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(UserMapper.toResponse(user));
    }

    // Get all users (ADMIN only)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users.stream().map(UserMapper::toResponse).toList());
    }

    // Update user (ADMIN only for now)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request) {

        User updated = userService.updateUser(id, request);
        return ResponseEntity.ok(UserMapper.toResponse(updated));
    }

    // Delete user (ADMIN only)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }



}
