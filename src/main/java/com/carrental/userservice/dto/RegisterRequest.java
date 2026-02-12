package com.carrental.userservice.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String phone;
    private String password;
    private String address;
    private String role;      // CUSTOMER / ADMIN / COMPANY
}
