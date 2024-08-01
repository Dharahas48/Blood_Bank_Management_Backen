package com.bloodbank.management.requests;

public class ResetPasswordReqDTO {
    private String token;
    private String newPassword;

    // Constructor, getters, and setters

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
