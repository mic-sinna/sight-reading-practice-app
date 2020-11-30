package com.example.sightreadingpractice.musicscore;

public enum Clef {

    TREBLE(27),
    BASS(15),
    ALTO(27),
    TENOR(27);

    private int staveCenterNoteNum;

    Clef(int staveCenterNoteNum) {
        this.staveCenterNoteNum = staveCenterNoteNum;
    }

    public int getStaveCenterNoteNum() {
        return staveCenterNoteNum;
    }
}
