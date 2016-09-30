package com.ankit.pointofsolution.Models;

/**
 * Created by Ankit on 29-Aug-16.
 */
public class Userdata {

    public String role;
    public String userId;
    public String encryptedPassword;

    public Userdata() {
    }

    public Userdata(String role, String userId, String encryptedPassword) {
        this.role = role;
        this.userId = userId;
        this.encryptedPassword = encryptedPassword;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }
}
