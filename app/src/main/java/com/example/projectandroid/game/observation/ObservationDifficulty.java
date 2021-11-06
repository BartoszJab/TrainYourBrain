package com.example.projectandroid.game.observation;

public enum ObservationDifficulty {
    EASY(10, 1800L),
    MEDIUM(15, 1500L),
    HARD(20, 1300L);

    private final int maxRepetitions;
    private final long imageTimeDuration;

    ObservationDifficulty(int maxRepetitions, long imageTimeDuration) {
        this.maxRepetitions = maxRepetitions;
        this.imageTimeDuration = imageTimeDuration;
    }

    public int getMaxRepetitions() {
        return maxRepetitions;
    }

    public long getImageTimeDuration() {
        return imageTimeDuration;
    }
}
