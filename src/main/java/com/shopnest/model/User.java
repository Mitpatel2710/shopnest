package com.shopnest.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

public class User {
    // ── Getters ───────────────────────────────────────────
    // ── Fields ────────────────────────────────────────────
    @Getter
    private final String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;        // will be hashed in Phase 4 (Spring Security)
    private String phoneNumber;
    private UserRole role;
    private boolean active;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ── Constructor ───────────────────────────────────────
    public User(String id, String firstName, String lastName,
                String email, String password) {
        if (id == null || id.isBlank())            throw new IllegalArgumentException("User ID cannot be empty");
        if (firstName == null || firstName.isBlank()) throw new IllegalArgumentException("First name cannot be empty");
        if (email == null || email.isBlank())      throw new IllegalArgumentException("Email cannot be empty");
        if (!email.contains("@"))                  throw new IllegalArgumentException("Invalid email format");
        if (password == null || password.length() < 6) throw new IllegalArgumentException("Password must be at least 6 characters");

        this.id        = id;
        this.firstName = firstName;
        this.lastName  = lastName;
        this.email     = email;
        this.password  = password;
        this.role      = UserRole.CUSTOMER;     // default role
        this.active    = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // ── Business Methods ──────────────────────────────────
    public String getFullName() {
        return firstName + " " + (lastName != null ? lastName : "");
    }

    public boolean isAdmin() {
        return this.role == UserRole.ADMIN;
    }

    public boolean isSeller() {
        return this.role == UserRole.SELLER;
    }

    public void promoteToSeller() {
        this.role      = UserRole.SELLER;
        this.updatedAt = LocalDateTime.now();
    }

    public void promoteToAdmin() {
        this.role      = UserRole.ADMIN;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.active    = false;
        this.updatedAt = LocalDateTime.now();
    }

    public String getFirstName()        { return firstName; }
    public String getLastName()         { return lastName; }
    public String getEmail()            { return email; }
    public String getPhoneNumber()      { return phoneNumber; }
    public UserRole getRole()           { return role; }
    public boolean isActive()           { return active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // ── Setters ───────────────────────────────────────────
    public void setFirstName(String firstName) {
        if (firstName == null || firstName.isBlank()) throw new IllegalArgumentException("First name cannot be empty");
        this.firstName = firstName;
        this.updatedAt = LocalDateTime.now();
    }
    public void setLastName(String lastName)     { this.lastName = lastName; this.updatedAt = LocalDateTime.now(); }
    public void setPhoneNumber(String phone)     { this.phoneNumber = phone; this.updatedAt = LocalDateTime.now(); }
    public void setPassword(String password) {
        if (password == null || password.length() < 6) throw new IllegalArgumentException("Password too short");
        this.password  = password;
        this.updatedAt = LocalDateTime.now();
    }

    // ── equals, hashCode, toString ────────────────────────
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{id='" + id + "', name='" + getFullName() +
                "', email='" + email + "', role=" + role + "}";
    }
}
