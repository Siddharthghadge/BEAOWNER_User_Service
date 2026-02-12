package com.carrental.userservice.dto;

import com.carrental.userservice.entity.User;

public class UserMapper {

    public static UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .role(user.getRole().name())
                .password(user.getPassword()) // <-- added here
                .created_at(user.getCreatedAt() != null ? user.getCreatedAt().toString() : null)
                .enabled(user.getEnabled() != null ? user.getEnabled() : true)
                .build();
    }
}
