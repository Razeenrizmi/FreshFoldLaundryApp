package com.laundry.freshfoldlaundryapp.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String role; // e.g., "customer", "staff", "admin"

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private String token;
    private LocalDateTime tokenExpiry;
    private boolean tokenUsed = false;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getTokenExpiry() {
        return tokenExpiry;
    }

    public void setTokenExpiry(LocalDateTime tokenExpiry) {
        this.tokenExpiry = tokenExpiry;
    }

    public boolean isTokenUsed() {
        return tokenUsed;
    }

    public void setTokenUsed(boolean tokenUsed) {
        this.tokenUsed = tokenUsed;
    }

    // Utility methods for token validation
    public boolean isTokenValid() {
        return token != null && !tokenUsed &&
               tokenExpiry != null && LocalDateTime.now().isBefore(tokenExpiry);
    }

    public void clearToken() {
        this.token = null;
        this.tokenExpiry = null;
        this.tokenUsed = false;
    }
}