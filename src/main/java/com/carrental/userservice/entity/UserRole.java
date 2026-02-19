package com.carrental.userservice.entity;

public enum UserRole {

    CUSTOMER,
    OWNER,
    ADMIN;

    public static UserRole fromString(String s) {
        if (s == null) return null;

        String normalized = s.trim().toUpperCase();

        if ("USER".equals(normalized)) {
            return CUSTOMER;
        }

        if (normalized.startsWith("ROLE_")) {
            normalized = normalized.substring(5);
        }

        try {
            return UserRole.valueOf(normalized);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Unknown role: " + s);
        }
    }
}
