package com.carrental.userservice.dto;

import lombok.Data;

@Data
public class CreateUserRequest {

    private String name;
    private String email;
    private String phone;
    private String password;
    private String address;  // <-- REQUIRED FIELD
    private String role;
}
