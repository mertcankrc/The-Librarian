package com.android.swproject.librarian.Models;


public class UserModel {
    public String username;
    public String email;

    public UserModel() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public UserModel(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
