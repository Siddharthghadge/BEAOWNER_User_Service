package com.carrental.userservice.dto;

import com.carrental.userservice.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor          // <-- ADD THIS
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String role;
    private String created_at;
    private String password;
    private Boolean enabled;
}
