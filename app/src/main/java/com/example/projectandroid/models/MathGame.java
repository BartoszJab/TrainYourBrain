package com.example.projectandroid.models;

public class MathGame {
    public int highestScore;

    public MathGame() {}

    public MathGame(int highestScore) {
        this.highestScore = highestScore;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }
}
