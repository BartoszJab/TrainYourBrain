package com.example.projectandroid.models;

public class Simon {
    public int highestRound;
    public User user;

    public Simon() { }

    public Simon(int highestRound, User user) {
        this.highestRound = highestRound;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getHighestRound() {
        return highestRound;
    }

    public void setHighestRound(int highestRound) {
        this.highestRound = highestRound;
    }
}
