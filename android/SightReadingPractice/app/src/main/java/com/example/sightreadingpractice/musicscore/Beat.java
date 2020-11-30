package com.example.sightreadingpractice.musicscore;

public enum Beat {

    BRIEVE(0),
    SEMIBRIEVE(1),
    MINIM(2),
    CROTCHET(3),
    QUAVER(4),
    SEMIQUAVER(5),
    DEMISEMIQUAVER(6),
    HEMIDEMISEMIQUAVER(7);

    private int rank;

    Beat(int rank) {
        this.rank = rank;
    }

}
