package com.example.sightreadingpractice.musicscore;

public class ScoreBrush {

    private Key key;
    private Clef clef;

    public ScoreBrush () {
        this.key = Key.C;
        this.clef = Clef.TREBLE;
    }

    public ScoreBrush (Key key, Clef clef) {
        this.key = key;
        this.clef = clef;
    }

    public Key getKey() {
        return key;
    }

    public Clef getClef() {
        return clef;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public void setClef(Clef clef) {
        this.clef = clef;
    }
}
