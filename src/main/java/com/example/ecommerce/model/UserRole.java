package com.example.ecommerce.model;

public enum UserRole {
    CUSTOMER("CUSTOMER"),
    ADMIN("ADMIN");

    private final String dbValue;

    UserRole(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }
}