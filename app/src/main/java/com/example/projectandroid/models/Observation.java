package com.example.projectandroid.models;

import android.util.Log;

import com.google.firebase.firestore.Exclude;

import java.util.HashMap;

import kotlin.jvm.JvmField;

public class Observation {
    public HashMap<String, Integer> easy;
    public HashMap<String, Integer> medium;
    public HashMap<String, Integer> hard;
    public User user;
    public int points;

    public Observation() {
    }

    public Observation(User user) {
        this.easy = new HashMap<String, Integer>() {{
            put("guessed", 0);
            put("not_guessed", 0);
        }};

        this.medium = new HashMap<String, Integer>() {{
            put("guessed", 0);
            put("not_guessed", 0);
        }};

        this.hard = new HashMap<String, Integer>() {{
            put("guessed", 0);
            put("not_guessed", 0);
        }};

        this.user = user;
        this.points = 0;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Exclude
    public double getEasyGuessedPercentage() {
        Integer guessed = easy.get("guessed");
        Integer notGuessed = easy.get("not_guessed");
        if (guessed == null || notGuessed == null) {
            return 0;
        } else {
            try {
                return Double.valueOf(guessed) / (Double.valueOf(guessed) + Double.valueOf(notGuessed)) * 100.0;
            } catch (ArithmeticException e) {
                return 0;
            }
        }
    }

    @Exclude
    public double getNormalGuessedPercentage() {
        Integer guessed = medium.get("guessed");
        Integer notGuessed = medium.get("not_guessed");
        if (guessed == null || notGuessed == null) {
            return 0;
        } else {
            try {
                return Double.valueOf(guessed) / (Double.valueOf(guessed) + Double.valueOf(notGuessed)) * 100.0;
            } catch (ArithmeticException e) {
                return 0;
            }
        }
    }

    @Exclude
    public double getHardGuessedPercentage() {
        Integer guessed = hard.get("guessed");
        Integer notGuessed = hard.get("not_guessed");
        if (guessed == null || notGuessed == null) {
            return 0;
        } else {
            try {
                return Double.valueOf(guessed) / (Double.valueOf(guessed) + Double.valueOf(notGuessed)) * 100.0;
            } catch (ArithmeticException e) {
                return 0;
            }
        }
    }


}
