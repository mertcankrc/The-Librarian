package com.android.swproject.librarian.Models;


public class UserModel {
    String name;
    String userName;
    String email;
    String password;
    Boolean isGoogleAuth;

    public UserModel(String name, String userName, String email, String password, Boolean isGoogleAuth) {
        this.name = name;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.isGoogleAuth = isGoogleAuth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getGoogleAuth() {
        return isGoogleAuth;
    }

    public void setGoogleAuth(Boolean googleAuth) {
        isGoogleAuth = googleAuth;
    }
}
