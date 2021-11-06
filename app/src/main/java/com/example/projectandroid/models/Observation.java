package com.example.projectandroid.models;

import java.util.HashMap;

public class Observation {
    public HashMap<String, Integer> easy;
    public HashMap<String, Integer> medium;
    public HashMap<String, Integer> hard;

    public Observation() {
        this.easy = new HashMap<String, Integer>(){{
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
    }

}
