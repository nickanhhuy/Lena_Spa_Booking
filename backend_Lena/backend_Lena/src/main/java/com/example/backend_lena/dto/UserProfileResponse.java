package com.example.backend_lena.dto;

public class UserProfileResponse {
    private String username;
    private String email;
    private String phone;
    private String avatarUrl;
    private String role;

    public UserProfileResponse(String username, String email, String phone, String avatarUrl, String role) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.avatarUrl = avatarUrl;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
