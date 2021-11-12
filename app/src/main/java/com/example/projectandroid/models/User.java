package com.example.projectandroid.models;

import java.util.Objects;

public class User {
    public String username;
    public String avatarUrl;

    public User() {}

    public User(String username) {
        this.username = username;
    }

    public User(String username, String avatarUrl) {
        this.username = username;
        this.avatarUrl = avatarUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                '}';
    }

}
