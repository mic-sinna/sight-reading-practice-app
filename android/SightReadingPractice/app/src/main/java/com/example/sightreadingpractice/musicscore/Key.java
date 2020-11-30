package com.example.sightreadingpractice.musicscore;

public enum Key {

    C(0),
    CD(1),
    D(2),
    DE(3),
    E(4),
    F(5),
    FG(6),
    G(7),
    GA(8),
    A(9),
    AB(10),
    B(11);

    private int number;

    Key(int number) {
        this.number = number;
    }

    public int asNumber() {
        return number;
    }

}
