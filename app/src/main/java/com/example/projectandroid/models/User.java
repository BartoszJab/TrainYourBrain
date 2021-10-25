package com.example.projectandroid.models;

import java.util.Objects;

public class User {
    public final String username;
    public String avatarUrl;

    public User(String username) {
        this.username = username;
    }

    public User(String username, String avatarUrl) {
        this.username = username;
        this.avatarUrl = avatarUrl;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(avatarUrl, user.avatarUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, avatarUrl);
    }
}
