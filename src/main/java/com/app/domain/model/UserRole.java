package com.app.domain.model;

public enum UserRole {
    USER("USER"),
    ADMIN("ADMIN");
    private String role;

    UserRole(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }

}