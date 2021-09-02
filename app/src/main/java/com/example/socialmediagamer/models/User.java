package com.example.socialmediagamer.models;

public class User {

    public String id;
    public String userName;
    public String email;
    public String confirmPassword;

    public User() {
    }

    public User(String id, String userName, String password, String confirmPassword) {
        this.id = id;
        this.userName = userName;
        this.email = password;
        this.confirmPassword = confirmPassword;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
