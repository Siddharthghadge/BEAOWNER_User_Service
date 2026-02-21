package com.carrental.userservice.entity;

public enum UserRole {
    CUSTOMER,
    OWNER,
    ADMIN,
    Customer,
    Owner,
    Admin;

    /**
     * Parse a role string into UserRole in a forgiving way.
     * Accepts values like: "USER", "user", "Customer", "ROLE_CUSTOMER", etc.
     * Maps unknown "USER" to CUSTOMER (common mapping when other services use USER).
     */
    public static UserRole fromString(String s) {
        if (s == null) return null;
        String normalized = s.trim().toUpperCase();

        // handle common alias: USER -> CUSTOMER (your domain uses CUSTOMER)
        if ("USER".equals(normalized)) {
            return CUSTOMER;
        }

        // strip ROLE_ prefix if present
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


//
//package com.carrental.userservice.entity;
//
//public enum UserRole {
//
//    CUSTOMER,
//    OWNER,
//    ADMIN;
//
//    public static UserRole fromString(String s) {
//        if (s == null) return null;
//
//        String normalized = s.trim().toUpperCase();
//
//        if ("USER".equals(normalized)) {
//            return CUSTOMER;
//        }
//
//        if (normalized.startsWith("ROLE_")) {
//            normalized = normalized.substring(5);
//        }
//
//        try {
//            return UserRole.valueOf(normalized);
//        } catch (IllegalArgumentException ex) {
//            throw new IllegalArgumentException("Unknown role: " + s);
//        }
//    }
//}
