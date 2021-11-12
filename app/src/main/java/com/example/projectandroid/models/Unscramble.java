package com.example.projectandroid.models;

public class Unscramble {
    public int highestScore;
    public User user;

    public Unscramble() { }

    public Unscramble(int highestScore, User user) {
        this.highestScore = highestScore;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }
}
