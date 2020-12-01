package com.example.sightreadingpractice.musicscore;

public enum Beat {

    DOUBLE_NOTE(0),
    WHOLE_NOTE(1),
    HALF_NOTE(2),
    QUARTER_NOTE(3),
    EIGHTH_NOTE(5),
    SIXTEENTH_NOTE(6);

    private int rank;

    Beat(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }
}
